package com.aozi.testclient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.aozi.testclient.util.TestClientRestApi;
import com.croquis.util.RestClient.OnRequestComplete;
import com.croquis.util.RestClient.RestError;

@EActivity(R.layout.signup)
public class SignUpActivity extends Activity{
	@ViewById
	EditText email;
	@ViewById
	EditText password;
	@ViewById
	EditText passwordConfirmation;
	
	@Bean
	TestClientRestApi mTestClientRestApi; 
	
	@AfterInject
	void init(){
		setTitle("SignUp");
	}
	
	@Click
	void signup() {
		if(TextUtils.isEmpty(email.getText().toString())) {
			Toast.makeText(this, "email is empty", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(TextUtils.isEmpty(password.getText().toString())) {
			Toast.makeText(this, "password is empty", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(password.getText().toString().equals(passwordConfirmation.getText().toString())) {
			Toast.makeText(this, "incorrect password confirmation", Toast.LENGTH_LONG).show();
			return;
		}
		
		final ProgressDialog progress = ProgressDialog.show(SignUpActivity.this, "SignUp", "wait for a while");
		mTestClientRestApi.signup(email.getText().toString(), password.getText().toString(), new OnRequestComplete<Void>() {
			@Override
			public void onComplete(RestError error, Void result) {
				progress.dismiss();
				if(error != null) {
					Toast.makeText(SignUpActivity.this, error.error, Toast.LENGTH_LONG).show();
					return;
				}
			}
		});
	}
}
