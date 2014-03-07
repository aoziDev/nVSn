package com.aozi.testclient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.EditText;

@EActivity(R.layout.signup)
public class SignUpActivity extends Activity{
	@ViewById
	EditText email;
	@ViewById
	EditText password;
	@ViewById
	EditText passwordConfirmation;
	
	@AfterInject
	void init(){
		setTitle("SignUp");
	}
	
	@Click
	void signup() {
		
	}
}
