package com.github.havlikmar.csv;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csv.logic.CsvDen;
import com.github.havlikmar.csv.logic.CsvNor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

/**
 * Třída ExportTest testuje export dat
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class ExportTest {

    /**
     * Metoda testuje zda fungují export dat
     *     
     */
    @Test
	public void saveTest() {
    	Convertor convertor = new Convertor();
    	CsvDen den = new CsvDen();
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	convertor.setMainFile("src\\main\\resources\\test\\test.csv");
    	convertor.setStartFormat("CsvDen");
    	convertor.setEndFormat("CsvDen");
    	assertTrue(den.saveFormat(convertor, true));
    	CsvNor nor = new CsvNor();
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\testDimenze.csv", ','));
    	convertor.setStartFormat("CsvNor");
    	convertor.setEndFormat("CsvNor");
    	assertTrue(nor.saveFormat(convertor, true));
    }
}
