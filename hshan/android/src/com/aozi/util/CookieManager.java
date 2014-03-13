package com.aozi.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CookieManager {
	public static final String KEY_SESSION_ID = "connect.sid";
	public static final String KEY_COOKIE_HEADER = "Set-Cookie";
	private static final String KEY_COOKIE_PREF = "cookie";
	private static CookieManager INSTANCE;

	private Context context;
	
	private CookieManager(Context context) {
		this.context = context;
	}
	
	public static CookieManager getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new CookieManager(context);
		}
		
		return INSTANCE;
	}
	
	public String getSavedSessionID() {
		SharedPreferences pref = this.context.getSharedPreferences(KEY_COOKIE_PREF, Activity.MODE_PRIVATE);
		return pref.getString(KEY_SESSION_ID, "");
	}
	
	public void saveSessionID(String sessionID) {
		SharedPreferences pref = this.context.getSharedPreferences(KEY_COOKIE_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(KEY_SESSION_ID, sessionID);
		editor.commit();
	}
	
	public void removeSessionID() {
		SharedPreferences pref = this.context.getSharedPreferences(KEY_COOKIE_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(KEY_SESSION_ID);
		editor.commit();
	}
}