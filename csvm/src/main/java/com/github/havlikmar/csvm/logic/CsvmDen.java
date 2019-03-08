package com.github.havlikmar.csvm.logic;

import java.util.ArrayList;
import java.util.Scanner;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Metadata;

/**
 * Třída CsvmDen implementuje denormalizovaný způsob reprezentace CSVM. 
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class CsvmDen implements IFormat {
	private String updateTable;
	private Scanner scanner;

	/**
     * Metoda pro zjištění zda je formát normalizovaný nebo ne
     * 
     * @return informace zda je formát normalizovaný nebo ne
     */
	public boolean isNormalize() {
		return false;
	}
	
	/**
     * Metoda pro zjištění názvu formátu
     * 
     * @return Název formátu
     */
	public String getName() {
		return "CsvmDen";
	}
	
	/**
     * Metoda pro uložení daného souboru do vnitřní paměti
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     * @return informace zda se soubor načetl nebo ne
     */
	public boolean loadFormat(Convertor convertor, String nameOfSource, char separator) {
		LoaderFormat loaderClass = new LoaderFormat(convertor, nameOfSource, separator, getName());
		return loaderClass.load();
	}
	
	/**
     * Metoda pro uložení daného souboru do vnitřní paměti. Slouží k testování
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     * @param	test		rozlišení, že jde o test
     * @return informace zda se soubor načetl nebo ne
     */
	public boolean loadFormat(Convertor convertor, String nameOfSource, char separator, boolean test) {
		LoaderFormat loaderClass = new LoaderFormat(convertor, nameOfSource, separator, getName(), true);
		return loaderClass.load();
	}
	
	/**
     * Ridici metoda pro zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveFormat(Convertor convertor) {
		try {
			if (!convertor.getFormat(FormatType.IMPORT).isNormalize()) {
				return saveOneFile(convertor);
			} else {
				if (denormalization(convertor)) {
					return saveOneFile(convertor);
				}
				return false;
			}
		}
		
		catch (Exception e) {
			return false;	
		}
	}
	
	/**
     * Ridici metoda pro zpracování exportu souboru. Slouží k testovani
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	test	rozlišeni že jde o testovací metodu
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveFormat(Convertor convertor, boolean test) {
		try {
			if (!convertor.getFormat(FormatType.IMPORT).isNormalize()) {
				return saveOneFile(convertor, true);
			} else {
				if (denormalization(convertor)) {
					return saveOneFile(convertor, true);
				}
				return false;
			}
		}
		
		catch (Exception e) {
			return false;	
		}
	}
	
	/**
     * Metoda pro samotnou implementaci zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveOneFile(Convertor convertor) {
		FileExporter fileExporter = new FileExporter(convertor);
		return fileExporter.exportFile();
	}
	
	/**
     * Metoda pro samotnou implementaci zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	test	rozlišeni že jde o testovaci metodu
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveOneFile(Convertor convertor, boolean test) {
		FileExporter fileExporter = new FileExporter(convertor, true);
		return fileExporter.exportFile();
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
     * Metoda pro zobrazení sloupců vedlejší (dimenzionální) tabulky
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return vracený seznam sloupců v podobě inline výpisu
     */
	public String getOtherFileColumn(Convertor convertor) {
		String otherFileColumn = "";
		String mainFile = convertor.getMainFile();
		for (String table: convertor.getTables()) {
			if (!table.equals(mainFile)) {
				updateTable = table;
				for (String column: convertor.getTable(table).getColumns()) {
					otherFileColumn = otherFileColumn + " " + column;
				}
				return otherFileColumn;
			}	
		}
		return otherFileColumn;
	}
	
	/**
     * Metoda pro zpracování denormalizace v případě, že vstup je normalizovaný
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se denormalizace povedla nebo ne
     */
	public boolean denormalization (Convertor convertor) {
		try {
			int countOfFileOrigin = convertor.getTables().size()-1;
			for (int countOfFile = 0; countOfFile < countOfFileOrigin; countOfFile++) {
				boolean isMerge = false;
				String[] answer = {};
				while (!isMerge) {
					System.out.println("Napište sloupec z hlavního souboru a vedlejšího, mezi kterými chcete vytvořit spojení pro sjednocení do jednoho souboru.");
					System.out.println("Formát je ve tvaru: nazevSloupceHlavniSoubor nazevSloupceVedlejsiSoubor");
					System.out.println("Hlavní soubor: " + getMainFileColumn(convertor));
					System.out.println("Vedlejší soubor: " + getOtherFileColumn(convertor));
					answer = loadTextInput().split("\\s");
					if (answer.length == 2 && isInMainFile (answer, convertor) && isInOtherFile (answer, convertor)) {
						isMerge = true;
					}				
				}
				
				ArrayList<String> newColumn = new ArrayList<String>();
				String mainFile = convertor.getMainFile();
				
				for (String column: convertor.getTable(updateTable).getColumns()) {
					if (!column.equals(answer[1])) {
						convertor.getTable(mainFile).addColumn(column);
						for (String meta: convertor.getTable(updateTable).getColumn(column).getMetadatas()) {
							Metadata metadata = convertor.getTable(updateTable).getColumn(column).getMetadata(meta);
							convertor.getTable(mainFile).getColumn(column).addMetadata(metadata.getName(), metadata.getMetadata());
						}
						newColumn.add(column);
					}
				}
				ArrayList<String> mainFileValue = convertor.getTable(mainFile).getColumn(answer[0]).getValues();
				ArrayList<String> answerOtherValue = convertor.getTable(updateTable).getColumn(answer[1]).getValues();
		
				for (String column: newColumn) {
					for (int i=0; i < mainFileValue.size(); i++) {
						for (int j=0; j < answerOtherValue.size(); j++) {
							if (answerOtherValue.get(j).equals(mainFileValue.get(i))) {
								convertor.getTable(mainFile).getColumn(column).getValues().add(convertor.getTable(updateTable).getColumn(column).getValues().get(j));
								break;
							}
						}
					}
				}
				convertor.getTable(mainFile).removeColumn(answer[0]);
				convertor.removeTable(updateTable);
			}
			return true;
		}
		catch (Exception e) {
			return false;	
		}
	}
	
	/**
     * Metoda pro zjištění zda se daný sloupec vyskytuje ve vedlejší (dimensionální) tabulce
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	answer	Vstup uživatele, který chceme ověřit
     * @return informace zda v tabulce je nebo ne
     */
	public boolean isInOtherFile (String[] answer, Convertor convertor) {
		for (String column: convertor.getTable(updateTable).getColumns()) {
			if (answer[1].equals(column)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Metoda pro zjištění zda se daný sloupec vyskytuje v hlavní tabulce (tabulce faktů)
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	answer	Vstup uživatele, který chceme ověřit
     * @return informace zda v tabulce je nebo ne
     */
	public boolean isInMainFile (String[] answer, Convertor convertor) {
		for (String column: convertor.getTable(convertor.getMainFile()).getColumns()) {
			if (answer[0].equals(column)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Metoda pro zobrazení sloupců hlavní tabulky (tabulky faktů)
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return vracený seznam sloupců v podobě inline výpisu
     */
	public String getMainFileColumn(Convertor convertor) {
		String mainFileColumn = "";
		for (String column: convertor.getTable(convertor.getMainFile()).getColumns()) {
			mainFileColumn = mainFileColumn + " " + column;
		}
		return mainFileColumn;
	}
}
