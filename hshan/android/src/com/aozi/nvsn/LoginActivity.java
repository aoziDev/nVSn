package com.aozi.nvsn;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
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

		Button btn_signup = (Button)findViewById(R.id.btn_signup);
		btn_signup.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		Button btn_signin = (Button)findViewById(R.id.btn_signin);
		btn_signin.setOnClickListener(new OnClickListener() {
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