package com.github.havlikmar.csv.logic;

import java.io.FileWriter;
import java.util.ArrayList;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.opencsv.CSVWriter;

/**
 * Třída FileExporter je zodpovědná za export transformovaneho souboru.
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class FileExporter {
	private Convertor convertor;
	private String nameOfSource = "";
	private boolean test = false;
	
	/**
     * Konstruktor třídy FileExporter pro tabulku faktů
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     */
	public FileExporter(Convertor convertor) {
		this.convertor = convertor;
	}
	
	/**
     * Konstruktor třídy FileExporter pro dimenzionální tabulku
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru
     */
	public FileExporter(Convertor convertor, String nameOfSource) {
		this.convertor = convertor;
		this.nameOfSource = nameOfSource;
	}
	
	/**
     * Konstruktor třídy FileExporter pro dimenzionální tabulku a testovani
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru
     * @param	test	Rozlišení že se testuje
     */
	public FileExporter(Convertor convertor, String nameOfSource, boolean test) {
		this.convertor = convertor;
		this.nameOfSource = nameOfSource;
		this.test = test;
	}
	
	/**
     * Konstruktor třídy FileExporter pro testování
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	test	Rozlišení že se testuje
     */
	public FileExporter(Convertor convertor, boolean test) {
		this.convertor = convertor;
		this.test = test;
	}
	
	/**
     * Metoda pro export souborů
     * 
     * @return informace zda se soubor načetl
     */
	public boolean exportFile() {
		try {
			if (nameOfSource.equals("")) {
				nameOfSource = convertor.getMainFile();
			}
			String exportName = "export_" + nameOfSource;
			ArrayList<String> header = convertor.getTable(nameOfSource).getColumns();
			String[] headerList = new String[header.size()];
			headerList = changeToStringArray(header);
			String directory = System.getProperty("user.dir") + "\\" + exportName;
			if (test) {
				directory = System.getProperty("user.dir") + "\\" + "src\\main\\resources\\test\\testExport.csv";
			}
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
}
