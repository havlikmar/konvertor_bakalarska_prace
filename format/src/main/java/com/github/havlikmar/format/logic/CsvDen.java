package com.github.havlikmar.format.logic;

import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;
import com.opencsv.CSVReader;

public class CsvDen implements IFormat {
	
	public boolean loadFormat(Convertor convertor) {
		try {
			String directory = System.getProperty("user.dir") + "\\" + convertor.getMainFile();
	//		File file = new File(directory, convertor.getMainFile());
			CSVReader importReader = new CSVReader(new FileReader(directory));
			List<String[]> input = importReader.readAll();
	//		BufferedReader fileInput = new BufferedReader(new FileReader(file));
			String[] header = input.get(0);
			int headerSize = header.length;
			
			for(String[] row: input) {
				if (row.length != headerSize) {
					return false;
				}
			}
			
			String nameOfTable = convertor.getMainFile();
			convertor.addTable(nameOfTable);
			
			for (String columnName: header) {
				convertor.getTable(convertor.getMainFile()).addColumn(columnName);
			}
			
			for(String[] row: input) {
				if (!row.equals(header)) {
					for (int i = 0; i < headerSize; i++) {
						convertor.getTable(nameOfTable).getColumn(header[i]).addValue(row[i]);
					}
				}
			}
			
			Set<String> vypis = convertor.getTable(convertor.getMainFile()).getColumns().keySet();
			for (String i: vypis) {
				System.out.println(i);
			//	convertor.getTable(nameOfTable).getColumn(i).get;
			}
				
			
			
			
			
	//		for(String[] row: input) {
	//			for (String value: row) {
	//				System.out.println(value);
	//			}
	//		}
			importReader.close();
		}
		
		catch (FileNotFoundException e) { 	
			return false;
		}
		
		catch (IOException e) { 	
			return false;
		}
		
		System.out.println("Cssasasad");
		return true;
		
	}
	
	public void saveFormat() {
		
	}
	
	public String getName() {
		return "CsvDen";
	}
	
	public boolean isNormalize() {
		System.out.println("Csv_nesadasdasd");
		return false;
	}
}
