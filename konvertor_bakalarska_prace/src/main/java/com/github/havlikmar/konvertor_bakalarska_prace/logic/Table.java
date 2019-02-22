package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
	private HashMap<String, Metadata> metadataList;
	private ArrayList<String> valueList;
	private HashMap<String, Column> columnList;
	private String name;
	
	public Table (String name) {
		metadataList = new HashMap<String, Metadata>();
		columnList = new HashMap<String, Column>();
		valueList = new ArrayList<String>();
		this.name = name;
	}
	
	public void addColumn(String nameColumn) {
		Column column = new Column(nameColumn);
		columnList.put(nameColumn, column);
	}
	
	public HashMap<String, Column> getColumns(){
		return columnList;
	}
	
	public Column getColumn(String nameOfColumn) {
		return (columnList.get(nameOfColumn));
	}
	
	
}
