package com.aozi.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectBuilder extends JSONObject{
	public static JSONObjectBuilder create(String json) {
		JSONObjectBuilder object = create();
		try {
			object = new JSONObjectBuilder(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
	public static JSONObjectBuilder create() {
		return new JSONObjectBuilder();
	}

	public JSONObjectBuilder() {
		super();
	}
	
	public JSONObjectBuilder(String json) throws JSONException {
		super(json);
	}
	
	public JSONObjectBuilder put(String name, int value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public JSONObjectBuilder put(String name, Object value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public int getInt(String name) {
		int result = 0;
		try {
			result = super.getInt(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getString(String name) {
		String result = "";
		try {
			result = super.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}