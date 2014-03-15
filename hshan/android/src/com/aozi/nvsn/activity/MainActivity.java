package com.aozi.nvsn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.model.UserMe;
import com.aozi.util.DialogManager;
import com.aozi.util.JSONObjectBuilder;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.HttpManager.Result;
import com.example.nvsn.R;

public class MainActivity extends Activity {
	private UserMe userMe;
	@Override
	protected void onStart() {
		super.onStart();
		
		userMe = UserMe.getInstance(MainActivity.this);
		userMe.checkLogin(new OnPostExecute() {
			@Override
			public void onError(Result result) {
				DialogManager.showErrorDialog(MainActivity.this, result.message);
			}

			@Override
			public void onSuccess(Result result) {
				JSONObjectBuilder jsonObject = result.getJsonObject();
				if (jsonObject.has("user_id")) {
					userMe.setInfo(jsonObject);
				}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		findViewById(R.id.main_btn_next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userMe.isLogined()) {
					Intent intent = new Intent(MainActivity.this, NextPageActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this, SigninActivity.class);
					startActivity(intent);
				}
				
				finish();
			}
		});
	}
}