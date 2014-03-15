package com.aozi.model;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.aozi.nvsn.util.RestClient;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;

public class UserMe {
	private static UserMe INSTANCE = null;
	
	private String user_id = "";
	private boolean isLogined = false;

	@SuppressWarnings("unused")
	private Context context = null;
	private RestClient client = null;
	
	private UserMe(Context context) {
		this.context = context;
		this.client = RestClient.getInstance(context);
	}
	
	public static UserMe getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new UserMe(context.getApplicationContext());
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
	
	public void checkLogin() {
		client.checkSessionInfo(null, new OnPostExecute() {
			@Override
			public void execute(HttpResponse response, JSONObjectBuilder result) {
				if (result.has("user_id")) {
					setInfo(result);
				}
			}
		});
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
