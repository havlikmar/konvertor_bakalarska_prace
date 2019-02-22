package com.github.havlikmar.format.logic;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.IFormat;

public class CsvNor implements IFormat{
	
	public boolean loadFormat(Convertor convertor) {
		System.out.println("Csv_nesadasdasd");
		return false;
	}
	
	public void saveFormat() {
		
	}
	
	public String getName() {
		return "CsvNor";
	}
	
	public boolean isNormalize() {
		return true;
	}
}
