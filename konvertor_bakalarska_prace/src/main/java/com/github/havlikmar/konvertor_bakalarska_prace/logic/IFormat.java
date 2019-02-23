package com.github.havlikmar.konvertor_bakalarska_prace.logic;

public interface IFormat {
	boolean isNormalize();
	String getName();
	boolean loadFormat(Convertor convertor, String nameOfSource, char separator);
	boolean saveFormat(Convertor convertor);
}
