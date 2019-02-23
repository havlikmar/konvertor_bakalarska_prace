package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.HashMap;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Třída Convertor je v návrhovém vzoru Strategie zodpovědný za přenos dat mezi logikou (kde jsou data uloženy)
 * a jednotlivými strategiemi (třídami implementující formáty).
 * 
 * @author     Martin Havlík
 * @version    20.2.2019
 */
public class Convertor {
	private HashMap<String, IFormat> formatList;
	private String startFormat;
	private String endFormat;
	private String mainFile;
	private HashMap<String, Table> tableList;
	
	/**
     * Konstruktor pro vytvoření instance convertoru
     */
	public Convertor(){
		formatList = new HashMap<String, IFormat>();
		tableList = new HashMap<String, Table>();
		loadFormat();
	};
	
	public void setMainFile (String nameOfFile) {
		mainFile = nameOfFile;
	}
	
	public String getMainFile () {
		return mainFile;
	}
	
	/**
     * Metoda pro načtení formátů. Slouží k iniciaci tříd formátů z externích modulů mimo tento baliček.
     * 
     */
	public void loadFormat() {
		ServiceLoader<IFormat> loader;
		loader = ServiceLoader.load(IFormat.class);
	       for (IFormat format : loader) {
	    	   formatList.put(format.getName(), format);
	       }
	}
	
	/**
     * Metoda pro získání formátů
     * 
     * @return   HashMap<String, IFormat> reprezentující formáty
     */
	public HashMap<String, IFormat> getFormats(){
		return formatList;
	}
	
	/**
     * Metoda pro nastavení zdrojového formátu
     * 
     * @param	startFormat	zdrojový formát
     */
	public void setStartFormat (String startFormat) {
		this.startFormat = startFormat;
	}

	/**
     * Metoda pro nastavení cílového formátu
     * 
     * @param	endFormat	cílový formát
     */
	public void setEndFormat (String endFormat) {
		this.endFormat = endFormat;
	}
	
	public void addTable(String nameOfTable, Table newTable) {
		tableList.put(nameOfTable, newTable);
	}
	
	public Table getTable(String nameOfTable) {
		return tableList.get(nameOfTable);
	}
	
	public void removeTable(String nameOfTable) {
		this.tableList.remove(nameOfTable);
	}
	
	public Set<String> getTables(){
		return tableList.keySet();
	}
	
	/**
     * Metoda pro nastavení cílového formátu
     * 
     * @param	type	rozlišení zda jde o zdrojový nebo cílový formát
     * @return	IFormat Daný zdrojový nebo cílový formát
     */
	public IFormat getFormat (FormatType type) {
		switch (type){
			case IMPORT: return formatList.get(startFormat);
			case EXPORT: return formatList.get(endFormat);
		}
		return null;
	}
}
