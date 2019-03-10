package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.HashMap;

/**
 * Třída MetaDictionary je třída představující slovník metadat
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class MetaDictionary {
	private HashMap <String, String> toGeneral;
	private HashMap <String, String> fromGeneral;
	
	/**
     * Konstruktor pro vytvoření instance třídy MetaDictionary
     * 
     */
	public MetaDictionary() {
		toGeneral = new HashMap <String, String>();
		fromGeneral = new HashMap <String, String>();
	}
	
	/**
     * Metoda pro nastavení slovníku pro převod do všeobecné podoby
     * 
     * @param	specific	Názvy metadat v daném formátu
     * @param	general		Název medadat ve všeobecném tvaru
     */
	public void setToGeneral(String[] specific, String[] general) {
		for (int i = 0; i < specific.length; i++) {
			toGeneral.put(specific[i], general[i]);
		}
	}
	
	/**
     * Metoda pro nastavení slovníku pro převod do specifické podoby
     * 
     * @param	specific	Názvy metadat v daném formátu
     * @param	general		Název medadat ve všeobecném tvaru
     */
	public void setFromGeneral(String[] specific, String[] general) {
		for (int i = 0; i < specific.length; i++) {
			fromGeneral.put(general[i], specific[i]);
		}
	}
	
	/**
     * Metoda pro získání specifických hodnot ze slovníku metadat
     * 
     * @param	general	všeobecný název
     * @return	specifické hodnoty metadat
     */
	public String getSpecific(String general) {
		return fromGeneral.get(general);
	}

	/**
     * Metoda pro získání všeobecných hodnot ze slovníku metadat
     * 
     * @param	specific	specifický název
     * @return	všeobecné hodnoty metadat
     */
	public String getGeneral(String specific) {
		return toGeneral.get(specific);
	}
}
