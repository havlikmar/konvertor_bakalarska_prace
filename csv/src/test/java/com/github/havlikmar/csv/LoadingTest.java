package com.github.havlikmar.csv;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csv.logic.CsvDen;
import com.github.havlikmar.csv.logic.CsvNor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

/**
 * Třída LoadingTest testuje nahrávání dat
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class LoadingTest {
	
    /**
     * Metoda testuje zda fungují nacitani
     *     
     */
    @Test
	public void importTest() {
    	Convertor convertor = new Convertor();
    	CsvDen den = new CsvDen();
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	assertTrue(!den.loadFormat(convertor, "src\\main\\resources\\test\\testNeni.csv", ','));
    	assertTrue(!den.loadFormat(convertor, "src\\main\\resources\\test\\testChyba.csv", ','));
    	CsvNor nor = new CsvNor();
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	assertTrue(!nor.loadFormat(convertor, "src\\main\\resources\\test\\testNeni.csv", ','));
    	assertTrue(!nor.loadFormat(convertor, "src\\main\\resources\\test\\testChyba.csv", ','));
    }
}
