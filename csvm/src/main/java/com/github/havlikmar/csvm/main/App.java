package com.github.havlikmar.csvm.main;

/**
 * Třída App je zodpovědná za vypsání informací o modulu. Tato třída nespouští funkčnost modulu, jelikož modul je spouštěn z 
 * externí aplikace.
 * 
 * @author     Martin Havlík
 * @version    2.3.2019
 */
public class App 
{
	/**
     * Metoda pro spuštění modulu. Slouží pro vypsání informací, jak daný modul zprovoznit.
     * 
     * @param	args	parametry pro spuštění
     */
    public static void main( String[] args )
    {
        System.out.println( "Tento modul se spouští skrz externí hlavní aplikaci" );
        System.out.println( "Pro připojenín modulu nahrajte JAR soubor tohoto modulu do složky kde se nachází JAR soubor hlavní aplikace" );
        System.out.println( "Hlavní aplikaci spuste následujícím příkazem z příkazové řádky:" );
        System.out.println( "java -Dfile.encoding=UTF-8 -cp \"./*\" com.github.havlikmar.konvertor_bakalarska_prace.main.App" );
        System.out.println( "nebo použijte soubor execute.bat" );
    }
}
