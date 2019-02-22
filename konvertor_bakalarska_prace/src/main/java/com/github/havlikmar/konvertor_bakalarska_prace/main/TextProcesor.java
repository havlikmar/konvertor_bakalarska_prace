package com.github.havlikmar.konvertor_bakalarska_prace.main;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

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
	
	/**
     * Kontruktor pro vytvoření instance třídy TextProcesor a zahájení textového rozhraní.
     * 
     * @param	convertor	odkaz na řídící třídu logiky
     */
	public TextProcesor (Convertor convertor) {
		this.convertor = convertor;
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
		return scanner.nextLine();
	}
	
	/**
     * Metoda pro zpracování výběru formátu
     * 
     * @param	text	rozlišení zda jde o zdrojový nebo cílový formát
     * @return	String	Dodatečný text, lišicí se oproti zbytku 
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
			outputText = "Vybrany format neni podporovan. ";
		}
		return inputText;
	}
	
	public void selectSource(String outputTrue) {
		try {
			boolean isCorect = false;
			String outputFalse = "";
			while (!isCorect) {
				System.out.println(outputFalse + outputTrue);
				convertor.setMainFile(loadTextInput());	
				isCorect = convertor.getFormat(1).loadFormat(convertor);
				outputFalse = "Soubor neexistuje, nebo neodpovídá zvolenému formátu. ";
			}	
		}
		
		catch (FileNotFoundException e) {
			
		}
	}
	
	/**
     * Metoda pro zpracování reakce mezi uživatelem a počítačem
     * 
     */
	public void textLogic() {
		convertor.setStartFormat(selectFormat("Vyberte vstupni format."));
		convertor.setEndFormat(selectFormat("Vyberte vystupni format."));
		selectSource("Zadejte název hlavního vstupniho souboru.");
		if (!convertor.getFormat(1).isNormalize()) {
			boolean anotherFile = true;
			while (anotherFile) {
//				selectSource("Zadejte název vedlejšího (dimensionálního) vstupniho souboru.");
				boolean isCorect = false;
				while (!isCorect) {
					System.out.println("Přejete si přidat další vedlejší formát. [Y/N]");
					String answer = loadTextInput();
					System.out.println(answer);
					switch (answer) {
						case "Y": 	isCorect = true;
						case "N": 	isCorect = true;
									anotherFile = false;
					}
				}
			}
		}
	}	
}
