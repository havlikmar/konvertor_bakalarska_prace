package com.github.havlikmar.csvm.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	private String name;
	private String metaDictionary = "metaDictionaryCSVM.csv";
	
	/**
     * Konverot třídy loadFormat
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     */
	public LoaderFormat (Convertor convertor, String nameOfSource, char separator, String name) {
		this.convertor = convertor;
		this.nameOfSource = nameOfSource;
		this.separator = separator;
		this.name = name;
	}
	
	/**
     * Metoda pro zpracování importu slovniku metadat
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor importoval nebo ne
     */
	public boolean loadMetadata(Convertor convertor) {
		try {
			if (convertor.containMetaDictionory(name)) {
				return true;
			}
			String directory = System.getProperty("user.dir") + "\\" + metaDictionary;
			CSVReader importReader = new CSVReader(new FileReader(directory));
			List<String[]> input = importReader.readAll();
			String[] specific = new String[input.size()];
			String[] general = new String[input.size()];
			int size = 0;
			for (String[] k: input) {
				specific[size] = k[0];
				general[size]= k[1];
				size++;
			}	
			convertor.addMetadictionary(specific, general, name);
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
	
	/**
     * Metoda pro uložení daného souboru do vnitřní paměti
     * 
     * @return informace zda se soubor načetl nebo ne
     */
	public boolean load() {
		try {
			if (!loadMetadata(convertor)) {
				System.out.println("Metadata se nenačetla");
				return false;
			};
			String directory = System.getProperty("user.dir") + "\\" + nameOfSource;
			@SuppressWarnings("deprecation")
			CSVReader importReader = new CSVReader(new FileReader(directory), separator);
			List<String[]> input = importReader.readAll();	
			Table newTable = new Table(nameOfSource);
			convertor.addTable(nameOfSource, newTable);
			ArrayList<String> header = new ArrayList<String>();
			int metaStart = 0;
			for (String[] k: input) {
				if (Character.toString(k[0].charAt(0)).equals("#")) {
					for (int i = 0; i < k.length; i++) {
						ArrayList<String> listMeta = new ArrayList<String>();
						if (k[0].toUpperCase().equals("#TITLE")  && i == 0) {
							listMeta.add(k[1]);
							convertor.getTable(nameOfSource).addMetadata(convertor.getMetaDictionary(name).getGeneral(k[0]), listMeta);
						};
						
						if (k[0].toUpperCase().equals("#HEADER") && i > 0) {
							convertor.getTable(nameOfSource).addColumn(k[i]);
							header.add(k[i]);
						};
						
						String meta = k[0];
						if (metaStart > 1 && i > 0) {
							listMeta.add(k[i]);
							convertor.getTable(nameOfSource).getColumn(header.get(i-1)).addMetadata(convertor.getMetaDictionary(name).getGeneral(meta), listMeta);
						}
					}
					metaStart++;
				}
			}
			
			for (String[] row: input) {
				if (!Character.toString(row[0].charAt(0)).equals("#")) {
					for (int i = 0; i < header.size(); i++) {
						newTable.getColumn(header.get(i)).addValue(row[i]);
					}
				}
			}
			importReader.close();
			return true;
		}
		catch (FileNotFoundException e) { 	
			System.out.println(e);
			return false;
		}
		
		catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}
}
