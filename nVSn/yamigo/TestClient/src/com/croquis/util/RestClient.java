package com.croquis.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

@EBean(scope = Scope.Singleton)
public class RestClient {
	public static class RestError {
		public String error;
		public String description;

		public RestError(String error, String description) {
			this.error = error;
			this.description = description;
		}

		public static final RestError NETWORK_ERROR = new RestError("network error", "");
		public static final RestError UNRECOGNIZABLE_RESULT = new RestError("unrecognizable result", "");
		public static final RestError UNKNOWN_ERROR = new RestError("unknown error", "");
	}

	public interface OnRequestComplete<T> {
		void onComplete(RestError error, T result);
	}

	@Bean
	RestClientImplApache mImplApache;

	private String mServerUrl = "";

	public final void setServerUrl(String serverUrl) {
		mServerUrl = serverUrl;
	}

	public final String getServerUrl() {
		return mServerUrl;
	}

	public void clearSessionCookie() {
		mImplApache.clearSession();
	}

	public void getUrl(String url, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.getNoCookie(url, parameters, complete, JSONObject.class);
	}

	public void get(String path, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.get(mServerUrl + path, parameters, complete, JSONObject.class);
	}

	public void getList(String path, JSONObject parameters, OnRequestComplete<JSONArray> complete) {
		mImplApache.get(mServerUrl + path, parameters, complete, JSONArray.class);
	}

	public void post(String path, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.post(mServerUrl + path, parameters, complete, JSONObject.class);
	}

	public void postGzip(String path, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.postGzip(mServerUrl + path, parameters, complete, JSONObject.class);
	}

	public void post(String path, MultipartEntity multipartEntity, OnRequestComplete<JSONObject> complete) {
		mImplApache.post(mServerUrl + path, multipartEntity, complete, JSONObject.class);
	}

	public void postList(String path, JSONObject parameters, OnRequestComplete<JSONArray> complete) {
		mImplApache.post(mServerUrl + path, parameters, complete, JSONArray.class);
	}

	public void put(String path, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.put(mServerUrl + path, parameters, complete, JSONObject.class);
	}

	public void put(String path, MultipartEntity multipartEntity, OnRequestComplete<JSONObject> complete) {
		mImplApache.put(mServerUrl + path, multipartEntity, complete, JSONObject.class);
	}

	public void delete(String path, JSONObject parameters, OnRequestComplete<JSONObject> complete) {
		mImplApache.delete(mServerUrl + path, parameters, complete, JSONObject.class);
	}

	public static void getBitmap(String url, OnRequestComplete<Bitmap> callback) {
		new GetBitmapAsync(callback).execute(url);
	}

	private static class GetBitmapAsync extends AsyncTask<String, Void, Bitmap> {
		private final OnRequestComplete<Bitmap> mCallback;

		public GetBitmapAsync(OnRequestComplete<Bitmap> callback) {
			mCallback = callback;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			try {
				URL streamURL = new URL(url);
				URLConnection urlConnection = streamURL.openConnection();
				urlConnection.connect();
				InputStream is = urlConnection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				is.close();
				return bitmap;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result == null) {
				mCallback.onComplete(new RestError("", ""), null);
			} else {
				mCallback.onComplete(null, result);
			}
		}
	}
}
