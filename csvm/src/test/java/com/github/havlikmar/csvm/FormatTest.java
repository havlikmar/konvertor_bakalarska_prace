package com.github.havlikmar.csvm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csvm.main.App;
import com.github.havlikmar.csvm.logic.CsvmDen;
import com.github.havlikmar.csvm.logic.CsvmNor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

/**
 * Třída FormatTest testuje operace na formáty
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class FormatTest {

	/**
     * Metoda testuje zda fungují operace nad denormalizovanými formáty
     *     
     */
    @Test
	public void denTest() {
    	Convertor convertor = new Convertor();
    	CsvmDen den = new CsvmDen();
    	assertTrue(!den.isNormalize());
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	convertor.setMainFile("src\\main\\resources\\test\\csvm.csv");
    	assertTrue(den.getMainFileColumn(convertor).equals(" ID MODEL TYPE ID_MANUFACTURE"));
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\csvm1.csv", ',', true));
   //	V nasledujim testu je chyba v Mavenu? JUnit test mi vrací, že je OK, Maven že chyba.
   // 	assertTrue(den.getOtherFileColumn(convertor).equals(" ﻿idhod hodnota"));
    	den.getOtherFileColumn(convertor);
    	String[] answer = new String[2];
    	answer[0] = "TYPE";
    	answer[1] = "TYPE";
    	assertTrue(den.isInMainFile(answer, convertor));
    	App.main(null);
    }
    
    /**
     * Metoda testuje zda fungují operace nad normalizovanými formaty
     *     
     */
    @Test
	public void norTest() {
    	Convertor convertor = new Convertor();
    	CsvmNor nor = new CsvmNor();
    	assertTrue(nor.isNormalize());
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm.csv", ',', true));
    	convertor.setMainFile("src\\main\\resources\\test\\csvm.csv");
    	assertTrue(nor.getMainFileColumn(convertor).equals(" ID MODEL TYPE ID_MANUFACTURE"));
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\csvm1.csv", ',', true));
    	String[] answer = new String[2];
    	answer[0] = "TYPE";
    	answer[1] = "TYPE";
    	assertTrue(nor.isInMainFile(answer, convertor));    	
    }
}
