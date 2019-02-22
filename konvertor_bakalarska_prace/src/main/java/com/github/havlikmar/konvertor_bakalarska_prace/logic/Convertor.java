package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.HashMap;
import java.util.ServiceLoader;

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
	private String otherFile;
	private HashMap<String, Table> tableList;
	
	/**
     * Konstruktor pro vytvoření instance convertoru
     */
	public Convertor(){
		formatList = new HashMap<String, IFormat>();
		tableList = new HashMap<String, Table>();
		loadFormat();
	};
	
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
	
	public void addTable (String nameOfTable) {
		Table newtable = new Table(nameOfTable);
		tableList.put(nameOfTable, newtable);
	}
	
	public Table getTable(String nameOfTable) {
		return tableList.get(nameOfTable);
	}
	
	public void setMainFile (String mainFile) {
		this.mainFile = mainFile;
	}
	
	public void addOtherFile (String otherFile) {
		this.otherFile = otherFile;
	}
	
	public String getMainFile () {
		return mainFile;
	}
	
	public String getOtherFile () {
		return otherFile;
	}
	
	/**
     * Metoda pro nastavení cílového formátu
     * 
     * @param	type	rozlišení zda jde o zdrojový nebo cílový formát
     * @return	IFormat Daný zdrojový nebo cílový formát
     */
	public IFormat getFormat (int type) {
		switch (type){
			case 1: return formatList.get(startFormat);
			case 2: return formatList.get(endFormat);
		}
		return null;
	}
}
