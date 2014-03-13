package com.aozi.nvsn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aozi.nvsn.HttpManager.OnPostExecute;
import com.aozi.util.CookieManager;
import com.example.nvsn.R;

public class MainActivity extends Activity {
	@Override
	protected void onStart() {
		super.onStart();
		
		final HttpManager httpManager = new HttpManager(
				Method.GET,  
				new Header[] {createCookieHeader()}, 
				"application/json",
				"sessionInfo", 
				null);
		
		httpManager.setOnPostExecute(new OnPostExecute() {
			@Override
			public void execute(HttpResponse response, JSONObjectBuilder result) {
				String sessionID = httpManager.getSessionID(response, CookieManager.getInstance(MainActivity.this).getSavedSessionID());
				if (result.has("user_id")) {
					UserMe.getInstance().setInfo(result);
				}
				CookieManager.getInstance(MainActivity.this).saveSessionID(sessionID);
			}
		});
		httpManager.execute();
	}

	public Header createCookieHeader() {
		List<Cookie> cookieList = new ArrayList<Cookie>();
		cookieList.add(new BasicClientCookie(CookieManager.KEY_SESSION_ID, CookieManager.getInstance(MainActivity.this).getSavedSessionID()));
		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		return cookieSpecBase.formatCookies(cookieList).get(0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViewById(R.id.main_btn_next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (UserMe.getInstance().isLogined()) {
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