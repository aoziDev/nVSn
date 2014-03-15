package com.aozi.model;

import android.content.Context;

import com.aozi.nvsn.util.RestClient;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;

public class UserMe {
	private static UserMe INSTANCE = null;
	
	private String user_id = "";
	private boolean isLogined = false;

	private RestClient client = null;
	
	private UserMe(Context context) {
		this.client = RestClient.getInstance(context);
	}
	
	public static UserMe getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new UserMe(context);
		}
		return INSTANCE;
	}
	
	public void setInfo(JSONObjectBuilder result) {
		user_id = result.getString("user_id");
		isLogined = true;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public boolean isLogined() {
		return isLogined;
	}
	
	public void checkLogin(OnPostExecute onPostExecute) {
		client.checkSessionInfo(null, onPostExecute);
	}
	
	public void login(String email, String password, OnPostExecute onPostExecute) {
		JSONObjectBuilder param = JSONObjectBuilder.create()
				.put("email", email)
				.put("password", password);
		
		client.login(param, onPostExecute);
	}

	public void logout(OnPostExecute onPostExecute) {
		client.logout(onPostExecute);
		
		user_id = "";
		isLogined = false;
	}
}
