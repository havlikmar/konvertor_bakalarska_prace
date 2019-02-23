package com.github.havlikmar.format.logic;

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

public class CsvDen implements IFormat {
	private Scanner scanner;
	private String updateTable;

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
	
	public String getMainFileColumn(Convertor convertor) {
		String mainFileColumn = "";
		for (String column: convertor.getTable(convertor.getMainFile()).getColumns()) {
			mainFileColumn = mainFileColumn + " " + column;
		}
		return mainFileColumn;
	}
	
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
	
	public boolean denormalization (Convertor convertor) {
		try {
			int countOfFileOrigin = convertor.getTables().size()-1;
			for (int countOfFile = 0; countOfFile < countOfFileOrigin; countOfFile++) {
				System.out.println(convertor.getTables().size()-1);
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
	
	public boolean isInMainFile (String[] answer, Convertor convertor) {
		for (String column: convertor.getTable(convertor.getMainFile()).getColumns()) {
			if (answer[0].equals(column)) {
				return true;
			}
		}
		return false;
	}
	
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
	
	public String[] changeToStringArray(ArrayList<String> list) {
		String[] newList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			newList[i] = list.get(i);
		}
		return newList;
	}
	
	public String getName() {
		return "CsvDen";
	}
	
	public boolean isNormalize() {
		return false;
	}
}
