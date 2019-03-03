package com.github.havlikmar.konvertor_bakalarska_prace.logic;

/**
 * Interface IFormat představuje rozhraní s metodami, které musí mít třídy pro implementaci formátů implementované
 * 
 * @author     Martin Havlík
 * @version    24.2.2019
 */
public interface IFormat {
	
	/**
     * Metoda pro zjištění zda je formát normalizovaný nebo ne
     * 
     * @return informace zda je formát normalizovaný nebo ne
     */
	boolean isNormalize();
	
	/**
     * Metoda pro zjištění názvu formátu
     * 
     * @return Název formátu
     */
	String getName();
	
	/**
     * Metoda pro uložení daného souboru do vnitřní paměti
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @param	nameOfSource	Název souboru, který chceme načíst do vnitřní paměti
     * @param	separator	použitý separator, pro rozdělení řádku CSV na objekty
     * @return informace zda se soubor načetl nebo ne
     */
	boolean loadFormat(Convertor convertor, String nameOfSource, char separator);
	
	/**
     * Ridici metoda pro zpracování exportu souboru
     * 
     * @param	convertor	Odkaz na třídu Convertor,která je zodpovědná za propojení s vnitřní datovou reprezentací
     * @return informace zda se soubor exportoval nebo ne
     */
	boolean saveFormat(Convertor convertor);
}
