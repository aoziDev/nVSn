package com.aozi.nvsn;

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

import com.aozi.util.CookieManager;

import android.os.AsyncTask;

enum Method {
	GET {
		@Override
		HttpUriRequest getRequest(Header[] headers, String urlPath, JSONObject param) {
			HttpGet httpGet = new HttpGet(urlPath);
			addHeaders(httpGet, headers);
			return httpGet;
		}
	},	
	POST {
		@Override
		HttpUriRequest getRequest(Header[] headers, String urlPath, JSONObject param) {
			HttpPost httpPost = new HttpPost(urlPath);
			addHeaders(httpPost, headers);
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
	
	void addHeaders(HttpUriRequest request, Header[] headers) {
		for (Header header : headers) {
			request.addHeader(header);
		}
	}
	
	abstract HttpUriRequest getRequest(Header[] headers, String path, JSONObject param); 
}


public class HttpManager {
	public static final String USER_EMAIL = "user.email";
	public static final String IS_LOGIN = "islogined";
	
	public static final String URL = "http://121.254.40.70:3000";
	
	private HttpUriRequest request;
	private OnPostExecute onPostExecute;
	
	public static interface OnPostExecute {
		void execute(HttpResponse response, JSONObjectBuilder result);
	}

	public HttpManager(Method method, Header[] headers, String contentType, String path, JSONObject param) {
		String urlStr = URL + (path.startsWith("/") ? path : "/"+path);
		request = method.getRequest(headers, urlStr, param);
		request.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
	}
	
	public void setOnPostExecute(OnPostExecute onPostExecute) {
		this.onPostExecute = onPostExecute;
	}
	
	public void execute() {
		new AsyncTask<HttpUriRequest, Void, HttpResponse>() {
			@Override
			protected HttpResponse doInBackground(HttpUriRequest... request) {
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = null;
				try {
					response = client.execute(request[0]);
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
					onPostExecute.execute(response, getJsonResult(response));
				}
			}
		}.execute(request);
	}
	
	public String getSessionID(HttpResponse response, String defaultValue) {
		Header[] headers = response.getHeaders(CookieManager.KEY_COOKIE_HEADER);
		
		if (headers.length != 0) {
			Header cookie = headers[0];
			HeaderElement[] elements = cookie.getElements();
			return elements[0].getValue();
		}
		
		return defaultValue;
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
	
	public Header createCookieHeader(String sessionID) {
		List<Cookie> cookieList = new ArrayList<Cookie>();
		cookieList.add(new BasicClientCookie(CookieManager.KEY_SESSION_ID, sessionID));
		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		return cookieSpecBase.formatCookies(cookieList).get(0);
	}
}
