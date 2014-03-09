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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.nvsn.R;

interface Callback {
	void execute();
}

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

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
				JSONObjectBuilder param = JSONObjectBuilder.create()
						.put("email", et_email.getText())
						.put("password", et_password.getText());

				
				final HttpManager httpManager = new HttpManager(
						Method.POST, 
						new Header[] {createCookieHeader()}, 
						"application/json",
						"login", 
						param);
				
				httpManager.setOnPostExecute(new HttpManager.OnPostExecute() {
					@Override
					public void execute(HttpResponse response) {
						String sessionID = httpManager.getSessionID(response, getSavedSessionID());
						JSONObjectBuilder result = httpManager.getJsonResult(response);
						
						if (result.getInt("status") != 200) {
							return;
						}
						
						saveSessionID(sessionID);
						
						Intent intent = new Intent(LoginActivity.this, NextPageActivity.class);
						startActivity(intent);
						finish();
					}
				});
				httpManager.execute();
			}
		});
	}
	
	public Header createCookieHeader() {
		List<Cookie> cookieList = new ArrayList<Cookie>();
		cookieList.add(new BasicClientCookie(HttpManager.SESSION_ID_KEY, getSavedSessionID()));
		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		return cookieSpecBase.formatCookies(cookieList).get(0);
	}
	
	private String getSavedSessionID() {
		SharedPreferences pref = getSharedPreferences(HttpManager.COOKIE_PREF, Activity.MODE_PRIVATE);
		return pref.getString(HttpManager.SESSION_ID_KEY, "");
	}

	private void saveSessionID(String sessionID) {
		SharedPreferences pref = getSharedPreferences(HttpManager.COOKIE_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(HttpManager.SESSION_ID_KEY, sessionID);
		editor.commit();
	}
}