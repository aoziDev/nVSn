package com.aozi.nvsn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.nvsn.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViewById(R.id.main_btn_next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences(HttpManager.COOKIE_PREF, Activity.MODE_PRIVATE);
				String storedSID = pref.getString(HttpManager.SESSION_ID_KEY, "");
				if (storedSID.equals("")) {
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this, NextPageActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
	}
}