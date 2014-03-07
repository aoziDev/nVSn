package com.aozi.testclient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.EditText;

@EActivity(R.layout.login)
public class LoginActivity extends Activity {
	@ViewById
	EditText email;
	@ViewById
	EditText password;
	
	@AfterInject
	void init() {
		setTitle("LogIn");
	}
	
	@Click
	void login(){
		
	}
	
	@Click
	void signup() {
		SignUpActivity_.intent(LoginActivity.this).start();
	}
}
