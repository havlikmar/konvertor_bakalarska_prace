package com.github.havlikmar.format.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CsvNor implements IFormat{
	
	public boolean loadFormat(Convertor convertor, String nameOfSource, char separator) {
		try {
			String directory = System.getProperty("user.dir") + "\\" + nameOfSource;
			CSVReader importReader = new CSVReader(new FileReader(directory), separator);
			List<String[]> input = importReader.readAll();
			String[] header = input.get(0);
			int headerSize = header.length;
			
			for(String[] row: input) {
				if (row.length != headerSize) {
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
	
	public boolean saveFormat(Convertor convertor) {
		Set<String> setOfTable = convertor.getTables();	
		for (String nameOfTable: setOfTable) {
			if (!saveOneFile(convertor, nameOfTable)) {
				return false;
			}
		}
		return true;
	}
	
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
	
	public String[] changeToStringArray(ArrayList<String> list) {
		String[] newList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			newList[i] = list.get(i);
		}
		return newList;
	}	
	
	public String getName() {
		return "CsvNor";
	}
	
	public boolean isNormalize() {
		return true;
	}
}
