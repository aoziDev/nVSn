package com.aozi.testclient.model;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.croquis.util.RestClient.OnRequestComplete;
import com.croquis.util.RestClient.RestError;

@EBean(scope=Scope.Singleton)
public class UserMe extends User{
	public UserMe(Context context) {
		super(context);
	}
	
	public void getUserMe(final OnRequestComplete<Void> complete) {
		getRestClient().get("userme", null, new OnRequestComplete<JSONObject>() {
			@Override
			public void onComplete(RestError error, JSONObject result) {
				if(error != null){
					Toast.makeText(getContext(), error.error, Toast.LENGTH_LONG).show();
					return;
				}
				
				complete.onComplete(null, null);
			}
		});
	}
}
