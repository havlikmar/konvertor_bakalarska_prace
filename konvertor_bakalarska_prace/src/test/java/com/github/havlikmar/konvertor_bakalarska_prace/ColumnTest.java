package com.github.havlikmar.konvertor_bakalarska_prace;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Column;

/**
 * Třída ColumnTest testuje třídu Column
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class ColumnTest {
	private Column column;

	/**
     * Metoda, která vytvoří přípravu testů
     *     
     */
    @Before
    public void setUp() {
    column = new Column("testColumn");
    }
    
    /**
     * Metoda testuje zda se správně nastaví konstruktor
     *     
     */
    @Test
	public void constructorTest() {
    	assertTrue(column.getName().equals("testColumn"));
    	assertTrue(column.getValues().size() == 0);
    	assertTrue(column.getMetadatas().size() == 0);
    }
    
    /**
     * Metoda testuje zda fungují operace s hodnotami
     *     
     */
    @Test
	public void valueTest() {
    	assertTrue(column.getValues().size() == 0);
    	column.addValue("test");
    	assertTrue(column.getValues().size() == 1);
    	assertTrue(column.getValues().get(0).equals("test"));
    	column.addValue("test_new");
    	assertTrue(column.getValues().size() == 2);
    	assertTrue(column.getValues().get(0).equals("test"));
    	column.switchValue("test_switch", 0);
    	assertTrue(column.getValues().size() == 2);
    	assertTrue(column.getValues().get(0).equals("test_switch"));
    }
    
    /**
     * Metoda testuje zda funguje operace s metadaty
     *     
     */
    @Test
	public void metadataTest() {
    	assertTrue(column.getMetadatas().size() == 0);
    	ArrayList<String> metalist = new ArrayList<String>(); 
    	metalist.add("meta");
    	column.addMetadata("test", metalist);
    	assertTrue(column.getMetadatas().size() == 1);
    	assertTrue(column.getMetadatas().contains("test"));
    	assertTrue(column.containMetadata("test"));
    	column.addMetadata("test_new",metalist);
    	assertTrue(column.getMetadata("test").getName().equals("test"));
    	assertTrue(column.getMetadata("test").getMetadata().size() == 1);
    	assertTrue(column.getMetadata("test").getMetadata().get(0).equals("meta"));
    }
}
