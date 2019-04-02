package com.github.havlikmar.konvertor_bakalarska_prace.main;

import java.util.Scanner;
import java.util.Set;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;

/**
 * Třída TextProcesor je zodpovědná za implementaci textového rozhraní mezi uživatelem a počítačem
 * 
 * @author     Martin Havlík
 * @version    20.2.2019
 */
public class TextProcesor {
	private Convertor convertor;
	private Scanner scanner;
	private Set<String> formatsName;
	private boolean isMainFile = true;
	
	/**
     * Kontruktor pro vytvoření instance třídy TextProcesor a zahájení textového rozhraní.
     * 
     * @param	convertor	odkaz na řídící třídu logiky
     */
	public TextProcesor (Convertor convertor) {
		this.convertor = convertor;
	}
	
	/**
     * Metoda pro inicializaci aplikace
     * 
     */
	public void initialization() {
		textLogic();
	}
	
	/**
     * Metoda pro vypsání formátu při výběru formátu uživatelem
     * 
     * @param	inputText	Dodatečný text, lišicí se oproti zbytku
     */
	public void allformatOutput(String inputText) {
		formatsName = convertor.getFormats().keySet();
		String output = inputText + " Podporované formáty jsou:";
		for (String i: formatsName){
			output = output + " " + i;
		}
		System.out.println(output);
	}
	
	/**
     * Metoda pro načtení reakce uživatele
     * 
     * @return	String Vstup uživatele
     */
	public String loadTextInput() {
		scanner = new Scanner(System.in);
		String answer = scanner.nextLine();
		if (answer.equals("stop application")) {
			System.exit(0);
		}
		return answer;
	}
	
	/**
     * Metoda pro zpracování výběru formátu
     * 
     * @param	text	Úvodní text vypsaný pro uživatele
     * @return	String	Reakce uživatele
     */
	public String selectFormat(String text) {
		String inputText = null;
		boolean repeat = true;
		String outputText = "";
		while (repeat) {
			outputText = outputText + text;
			allformatOutput(outputText);
			inputText = loadTextInput();
			for (String i: formatsName) {
				if (inputText.equals(i)) {
					repeat = false;
				}
			}
			outputText = "";
			outputText = "Vybraný formát není podporován. ";
		}
		return inputText;
	}
	
	/**
     * Metoda pro zpracování výběru vstupních souborů
     * 
     * @param	outputTrue	úvodní text vypsáný pro uživatele
     * @param	type	Informace zda jde o vstupní nebo výstupní formát
     */
	public void selectSource(String outputTrue, FormatType type) {
		boolean isCorect = false;
		String outputFalse = "";
		String nameOfSource = "";
		while (!isCorect) {
			System.out.println(outputFalse + outputTrue);
			nameOfSource = loadTextInput();
			if (convertor.tableExist(nameOfSource)) {
				outputFalse = "Zadaný soubor již byl vybrán. ";
				continue;
			}
			boolean isSeparator = false;
			String separator = ",";
			String output = "Zadejte separátor.";
			while (!isSeparator) {
				System.out.println(output);
				separator = loadTextInput();
				if (separator.length() == 1) {
					isSeparator = true;
				}
				output = "Chybně zadaný separátor. Musí mít pouze jeden znak. " + output;
			}
			
			isCorect = convertor.getFormat(type).loadFormat(convertor, nameOfSource, separator.charAt(0));
			outputFalse = "Soubor neexistuje, nebo neodpovídá zvolenému formátu. ";
		}
		
		if (isMainFile) {
			convertor.setMainFile(nameOfSource);
			isMainFile = false;
		}
	}
	
	/**
     * Metoda pro zpracování výběru vedlejších souborů
     */
	public void selectOtherSources() {
		if (convertor.getFormat(FormatType.IMPORT).isNormalize()) {
			boolean anotherFile = true;
			while (anotherFile) {
				selectSource("Zadejte název vedlejšího (dimensionálního) vstupního souboru.", FormatType.IMPORT);
				boolean isCorect = false;
				while (!isCorect) {
					System.out.println("Přejete si přidat další vedlejší formát. [Y/N]");
					String answer = loadTextInput();
					switch (answer) {
						case "Y": 	isCorect = true;
									break;
						case "N": 	isCorect = true;
									anotherFile = false;
									break;
					}
				}
			}
		}
	}
	
	/**
     * Metoda pro zpracování reakce mezi uživatelem a počítačem
     * 
     */
	public void textLogic() {
		convertor.setStartFormat(selectFormat("Vyberte vstupní formát."));
		convertor.setEndFormat(selectFormat("Vyberte výstupní formát."));
		selectSource("Zadejte název hlavního vstupního souboru.", FormatType.IMPORT);
		selectOtherSources();
		if (convertor.getFormat(FormatType.EXPORT).saveFormat(convertor)) {
			System.out.println("Transformace proběhla v pořádku");
		} else {
			System.out.println("Při transformaci došlo k nečekané chybě");
		}
	}	
}
