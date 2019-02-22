package com.github.havlikmar.konvertor_bakalarska_prace.main;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

/**
 * Hello world!
 *
 */
public class App {	
	private static Convertor convertor;
	
    public static void main( String[] args ){
    	convertor = new Convertor();
    	TextProcesor procesor = new TextProcesor(convertor);
   }
}
