package com.github.havlikmar.csvm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csvm.logic.CsvmDen;
import com.github.havlikmar.csvm.logic.CsvmNor;
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
    	CsvmDen den = new CsvmDen();
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	assertTrue(!den.loadFormat(convertor, "src\\main\\resources\\test\\testNeni.csv", ',', true));
    	CsvmNor nor = new CsvmNor();
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	assertTrue(!nor.loadFormat(convertor, "src\\main\\resources\\test\\testNeni.csv", ',', true));
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm1.csv", ',', true));
    }
}
