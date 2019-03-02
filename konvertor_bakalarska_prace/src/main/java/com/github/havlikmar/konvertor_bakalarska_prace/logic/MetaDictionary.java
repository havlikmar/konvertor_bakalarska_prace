package com.github.havlikmar.konvertor_bakalarska_prace.logic;

import java.util.HashMap;

public class MetaDictionary {
	private HashMap <String, String> toGeneral;
	private HashMap <String, String> fromGeneral;
	
	public MetaDictionary() {
		toGeneral = new HashMap <String, String>();
		fromGeneral = new HashMap <String, String>();
	}
	
	public void setToGeneral(String[] specific, String[] general) {
		for (int i = 0; i < specific.length; i++) {
			toGeneral.put(specific[i], general[i]);
		}
	}
	
	public void setFromGeneral(String[] specific, String[] general) {
		for (int i = 0; i < specific.length; i++) {
			fromGeneral.put(general[i], specific[i]);
		}
	}
	
	public String getSpecific(String general) {
		return fromGeneral.get(general);
	}

	public String getGeneral(String specific) {
		return toGeneral.get(specific);
	}
}
