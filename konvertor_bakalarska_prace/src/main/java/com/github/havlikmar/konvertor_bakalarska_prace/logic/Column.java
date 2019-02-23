package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class Column {
	String name;
	private HashMap<String, Metadata> metadataList;
	private ArrayList<String> valueList;
	
	public Column (String name) {
		this.name = name;
		metadataList = new HashMap<String, Metadata>();
		valueList = new  ArrayList<String>();
	}
	
	public void addValue (String value) {
		valueList.add(value);
	}
	
	public ArrayList<String> getValues(){
		return valueList;
	}
}
