package com.github.havlikmar.konvertor_bakalarska_prace;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.havlikmar.konvertor_bakalarska_prace.logic.Table;

/**
 * Třída TableTest testuje třídu Table
 * 
 * @author     Martin Havlík
 * @version    7.3.2019
 */
public class TableTest {
	private Table table;
	
	/**
     * Metoda, která vytvoří přípravu testů
     *     
     */
    @Before
    public void setUp() {
    	table = new Table("testTable");
    }
    
    /**
     * Metoda testuje operace se slupci
     *     
     */
    @Test
	public void columnTest() {
    	assertTrue(table.getColumns().isEmpty());
    	table.addColumn("test");
    	assertTrue(table.getColumns().size() == 1);
    	assertTrue(table.columnExist("test"));
    	assertTrue(!table.columnExist("test1"));
    	assertTrue(table.getColumn("test").getName().equals("test"));
    	table.removeColumn("test");
    	assertTrue(table.getColumns().isEmpty());
    	assertTrue(!table.columnExist("test"));
    }
    
    /**
     * Metoda testuje operace se slupci
     *     
     */
    @Test
	public void metaTest() {
    	assertTrue(table.getMetadatas().isEmpty());
    	ArrayList<String> meta = new ArrayList<String>();
    	meta.add("a");
    	table.addMetadata("test", meta);
    	assertTrue(table.getMetadatas().size() == 1);
    	assertTrue(table.containMetadata("test"));
    	assertTrue(!table.containMetadata("test1"));
    	assertTrue(table.getMetadata("test").getName().equals("test"));;
    }
}
