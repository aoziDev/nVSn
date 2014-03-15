package com.aozi.nvsn.util;

import org.json.JSONObject;

import android.content.Context;

import com.aozi.util.HttpManager;
import com.aozi.util.HttpManager.Method;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;

public class RestClient {
	private static RestClient INSTANCE = null;
	
	private HttpManager httpManager = null;

	@SuppressWarnings("unused")
	private Context context;
	private RestClient(Context context){
		this.context = context;
		httpManager = new HttpManager(context);
	}
	
	public static RestClient getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new RestClient(context.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public void request(Method method, String contentType, String path, JSONObject param, OnPostExecute onPostExecute) {
		httpManager.execute(method, contentType, path, param, onPostExecute);
	}
	
	public void checkSessionInfo(JSONObject param, OnPostExecute onPostExecute) {
		request(Method.GET, "application/json", "sessionInfo", param, onPostExecute);
	}

	public void login(JSONObjectBuilder param, OnPostExecute onPostExecute) {
		request(Method.POST, "application/json", "login", param, onPostExecute);
	}

	public void logout(OnPostExecute onPostExecute) {
		request(Method.GET, "application/json", "logout", null, onPostExecute);
	}
	
	public void signup(JSONObject param, OnPostExecute onPostExecute) {
		request(Method.POST, "application/json", "signup", param, onPostExecute);
	}
}
