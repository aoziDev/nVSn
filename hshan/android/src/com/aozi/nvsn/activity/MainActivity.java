package com.aozi.nvsn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.model.UserMe;
import com.example.nvsn.R;

public class MainActivity extends Activity {
	private UserMe userMe;
	@Override
	protected void onStart() {
		super.onStart();
		
		userMe = UserMe.getInstance(getApplicationContext());
		userMe.checkLogin();
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
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				
				finish();
			}
		});
	}
}