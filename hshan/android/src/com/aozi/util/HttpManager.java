package com.aozi.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class HttpManager {
	public static final String USER_EMAIL = "user.email";
	public static final String IS_LOGIN = "islogined";
	public static final String URL = "http://121.254.40.70:3000";
	
	public static abstract class OnPostExecute {
		public void execute(Result result) {
			if (result.isError) {
				onError(result);
			} else {
				onSuccess(result);
			}
		}
		
		public abstract void onSuccess(Result result);
		public abstract void onError(Result result);
	}

	public enum Method {
		GET {
			@Override
			HttpUriRequest getRequest(String urlPath, JSONObject param) {
				HttpGet httpGet = new HttpGet(urlPath);
				return httpGet;
			}
		},	
		POST {
			@Override
			HttpUriRequest getRequest(String urlPath, JSONObject param) {
				HttpPost httpPost = new HttpPost(urlPath);
				if (param != null) {
					try {
						httpPost.setEntity(new StringEntity(param.toString(), HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				return httpPost;
			}
		};
		
		abstract HttpUriRequest getRequest(String path, JSONObject param); 
	}
	
	private HttpUriRequest request;
	private CookieManager cookieManager;
	
	public HttpManager(Context context) {
		this.cookieManager = CookieManager.getInstance(context);
	}
	
	public void execute(Method method, String contentType, String path, JSONObject param, final OnPostExecute onPostExecute) {
		createRequest(method, contentType, path, param);

		new AsyncTask<HttpUriRequest, Void, HttpResponse>() {
			@Override
			protected HttpResponse doInBackground(HttpUriRequest... request) {
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = null;
				try {
					response = client.execute(request[0]);
					saveSessionID(getSessionID(response));
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return response;
			}

			@Override
			protected void onPostExecute(HttpResponse response) {
				if (onPostExecute != null) {
					onPostExecute.execute(new Result(response));
				}
			}
		}.execute(request);
	}
	
	private void createRequest(Method method, String contentType, String path, JSONObject param) {
		String urlStr = URL + (path.startsWith("/") ? path : "/"+path);
		request = method.getRequest(urlStr, param);
		request.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
		request.addHeader(createCookieHeader());
	}

	public String getSessionID(HttpResponse response) {
		Header[] headers = response.getHeaders(CookieManager.KEY_COOKIE_HEADER);
		
		if (headers.length != 0) {
			Header cookie = headers[0];
			HeaderElement[] elements = cookie.getElements();
			return elements[0].getValue();
		}
		
		return cookieManager.getSavedSessionID();
	}

	private Header createCookieHeader() {
		List<Cookie> cookieList = new ArrayList<Cookie>();
		cookieList.add(new BasicClientCookie(CookieManager.KEY_SESSION_ID, cookieManager.getSavedSessionID()));
		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		return cookieSpecBase.formatCookies(cookieList).get(0);
	}
	
	private void saveSessionID(String sessionID) {
		cookieManager.saveSessionID(sessionID);
	}
	
	public JSONObjectBuilder getJsonResult(HttpResponse response) {
		HttpEntity resEntity = response.getEntity();
		String result = "";
		try {
			result = EntityUtils.toString(resEntity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return JSONObjectBuilder.create(result);
	}
	
	private int getStatusCode(JSONObjectBuilder jsonObject) {
		if (jsonObject.has("status")) {
			return jsonObject.getInt("status");
		}
		
		return 400;
	}
	
	public class Result {
		public boolean isError = false;
		public String message = "";
		private JSONObjectBuilder jsonBuilder = null;
		
		public Result(HttpResponse response) {
			if (response == null) {
				isError = true;
				message = "Network error";
			} else {
				jsonBuilder = getJsonResult(response);
				
				int statusCode = getStatusCode(jsonBuilder);
				isError = !(statusCode >= 200 && statusCode < 300);
				message = jsonBuilder.getString("message");
			}
		}

		public JSONObjectBuilder getJsonObject() {
			return jsonBuilder;
		}
	}
}

