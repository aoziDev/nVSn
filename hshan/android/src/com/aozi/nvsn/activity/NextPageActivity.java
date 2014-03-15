package com.aozi.nvsn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.model.UserMe;
import com.aozi.util.DialogManager;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.HttpManager.Result;
import com.example.nvsn.R;

public class NextPageActivity extends Activity {
	private UserMe userMe = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next);
		
		userMe = UserMe.getInstance(NextPageActivity.this);
		findViewById(R.id.next_btn_logout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userMe.logout(new OnPostExecute() {
					@Override
					public void onSuccess(Result result) {
						Intent intent = new Intent(NextPageActivity.this, SigninActivity.class);
						startActivity(intent);
						finish();						
					}

					@Override
					public void onError(Result result) {
						DialogManager.showErrorDialog(NextPageActivity.this, result.message);
					}
				});
			}
		});
		
	}
}
