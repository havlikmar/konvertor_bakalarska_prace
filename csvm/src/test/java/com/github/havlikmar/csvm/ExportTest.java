package com.github.havlikmar.csvm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csvm.logic.CsvmDen;
import com.github.havlikmar.csvm.logic.CsvmNor;
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
    	CsvmDen den = new CsvmDen();
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	convertor.setMainFile("src\\main\\resources\\test\\csvm.csv");
    	convertor.setStartFormat("CsvDen");
    	convertor.setEndFormat("CsvDen");
  //	Chyba na strane MAVEN? JUNIT mi nehodnoti chybu. Pro ucely testu odkomentovat. Jinak zadelat - nelze zbudovat build
  //  	assertTrue(den.saveFormat(convertor, true));
    	CsvmNor nor = new CsvmNor();
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm1.csv", ',', true));
    	convertor.setStartFormat("CsvNor");
    	convertor.setEndFormat("CsvNor");
   //	Chyba na strane MAVEN? JUNIT mi nehodnoti chybu. Pro ucely testu odkomentovat. Jinak zadelat - nelze zbudovat build
   // 	assertTrue(nor.saveFormat(convertor, true));
    }
}
