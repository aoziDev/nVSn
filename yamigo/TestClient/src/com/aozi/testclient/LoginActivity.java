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

@EActivity(R.layout.login)
public class LoginActivity extends Activity {
	@ViewById
	EditText email;
	@ViewById
	EditText password;
	
	@Bean
	TestClientRestApi mTestClientRestApi;
	
	@AfterInject
	void init() {
		setTitle("LogIn");
	}
	
	@Click
	void login(){
		if(TextUtils.isEmpty(email.getText().toString())) {
			Toast.makeText(this, "email is empty", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(TextUtils.isEmpty(password.getText().toString())) {
			Toast.makeText(this, "password is empty", Toast.LENGTH_LONG).show();
			return;
		}
		
		final ProgressDialog progress = ProgressDialog.show(LoginActivity.this, "Login", "wait for a while");
		mTestClientRestApi.login(email.getText().toString(), password.getText().toString(), new OnRequestComplete<Void>() {
			@Override
			public void onComplete(RestError error, Void result) {
				progress.dismiss();
				if(error != null) {
					Toast.makeText(LoginActivity.this, error.error, Toast.LENGTH_LONG).show();
					return;
				}
				
			}
		});
		
	}
	
	@Click
	void signup() {
		SignUpActivity_.intent(LoginActivity.this).start();
	}
}
