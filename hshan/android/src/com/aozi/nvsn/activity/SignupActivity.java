package com.aozi.nvsn.activity;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.aozi.nvsn.util.RestClient;
import com.aozi.util.HttpManager.OnPostExecute;
import com.aozi.util.JSONObjectBuilder;
import com.aozi.util.Util;
import com.example.nvsn.R;

public class SignupActivity extends Activity {
	private EditText et_email;
	private EditText et_password;
	private EditText et_password_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
	
		initializeVariable();
		fillWrittenUserInfo();
		
		findViewById(R.id.signup_btn_signin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isValidEmail()) {
					showTextViewError(et_email, "Invalid email address.");
					return;
				};
				
				if (!isValidPassword()) {
					showTextViewError(et_password_confirm, "Password is not matched.");
					return;
				}
				
				JSONObjectBuilder param = JSONObjectBuilder.create()
						.put("email", et_email.getText().toString())
						.put("password", et_password.getText().toString());

				RestClient.getInstance(getApplicationContext()).signup(param, new OnPostExecute() {
					@Override
					public void execute(HttpResponse response, JSONObjectBuilder result) {
						finish();
					}
				});
			};
		});
	}

	private void initializeVariable() {
		et_email = (EditText)findViewById(R.id.signup_email);
		et_email.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!isValidEmail()) {
					et_email.setError("Invalid email address.");
				}
			}
		});
		
		
		et_password = (EditText)findViewById(R.id.signup_password);
		
		et_password_confirm = (EditText)findViewById(R.id.signup_password_confirm);
		et_password_confirm.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!isValidPassword()) {
					et_password_confirm.setError("Password is not matched.");
				}
			}
		});
	}
	
	private boolean isValidEmail() {
		return Util.isValidEmail(et_email.getText().toString());
	}
	
	private boolean isValidPassword() {
		String password = et_password.getText().toString();
		String passwordConfirm = et_password_confirm.getText().toString();
		
		return password.equals(passwordConfirm);
	}
	
	private void fillWrittenUserInfo() {
		String email = getIntent().getStringExtra("email");
		String password = getIntent().getStringExtra("password");

		et_email.requestFocus();
		if (!email.equals("")) {
			et_email.setText(email);
			et_password.requestFocus();
			
			if (!password.equals("")) {
				et_password.setText(password);
				et_password_confirm.requestFocus();
			}
		}
	}
	
	private void showTextViewError(final EditText view, String errMsg) {
		Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
		shake.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				view.requestFocus();
				view.setError("Invalid email address.");
			}
		});
		view.startAnimation(shake);
	}
}
