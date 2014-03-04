package com.aozi.nvsn;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.nvsn.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		btn_signin.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				HttpTask httpTask = new HttpTask(et_email.getText().toString(), et_password.getText().toString());
				httpTask.execute(HTTP_Method.GET);
				return false;
			}
		});

		Button btn_logout = (Button)findViewById(R.id.btn_logout);
		btn_logout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}
}

enum HTTP_Method {
	GET, POST, PUT, DELETE
}
/**
 * 
 * AsyncTask<Param, Progress, Result>
 *
 */
class HttpTask extends AsyncTask<HTTP_Method, Void, Void> {
	private String id;
	private String pw;

	HttpTask (String id, String pw) {
		this.id = id;
		this.pw = pw;
	}

	@Override
	protected Void doInBackground(HTTP_Method... params) {
		HttpClient client = new DefaultHttpClient();
		String url = "http://121.254.40.70:3000/login";
		JSONObject param = null;
		try {
			param = new JSONObject()
				.put("email", id)
				.put("password", pw);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		HttpPost httpPost = new HttpPost(url);
		if (param != null) {
			try {
				httpPost.setEntity(new StringEntity(param.toString(), HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		httpPost.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		
		try {
			HttpResponse response = client.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			String string = EntityUtils.toString(resEntity);
			Log.e("Result", string);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
}