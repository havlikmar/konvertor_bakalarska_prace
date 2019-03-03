package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;

/**
 * Třída Matadata je třída představující určitá metadata
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class Metadata {
	private ArrayList<String> values;
	private String name;
	
	/**
     * Konstruktor pro vytvoření instance třídy Metadata
     * 
     */
	public Metadata() {
		values = new ArrayList<String>();
	}
	
	/**
     * Metoda pro nastavení metadat
     * 
     * @param	name	název metadat
     * @param	values	hodnoty metadat
     */
	public void setMetadata(String name, ArrayList<String> values) {
		this.values = values;
		this.name = name;
	}
	
	/**
     * Metoda pro získání hodnot metadat
     * 
     * @return	hledané hodnoty metadat
     */
	public ArrayList<String> getMetadata() {
		return values;
	}
	
	/**
     * Metoda pro získání názvu metadat
     * 
     * @return	název metadat
     */
	public String getName() {
		return name;
	}
}
