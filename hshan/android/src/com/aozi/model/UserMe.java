package com.aozi.model;

import android.content.Context;
import android.widget.Toast;

import com.aozi.nvsn.util.RestClient;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.HttpManager.Result;
import com.aozi.util.JSONObjectBuilder;

public class UserMe {
	private static UserMe INSTANCE = null;
	
	private String user_id = "";
	private boolean isLogined = false;

	private Context context = null;
	private RestClient client = null;
	
	private UserMe(Context context) {
		this.context = context.getApplicationContext();
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
			public void onError(Result result) {
				Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(Result result) {
				JSONObjectBuilder jsonObject = result.getJsonObject();
				if (jsonObject.has("user_id")) {
					setInfo(jsonObject);
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
