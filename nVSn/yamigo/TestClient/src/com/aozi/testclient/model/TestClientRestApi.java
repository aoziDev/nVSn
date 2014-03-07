package com.aozi.testclient.model;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.json.JSONObject;

import com.croquis.util.JSONObjectBuilder;
import com.croquis.util.RestClient;
import com.croquis.util.RestClient.OnRequestComplete;
import com.croquis.util.RestClient.RestError;

@EBean(scope=Scope.Singleton)
public class TestClientRestApi{
	public static final String SERVER_URL = "http://127.0.0.1/api/1/";
	@Bean
	RestClient mRestClient;
	
	@AfterInject
	void init() {
		mRestClient.setServerUrl(SERVER_URL);
	}
	
	public void login(String email, String password, final OnRequestComplete<Void> complete) {
		JSONObject parameters = new JSONObjectBuilder().put("email", email).put("password", password);
		mRestClient.post("login", parameters, new OnRequestComplete<JSONObject>(){
			@Override
			public void onComplete(RestError error, JSONObject json) {
				complete.onComplete(error, null);
			}	
		});
	}
	
	public void signup(String email, String password, final OnRequestComplete<Void> complete) {
		JSONObject parameters = new JSONObjectBuilder().put("email", email).put("password", password);
		mRestClient.post("users", parameters, new OnRequestComplete<JSONObject>(){
			@Override
			public void onComplete(RestError error, JSONObject json) {
				complete.onComplete(error, null);
			}
		});
	}
	
	public void logout(final OnRequestComplete<Void> complete) {
		mRestClient.get("logout", null, new OnRequestComplete<JSONObject>() {
			@Override
			public void onComplete(RestError error, JSONObject json) {
				complete.onComplete(error, null);
			}
		});
	}
}