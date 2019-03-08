package com.github.havlikmar.konvertor_bakalarska_prace.logic;

/**
 * Výčtový typ FormatType představuje přípustné názvy určené pro rozlišení zda se do daného souboru exportuje nebo 
 * z něj importuje. Prvek TEST slouzi k otestovani pripadu v testech, kdy by se dotatecne pridal dalsi prvek.
 * 
 * @author     Martin Havlík
 * @version    25.2.2019
 */
public enum FormatType {
	IMPORT,EXPORT,TEST;
}
