package com.aozi.nvsn;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.model.UserMe;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;
import com.example.nvsn.R;

public class NextPageActivity extends Activity {
	private UserMe userMe = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next);
		
		userMe = UserMe.getInstance(getApplicationContext());
		findViewById(R.id.next_btn_logout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				userMe.logout(new OnPostExecute() {
					@Override
					public void execute(HttpResponse response, JSONObjectBuilder result) {
//						CookieManager.getInstance(getApplicationContext()).removeSessionID();
						
						Intent intent = new Intent(NextPageActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
				
				
				
			}
		});
		
	}
}
