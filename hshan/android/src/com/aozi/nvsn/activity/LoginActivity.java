package com.aozi.nvsn.activity;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.aozi.model.UserMe;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;
import com.example.nvsn.R;

interface Callback {
	void execute();
}

public class LoginActivity extends Activity {
	private UserMe userMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		userMe = UserMe.getInstance(getApplicationContext());
		
		final EditText et_email = (EditText)findViewById(R.id.et_email);
		final EditText et_password = (EditText)findViewById(R.id.et_password);

		findViewById(R.id.btn_signup).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("password", et_password.getText().toString());
				startActivity(intent);
			}
		});

		findViewById(R.id.btn_signin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userMe.login(et_email.getText().toString(), et_password.getText().toString(), new OnPostExecute() {
					@Override
					public void execute(HttpResponse response, JSONObjectBuilder result) {
						if (result.getInt("status") != 200) {
							return;
						}
						
						Intent intent = new Intent(LoginActivity.this, NextPageActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		});
	}
}