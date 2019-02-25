package com.github.havlikmar.csv.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Třída CsvNor je zodpovědná za implementaci zpracování načítání a export transformovaných dat v normalizovaném CSV.
 * 
 * @author     Martin Havlík
 * @version    24.2.2019
 */
public class CsvNor implements IFormat{
	private Scanner scanner;

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
     * Metoda pro zpracování normalizace v případě, že vstup je denormalizovaný
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se normalizace povedla nebo ne
     */
	public boolean normalization (Convertor convertor) {
		try {
			String thisFile = convertor.getMainFile();
			boolean isSplit = false;
			String[] answer = {};
			while (!isSplit) {
				System.out.println("Napište sloupce z hlavního souboru z kterých chcete vytvořit novou dimensionální tabulku.");
				System.out.println("Formát je ve tvaru: nazevSloupce nazevSloupce ... ...");
				System.out.println("Hlavní soubor: " + getMainFileColumn(convertor));
				answer = loadTextInput().split("\\s");
				if (isInMainFile (answer, convertor)) {
					isSplit = true;
				}	
			}
			
			String nameOfColumn = "";
			boolean colummnIsNotCreated = true;
			while (colummnIsNotCreated) {
				System.out.println("Zadejte jednoslovný unikátní název sloupce, který bude sloužit jako primární/cizí klíč");
				nameOfColumn = loadTextInput().split("\\s")[0];
				colummnIsNotCreated = convertor.getTable(thisFile).columnExist(nameOfColumn);
			}
			
			String nameOfFile = "";
			boolean fileIsNotCreated = true;
			while (fileIsNotCreated) {
				System.out.println("Zadejte jednoslovny unikátní název nového souboru, bez přípony");
				nameOfFile = loadTextInput().split("\\s")[0] + ".csv";
				fileIsNotCreated = convertor.tableExist(nameOfFile);
			}
			
			ArrayList<String[]> moveColumn = new ArrayList<String[]>();
			Table newTable = new Table(nameOfFile);
			convertor.addTable(nameOfFile, newTable);
			convertor.getTable(nameOfFile).addColumn(nameOfColumn);
			convertor.getTable(thisFile).addColumn(nameOfColumn);
			
			for (String column: answer) {
				String[] columnArray= changeToStringArray(convertor.getTable(thisFile).getColumn(column).getValues());
				moveColumn.add(columnArray);
				convertor.getTable(nameOfFile).addColumn(column);
			}		
			
			for (int i = 0; i < convertor.getTable(thisFile).getColumn(answer[0]).getValues().size(); i++) {
				int size = convertor.getTable(nameOfFile).getColumn(nameOfColumn).getValues().size();
				int index = 0;
				int positionInNewFile = 0;
				int positionAnswer = 0;
				for (String[] k: moveColumn) {
					if (positionAnswer > answer.length) {
						break;
					}
					boolean isInColumn = false; 
					for (int j = index; j < size; j++) {
						if(k[i].equals(convertor.getTable(nameOfFile).getColumn(answer[positionAnswer]).getValues().get(j))) {
							if (positionAnswer > 0) {
								String newTableAncestorValue = convertor.getTable(nameOfFile).getColumn(answer[positionAnswer-1]).getValues().get(j);
								String oldTableAncestorValue = convertor.getTable(thisFile).getColumn(answer[positionAnswer-1]).getValues().get(i);
								if (newTableAncestorValue.equals(oldTableAncestorValue)) {
									isInColumn = true;
									index = j;
									positionAnswer ++;
									if (positionAnswer == answer.length) {
										positionInNewFile = j+1;
										convertor.getTable(thisFile).getColumn(nameOfColumn).addValue(Integer.toString(positionInNewFile));
									}
									break;	
								}
							} else {
								isInColumn = true;
								index = j;
								positionAnswer ++;
								if (positionAnswer == answer.length) {
									positionInNewFile = j+1;
									convertor.getTable(thisFile).getColumn(nameOfColumn).addValue(Integer.toString(positionInNewFile));
								}
								break;
							}
						}
					}
					
					if (!isInColumn) {
						size++;
						convertor.getTable(nameOfFile).getColumn(nameOfColumn).addValue(Integer.toString(size));
						int position = 0;
						for (String[] name: moveColumn) {
							convertor.getTable(nameOfFile).getColumn(answer[position]).addValue(name[i]);
							positionInNewFile = convertor.getTable(nameOfFile).getColumn(nameOfColumn).getValues().size();
							position++;
						}
						convertor.getTable(thisFile).getColumn(nameOfColumn).addValue(Integer.toString(positionInNewFile));
						break;
					}
				}
			}
			for (String column: answer) {
				convertor.getTable(thisFile).removeColumn(column);
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
     * Metoda pro zjištění zda se dané sloupce vyskytuje v hlavní tabulce (tabulce faktů)
     * 
     * @param	answer	Vstup uživatele, který chceme ověřit
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda v tabulce je nebo ne
     */
	public boolean isInMainFile (String[] answer, Convertor convertor) {
		for (int i = 0; i < answer.length; i++) {
			boolean inTable = false;
			for (String column: convertor.getTable(convertor.getMainFile()).getColumns()) {
				if (answer[i].equals(column)) {
					inTable = true;
				}
			}
			if (!inTable) {
				return false;
			}
				
		}
		return true;
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
     * Metoda řídící zpracování normalizace
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se normalizace povedla nebo ne
     */
	public boolean normalizationControl(Convertor convertor) {
		boolean continueRequest = true;
		boolean withoutError = true;
		while (continueRequest) {
			withoutError = normalization(convertor);
			if (!withoutError) {
				return false;
			}
			System.out.println("Přejete si provést další úpravy. [Y/N]");
			String answer = loadTextInput();
			switch (answer) {
				case "Y": 	continueRequest = true;
							break;
				case "N": 	continueRequest = false;
							break;
			}
		}
		return true;
	}
	
	/**
     * Řídící metoda pro zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveFormat(Convertor convertor) {		
		try {
			if (convertor.getFormat(FormatType.IMPORT).isNormalize()) {
				Set<String> setOfTable = convertor.getTables();	
				for (String nameOfTable: setOfTable) {
					if (!saveOneFile(convertor, nameOfTable)) {
						return false;
					}
				}
				return true;
				
			} else {
				if (normalizationControl(convertor)) {
					Set<String> setOfTable = convertor.getTables();	
					for (String nameOfTable: setOfTable) {
						if (!saveOneFile(convertor, nameOfTable)) {
							return false;
						}
					}
					return true;
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
	public boolean saveOneFile (Convertor convertor, String nameOfSource) {
		try {
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
		return "CsvNor";
	}
	
	/**
     * Metoda pro zjištění zda je formát normalizovaný nebo ne
     * 
     * @return informace zda je formát normalizovaný nebo ne
     */
	public boolean isNormalize() {
		return true;
	}
}
