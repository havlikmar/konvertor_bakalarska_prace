package com.github.havlikmar.csv.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;
import com.opencsv.CSVReader;

/**
 * Třída LoadFormat je zodpovědná načítání souborů.
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class LoaderFormat {
	private Convertor convertor;
	private String nameOfSource;
	private char separator;
	
	/**
     * Konverot třídy loadFormat
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     */
	public LoaderFormat (Convertor convertor, String nameOfSource, char separator) {
		this.convertor = convertor;
		this.nameOfSource = nameOfSource;
		this.separator = separator;
	}
	
	/**
     * Metoda pro uložení daného souboru do vnitřní paměti
     * 
     * @return informace zda se soubor načetl nebo ne
     */
	public boolean load() {
		try {
			String directory = System.getProperty("user.dir") + "\\" + nameOfSource;
			@SuppressWarnings("deprecation")
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
			return true;
		}
		
		catch (FileNotFoundException e) { 	
			return false;
		}
		
		catch (IOException e) { 
			return false;
		}
	}
}
