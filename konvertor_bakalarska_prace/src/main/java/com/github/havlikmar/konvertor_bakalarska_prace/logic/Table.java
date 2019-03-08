package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Třída Table je třída představující danou tabulku
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class Table {
	private HashMap<String, Metadata> metadataList;
	private HashMap<String, Column> columnMap;
	private ArrayList<String> columnList;
	private String name;
	
	/**
     * Konstruktor pro vytvoření instance třídy Table
     * 
     * @param	name	název tabulky
     */
	public Table (String name) {
		metadataList = new HashMap<String, Metadata>();
		columnMap = new HashMap<String, Column>();
		columnList = new ArrayList<String>();
		this.name = name;
	}
	
	/**
     * Metoda pro přidání metadat
     * 
     * @param	name	název metadat
     * @param	value	hodnota metadat
     */
	public void addMetadata(String name, ArrayList<String> value) {
		Metadata metadata = new Metadata();
		metadata.setMetadata(name,value);
		metadataList.put(name, metadata);
	}
	
	/**
     * Metoda pro přidání sloupců tabulky
     * 
     * @param	nameColumn	název sloupce
     */
	public void addColumn(String nameColumn) {
		Column column = new Column(nameColumn);
		columnMap.put(nameColumn, column);
		columnList.add(nameColumn);
	}
	
	/**
     * Metoda pro získání názvů sloupců tabulky
     * 
     * @return	hodnoty ve sloupci
     */
	public ArrayList<String> getColumns(){
		return columnList;
	}
	
	/**
     * Metoda pro získání sloupce tabulky
     * 
     * @param	nameOfColumn	název sloupce
     * @return	Daný sloupec tabulky
     */
	public Column getColumn(String nameOfColumn) {
		return (columnMap.get(nameOfColumn));
	}
	
	/**
     * Metoda pro odstranění sloupce z tabulky
     * 
     * @param	nameOfColumn	název sloupce
     */
	public void removeColumn(String nameOfColumn) {
		this.getColumns().remove(nameOfColumn);
		columnMap.remove(nameOfColumn);
	}
	
	/**
     * Metoda pro získání informace zda v dané tabulce existuje sloupec
     * 
     * @param	nameOfColumn	název sloupce
     * @return	Informace zda daný sloupec existuje
     */
	public boolean columnExist(String nameOfColumn) {
		return columnMap.containsKey(nameOfColumn);
	}
	
	/**
     * Metoda pro získání informace zda daná tabulka obsahuje daná metadata
     * 
     * @param	nameOfMetadata	název metadat
     * @return	Informace zda daný sloupec obsahuje metadata
     */
	public boolean containMetadata(String nameOfMetadata) {
		return metadataList.containsKey(nameOfMetadata);
	}
	
	/**
     * Metoda pro získání daných metadat z tabulky
     * 
     * @param	nameOfMetadata	název metadat
     * @return	Daná metadata z tabulky
     */
	public Metadata getMetadata(String nameOfMetadata) {
		return metadataList.get(nameOfMetadata);
	}
	
	/**
     * Metoda pro získání názvu metadat z tabulky
     * 
     * @return	Názvy metadat z tabulky
     */
	public Set<String> getMetadatas() {
		return metadataList.keySet();
	}
	
	/**
     * Metoda pro získání názvu tabulky
     * 
     * @return	Název tabulky
     */
	public String getName() {
		return name;
	}
}
