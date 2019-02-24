package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
	private HashMap<String, Metadata> metadataList;
	private ArrayList<String> valueList;
	private HashMap<String, Column> columnMap;
	private ArrayList<String> columnList;
	private String name;
	
	public Table (String name) {
		metadataList = new HashMap<String, Metadata>();
		columnMap = new HashMap<String, Column>();
		columnList = new ArrayList<String>();
		this.name = name;
	}
	
	public void addColumn(String nameColumn) {
		Column column = new Column(nameColumn);
		columnMap.put(nameColumn, column);
		columnList.add(nameColumn);
	}
	
	public ArrayList<String> getColumns(){
		return columnList;
	}
	
	public Column getColumn(String nameOfColumn) {
		return (columnMap.get(nameOfColumn));
	}
	
	public void removeColumn(String nameOfColumn) {
		this.getColumns().remove(nameOfColumn);
	}
	
	public boolean columnExist(String nameOfColumn) {
		return columnMap.containsKey(nameOfColumn);
	}
}
