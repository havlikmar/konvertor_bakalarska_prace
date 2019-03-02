package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.ArrayList;

public class Metadata {
	private ArrayList<String> values;
	private String name;
	
	public Metadata() {
		values = new ArrayList<String>();
	}
	
	public void setMetadata(String name, ArrayList<String> values) {
		this.values = values;
		this.name = name;
	}
	
	public ArrayList<String> getMetadata() {
		return values;
	}
	
	public String getName() {
		return name;
	}
}
