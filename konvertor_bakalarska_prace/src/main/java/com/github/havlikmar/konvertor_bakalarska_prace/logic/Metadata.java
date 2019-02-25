package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;

public class Metadata {
	private ArrayList<String> values;
	private String name;
	
	public Metadata(String name, ArrayList<String> values) {
		this.values = values;
		this.name = name;
	}
}
