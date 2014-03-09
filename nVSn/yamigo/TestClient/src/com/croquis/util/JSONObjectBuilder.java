package com.croquis.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectBuilder extends JSONObject {

	public static JSONObject build(String name, boolean value) {
		return new JSONObjectBuilder().put(name, value);
	}

	public static JSONObject build(String name, int value) {
		return new JSONObjectBuilder().put(name, value);
	}

	public static JSONObject build(String name, double value) {
		return new JSONObjectBuilder().put(name, value);
	}

	public static JSONObject build(String name, long value) {
		return new JSONObjectBuilder().put(name, value);
	}

	public static JSONObject build(String name, Object value) {
		return new JSONObjectBuilder().put(name, value);
	}

	@Override
	public JSONObjectBuilder put(String name, boolean value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public JSONObjectBuilder put(String name, double value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public JSONObjectBuilder put(String name, int value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public JSONObjectBuilder put(String name, long value) {
		try {
			super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public JSONObjectBuilder put(String name, Object value) {
		try {
			super.put(name, value != null ? value : JSONObject.NULL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}
}
