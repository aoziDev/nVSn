package com.croquis.mvc;

import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.croquis.util.RestClient;
import com.croquis.util.RestClient_;

public abstract class CRModel extends CREventEmitter {
	public static final String EVENT_UPDATE = "event_update";
	public static final String EVENT_DELETE = "event_delete";

	private Context mContext;
	private RestClient mRestClient;

	protected Context getContext() {
		return mContext;
	}

	protected RestClient getRestClient() {
		return mRestClient;
	}

	// for serialization
	protected CRModel() {
	}

	public CRModel(Context context) {
		setContext(context);
	}

	public CRModel(Context context, JSONObject json) {
		this(context);
		if (json != null) {
			set(json);
		}
	}

	void setContext(Context context) {
		mContext = context;
		mRestClient = RestClient_.getInstance_(mContext);
	}

	public abstract void set(JSONObject json);

	protected static final String nullString = new String();
	@SuppressLint("UseValueOf")
	protected static final Integer nullInteger = new Integer(Integer.MAX_VALUE);
	@SuppressLint("UseValueOf")
	protected static final Double nullDouble = new Double(Double.MAX_VALUE);
	protected static final Bitmap nullBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
	protected static final Date nullDate = new Date(0);
}
