package com.aozi.testclient.model;

import org.json.JSONObject;

import android.content.Context;

import com.croquis.mvc.CRModel;
import com.croquis.util.JSONHelper;

public class User extends CRModel{
	private String mId;
	private String mEmail;
	private String mFullName;
	
	public User(Context context) {
		super(context);
	}
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getId() {
		return mId;
	}
	
	public void setEmail(String email) {
		mEmail = email;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public String getFullName() {
		return mFullName;
	}

	public void setFullName(String fullName) {
		this.mFullName = fullName;
	}

	@Override
	public void set(JSONObject json) {
		if(json.has("id")) {
			setId(JSONHelper.getString(json, "id"));
		}
		
		if(json.has("email")) {
			setEmail(JSONHelper.getString(json, "email"));
		}
		
		if(json.has("full_name")) {
			setFullName(JSONHelper.getString(json, "full_name"));
		}
	}
}
