package com.github.havlikmar.konvertor_bakalarska_prace.main;
import com.github.havlikmar.konvertor_bakalarska_prace.logic.Convertor;

/**
 * Třída App představuje třídu z které se startuje aplikace
 * 
 * @author     Martin Havlík
 * @version    26.2.2019
 */
public class App {	
	private static Convertor convertor;
	private static TextProcesor procesor;
	
	/**
     * Ridici metoda, z které se spouští aplikace
     * 
     * @param	args	parametry pro spuštění
     */
    public static void main( String[] args ){
    	convertor = new Convertor();
    	procesor = new TextProcesor(convertor);
    	procesor.initialization();
   }
}
