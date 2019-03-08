package com.github.havlikmar.csv.logic;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;

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
		LoaderFormat loaderClass = new LoaderFormat(convertor, nameOfSource, separator);
		return loaderClass.load();
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
				if (nameOfColumn.equals("")) {
					colummnIsNotCreated = true;
				}
			}
			
			String nameOfFile = "";
			boolean fileIsNotCreated = true;
			while (fileIsNotCreated) {
				System.out.println("Zadejte jednoslovny unikátní název nového souboru, bez přípony");
				nameOfFile = loadTextInput().split("\\s")[0] + ".csv";
				fileIsNotCreated = convertor.tableExist(nameOfFile);
				if (nameOfFile.equals(".csv")) {
					fileIsNotCreated = true;
				}
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
						boolean isInFile = true;
						for (int ancestor = 0; ancestor <= positionAnswer; ancestor++) {
							if (!convertor.getTable(thisFile).getColumn(answer[ancestor]).getValues().get(i).equals(convertor.getTable(nameOfFile).getColumn(answer[ancestor]).getValues().get(j))) {
								isInFile = false;
								break;
							}
						}
						
						if (isInFile) {
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
		String answer = scanner.nextLine();
		if (answer.equals("stop application")) {
			System.exit(0);
		}
		return answer;
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
     * Ridici metoda pro zpracování exportu souboru
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
	* Ridici metoda pro zpracování exportu souboru. Slouží pro testování
	     * 
	     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
	     * @param	test	Rozlišení že jde o testovací metodu
	     * @return informace zda se soubor exportoval nebo ne
	     */
	public boolean saveFormat(Convertor convertor, boolean test) {		
		try {
			if (convertor.getFormat(FormatType.IMPORT).isNormalize()) {
				Set<String> setOfTable = convertor.getTables();	
				for (String nameOfTable: setOfTable) {
					if (!saveOneFile(convertor, nameOfTable, true)) {
						return false;
					}
				}
				return true;
				
			} else {
				if (normalizationControl(convertor)) {
					Set<String> setOfTable = convertor.getTables();	
					for (String nameOfTable: setOfTable) {
						if (!saveOneFile(convertor, nameOfTable, true)) {
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
     * @param	nameOfSource	Název souboru
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveOneFile (Convertor convertor, String nameOfSource) {
		FileExporter fileExporter = new FileExporter(convertor, nameOfSource);
		return fileExporter.exportFile();
	}
	
	/**
     * Metoda pro samotnou implementaci zpracování exportu souboru. Metoda slouzi k testovani
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru
     * @param	test	Rozliseni ze metoda slouzi k testovani
     * @return informace zda se soubor exportoval nebo ne
     */
	public boolean saveOneFile (Convertor convertor, String nameOfSource, boolean test) {
		FileExporter fileExporter = new FileExporter(convertor, nameOfSource, true);
		return fileExporter.exportFile();
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
