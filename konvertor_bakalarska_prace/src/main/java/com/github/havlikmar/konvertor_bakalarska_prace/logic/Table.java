package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Table {
	private HashMap<String, Metadata> metadataList;
	private HashMap<String, Column> columnMap;
	private ArrayList<String> columnList;
	private String name;
	
	public Table (String name) {
		metadataList = new HashMap<String, Metadata>();
		columnMap = new HashMap<String, Column>();
		columnList = new ArrayList<String>();
		this.name = name;
	}
	
	public void addMetadata(String name, ArrayList<String> value) {
		Metadata metadata = new Metadata();
		metadata.setMetadata(name,value);
		metadataList.put(name, metadata);
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
	
	public boolean containMetadata(String nameOfMetadata) {
		return metadataList.containsKey(nameOfMetadata);
	}
	
	public Metadata getMetadata(String nameOfMetadata) {
		return metadataList.get(nameOfMetadata);
	}
	
	public Set<String> getMetadatas() {
		return metadataList.keySet();
	}
}
