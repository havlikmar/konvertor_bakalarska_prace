package com.github.havlikmar.csv.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Třída CsvDen je zodpovědná za implementaci zpracování načítání a export transformovaných dat v denormalizovaném CSV.
 * 
 * @author     Martin Havlík
 * @version    24.2.2019
 */
public class CsvDen implements IFormat {
	private Scanner scanner;
	private String updateTable;

	/**
     * Metoda pro uložení daného souboru do vnitřní paměti
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     * @return informace zda se soubor načetl nebo ne
     */
	public boolean loadFormat(Convertor convertor, String nameOfSource, char separator) {
		try {
			String directory = System.getProperty("user.dir") + "\\" + nameOfSource;
			CSVReader importReader = new CSVReader(new FileReader(directory), separator);
			List<String[]> input = importReader.readAll();
			String[] header = input.get(0);
			int headerSize = header.length;
			
			for(String[] row: input) {
				if (row.length != headerSize) {
					importReader.close();
					return false;
				}
			}
			
			Table newTable = new Table(nameOfSource);
			convertor.addTable(nameOfSource, newTable);
			
			for (String columnName: header) {
				newTable.addColumn(columnName);
			}
			
			for(String[] row: input) {
				if (!row.equals(header)) {
					for (int i = 0; i < headerSize; i++) {
						newTable.getColumn(header[i]).addValue(row[i]);
					}
				}
			}
			importReader.close();
		}
		
		catch (FileNotFoundException e) { 	
			return false;
		}
		
		catch (IOException e) {
			return false;
		}
		return true;
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
				convertor.getTable(mainFile).removeColumn(answer[1]);
				convertor.removeTable(updateTable);
			}
			return true;
		}
		catch (Exception e) {
			return false;	
		}
	}
	
	/**
     * Řídící metoda pro zpracování exportu souboru
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
     * Metoda pro načtení reakce uživatele
     * 
     * @return	String Vstup uživatele
     */
	public String loadTextInput() {
		scanner = new Scanner(System.in);
		return scanner.nextLine();
	}
	
	/**
     * Metoda pro samotnou implementaci zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveOneFile(Convertor convertor) {
		try {
			String nameOfSource = convertor.getMainFile();
			String exportName = "export_" + nameOfSource;
			ArrayList<String> header = convertor.getTable(nameOfSource).getColumns();
			String[] headerList = new String[header.size()];
			headerList = changeToStringArray(header);
			String directory = System.getProperty("user.dir") + "\\" + exportName;
			CSVWriter exportWriter = new CSVWriter(new FileWriter(directory));
			exportWriter.writeNext(headerList);
			ArrayList<String[]> exportData = new ArrayList<String[]>();
			for (String i: header) {
				String[] columnArray= changeToStringArray(convertor.getTable(nameOfSource).getColumn(i).getValues());
				exportData.add(columnArray);
			}
			int countOfValue = exportData.get(0).length;
			for (int i = 0; i < countOfValue; i++) {
				String[] addData = new String[header.size()];
				int index = 0;
				for (String[] k: exportData) {
					addData[index] = k[i];
					index ++;
				}
				exportWriter.writeNext(addData);
			}
			exportWriter.close();
			return true;
		}
		catch (Exception e) {
			return false;	
		}
	}
	
	/**
     * Metoda pro změnu ArrayListu na pole Stringů
     * 
     * @param	list	Vstupní ArrayList
     * @return Výstupní pole Stringů
     */
	public String[] changeToStringArray(ArrayList<String> list) {
		String[] newList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			newList[i] = list.get(i);
		}
		return newList;
	}
	
	/**
     * Metoda pro zjištění názvu formátu
     * 
     * @return Název formátu
     */
	public String getName() {
		return "CsvDen";
	}
	
	/**
     * Metoda pro zjištění zda je formát normalizovaný nebo ne
     * 
     * @return informace zda je formát normalizovaný nebo ne
     */
	public boolean isNormalize() {
		return false;
	}
}
