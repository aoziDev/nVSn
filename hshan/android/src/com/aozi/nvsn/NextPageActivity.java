package com.aozi.nvsn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.util.CookieManager;
import com.example.nvsn.R;

public class NextPageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next);
		
		findViewById(R.id.next_btn_logout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CookieManager.getInstance(NextPageActivity.this).removeSessionID();
				
				Intent intent = new Intent(NextPageActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
}
