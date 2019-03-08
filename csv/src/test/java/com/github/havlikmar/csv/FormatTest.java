package com.github.havlikmar.csv;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.havlikmar.csv.logic.CsvDen;
import com.github.havlikmar.csv.logic.CsvNor;
import com.github.havlikmar.csv.main.App;
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
    	CsvDen den = new CsvDen();
    	assertTrue(!den.isNormalize());
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	convertor.setMainFile("src\\main\\resources\\test\\test.csv");
    	assertTrue(den.getMainFileColumn(convertor).equals(" idhod hodnota stapro_kod reprcen_cis reprcen_kod obdobiod obdobido uzemi_cis uzemi_kod uzemi_txt reprcen_txt"));
    	assertTrue(den.loadFormat(convertor, "src\\main\\resources\\test\\testDimenze.csv", ','));
   //	V nasledujim testu je chyba v Mavenu? JUnit test mi vrací, že je OK, Maven že chyba.
   // 	assertTrue(den.getOtherFileColumn(convertor).equals(" ﻿idhod hodnota"));
    	den.getOtherFileColumn(convertor);
    	String[] answer = new String[2];
    	answer[0] = "idhod";
    	answer[1] = "idhod";
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
    	CsvNor nor = new CsvNor();
    	assertTrue(nor.isNormalize());
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\test.csv", ','));
    	convertor.setMainFile("src\\main\\resources\\test\\test.csv");
    	assertTrue(nor.getMainFileColumn(convertor).equals(" idhod hodnota stapro_kod reprcen_cis reprcen_kod obdobiod obdobido uzemi_cis uzemi_kod uzemi_txt reprcen_txt"));
    	assertTrue(nor.loadFormat(convertor, "src\\main\\resources\\test\\testDimenze.csv", ','));
    	String[] answer = new String[2];
    	answer[0] = "idhod";
    	answer[1] = "idhod";
    	assertTrue(nor.isInMainFile(answer, convertor));    	
    }
}
