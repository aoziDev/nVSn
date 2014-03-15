package com.aozi.nvsn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.aozi.model.UserMe;
import com.aozi.util.DialogManager;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.HttpManager.Result;
import com.example.nvsn.R;

interface Callback {
	void execute();
}

public class SigninActivity extends Activity {
	private UserMe userMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		userMe = UserMe.getInstance(SigninActivity.this);
		
		final EditText et_email = (EditText)findViewById(R.id.et_email);
		final EditText et_password = (EditText)findViewById(R.id.et_password);

		findViewById(R.id.btn_signup).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
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
					public void onSuccess(Result result) {
						Intent intent = new Intent(SigninActivity.this, NextPageActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onError(Result result) {
						DialogManager.showErrorDialog(SigninActivity.this, result.message);
					}
				});
			}
		});
	}
}