package com.github.havlikmar.konvertor_bakalarska_prace;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.FormatType;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;

/**
 * Třída ConvertorTest testuje třídu Convertor
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class ConvertorTest {
	private Convertor convertor;
	
	/**
     * Metoda, která vytvoří přípravu testů
     *     
     */
    @Before
    public void setUp() {
    	convertor = new Convertor();
    }
    
    /**
     * Metoda testuje zda se správně nastaví konstruktor
     *     
     */
    @Test
	public void constructorTest() {
    	assertTrue(convertor.getTables().isEmpty());
    }
    
    /**
     * Metoda testuje operace s formaty
     *     
     */
    @Test
	public void formatTest() {
    	assertTrue(java.util.Objects.equals(convertor.getFormat(FormatType.IMPORT), null));
    	assertTrue(java.util.Objects.equals(convertor.getFormat(FormatType.EXPORT), null));
    	assertTrue(java.util.Objects.equals(convertor.getFormat(FormatType.TEST), null));
    	convertor.setStartFormat("a");
    	convertor.setEndFormat("b");
    	assertTrue(convertor.getStartFormat().equals("a"));
    	assertTrue(convertor.getEndFormat().equals("b"));
    }
    
    /**
     * Metoda testuje operace s tabulkami
     *     
     */
    @Test
	public void tableTest() {
    	assertTrue(convertor.getTables().isEmpty());
    	Table table = new Table("a");
    	convertor.addTable("a", table);
    	assertTrue(convertor.getTables().size() == 1);
    	assertTrue(!convertor.tableExist("b"));
    	assertTrue(convertor.tableExist("a"));
    	assertTrue(convertor.getTable("a").getName().equals("a"));
    	convertor.removeTable("a");
    	assertTrue(convertor.getTables().isEmpty());
    }
    
    /**
     * Metoda testuje operace se soubory
     *     
     */
    @Test
	public void fileTest() {
    	assertTrue(java.util.Objects.equals(convertor.getMainFile(), null));
    	convertor.setMainFile("test");
    	assertTrue(convertor.getMainFile().equals("test"));
    }
    
    /**
     * Metoda testuje operace s metadaty
     *     
     */
    @Test
	public void metaTest() {
    	assertTrue(convertor.getMetadictionaries().isEmpty());
    	String[] specific = new String[2];
    	specific[0] = "a";
    	String[] general = new String[2];
    	general[0] = "b";
    	String format = "test";
    	convertor.addMetadictionary(specific, general, format);
    	assertTrue(convertor.getMetadictionaries().size() == 1);
    	assertTrue(!convertor.containMetaDictionory("a"));
    	assertTrue(convertor.containMetaDictionory("test"));
    	assertTrue(convertor.getMetaDictionary("test").getGeneral("a").equals("b"));
    	assertTrue(convertor.getMetaDictionary("test").getSpecific("b").equals("a"));
    }
}
