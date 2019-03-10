package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Třída Column je třída představující daný sloupec tabulky
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class Column {
	private String name;
	private HashMap<String, Metadata> metadataList;
	private ArrayList<String> valueList;
	
	/**
     * Konstruktor pro vytvoření instance třídy Column
     * 
     * @param	name	název sloupec
     */
	public Column (String name) {
		this.name = name;
		metadataList = new HashMap<String, Metadata>();
		valueList = new  ArrayList<String>();
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
     * Metoda pro přidání hodnoty ve sloupci
     * 
     * @param	value	přidaná hodnota
     */
	public void addValue (String value) {
		valueList.add(value);
	}
	
	/**
     * Metoda pro získání hodnot ze sloupce
     * 
     * @return	hodnoty ve sloupci
     */
	public ArrayList<String> getValues(){
		return valueList;
	}
	
	/**
     * Metoda pro získání názvu sloupce
     * 
     * @return	název sloupce
     */
	public String getName() {
		return name;
	}
	
	/**
     * Metoda pro zjištění zda ve sloupci jsou daná metadata
     * 
     * @param	nameOfMetadata	název metadat
     * @return	informace zda obsahuje metadata
     */
	public boolean containMetadata(String nameOfMetadata) {
		return metadataList.containsKey(nameOfMetadata);
	}
	
	/**
     * Metoda pro získání metadat
     * 
     * @param	nameOfMetadata	název metadat
     * @return	hledaná metadata
     */
	public Metadata getMetadata(String nameOfMetadata) {
		return metadataList.get(nameOfMetadata);
	}
	
	/**
     * Metoda pro záskání názvu metadat
     * 
     * @return	názvy všech metadat vztahující se na daný sloupec
     */
	public Set<String> getMetadatas() {
		return metadataList.keySet();
	}
	
	/**
     * Metoda pro nahrazeni hodnoty
     * 
     * @param 	name 	hodnota
     * @param	index	umisteni hodnoty
     */
	public void switchValue(String name, int index) {
		valueList.set(index, name);
	}
}
