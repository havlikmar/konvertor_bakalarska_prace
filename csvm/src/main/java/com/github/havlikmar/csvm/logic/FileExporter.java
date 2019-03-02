package com.github.havlikmar.csvm.logic;

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
	
	/**
     * Konstruktor třídy FileExporter pro tabulku faktu
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     */
	public FileExporter(Convertor convertor) {
		this.convertor = convertor;
	}
	
	/**
     * Konstruktor třídy FileExporter pro dimensionálni tabulku
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     */
	public FileExporter(Convertor convertor, String nameOfSource) {
		this.convertor = convertor;
		this.nameOfSource = nameOfSource;
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
			String directory = System.getProperty("user.dir") + "\\" + exportName;
			CSVWriter exportWriter = new CSVWriter(new FileWriter(directory));
			ArrayList<String[]> exportData = new ArrayList<String[]>();
			for (String i: header) {
				String[] columnArray= changeToStringArray(convertor.getTable(nameOfSource).getColumn(i).getValues(), "");
				exportData.add(columnArray);
			}
			int countOfValue = exportData.get(0).length;
			for (int i = 0; i < countOfValue - 1; i++) {
				String[] addData = new String[header.size()];
				int index = 0;
				for (String[] k: exportData) {
					addData[index] = k[i];
					index ++;
				}
				exportWriter.writeNext(addData);
			}
			
			String[] title = new String[2];
			if (convertor.getTable(nameOfSource).containMetadata("description-file")) {
				title[0] = "#TITLE";
				title[1] = convertor.getTable(nameOfSource).getMetadata("description-file").getMetadata().get(0);
			} else {
				title[0] = "#TITLE";
				title[1] = "";
			}
			exportWriter.writeNext(title);
	
			String[] headerList = new String[header.size() + 1];
			headerList = changeToStringArray(header, "#HEADER");
			exportWriter.writeNext(headerList);
			
			String[] metaList = new String[header.size() + 1];
			int count = 1;
			metaList[0] = "#TYPE";
			for (String head: header) {
				if (convertor.getTable(nameOfSource).getColumn(head).containMetadata("dataType")) {
					metaList[count] = convertor.getTable(nameOfSource).getColumn(head).getMetadata("dataType").getMetadata().get(0);
				} else {
					metaList[count] = "";
				}
				count++;
			}
			
			exportWriter.writeNext(metaList);
			
			metaList = new String[header.size() + 1];
			count = 1;
			metaList[0] = "#WIDTH";
			for (String head: header) {
				if (convertor.getTable(nameOfSource).getColumn(head).containMetadata("max-width")) {
					metaList[count] = convertor.getTable(nameOfSource).getColumn(head).getMetadata("max-width").getMetadata().get(0);
				} else {
					metaList[count] = "";
				}
				count++;
			}
			
			exportWriter.writeNext(metaList);
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
     * @param	meta	název metadat
     * @return Výstupní pole Stringů
     */
	public String[] changeToStringArray(ArrayList<String> list, String meta) {
		String[] newList = new String[list.size() + 1];
		if (meta.equals("")) {
			for (int i = 0; i < list.size(); i++) {
				newList[i] = list.get(i);
			}	
		} else {
			newList[0] = meta;
			for (int i = 0; i < list.size(); i++) {
				newList[i+1] = list.get(i);
			}	
		}
		return newList;
	}
}
