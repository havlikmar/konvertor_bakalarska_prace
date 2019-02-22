package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.io.FileNotFoundException;

public interface IFormat {
	boolean isNormalize();
	String getName();
	boolean loadFormat(Convertor convertor) throws FileNotFoundException;
	void saveFormat();
}
