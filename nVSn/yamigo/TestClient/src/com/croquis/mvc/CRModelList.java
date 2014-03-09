package com.croquis.mvc;

import static com.croquis.mvc.CRModel.EVENT_UPDATE;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.croquis.util.RestClient;
import com.croquis.util.RestClient.OnRequestComplete;
import com.croquis.util.RestClient.RestError;
import com.croquis.util.RestClient_;

public abstract class CRModelList<T extends CRModel> extends CREventEmitter implements Iterable<T> {
	private List<T> mModels = new ArrayList<T>();

	private final Context mContext;
	private final RestClient mRestClient;

	protected Context getContext() {
		return mContext;
	}

	protected RestClient getRestClient() {
		return mRestClient;
	}

	// internal use only
	protected CRModelList() {
		mContext = null;
		mRestClient = null;
	}

	public CRModelList(Context context) {
		mContext = context;
		mRestClient = RestClient_.getInstance_(mContext);
	}

	public CRModelList(Context context, JSONArray array) {
		this(context);
		if (array != null) {
			set(array);
		}
	}

	public void set(JSONArray array) {
		set(array, true);
	}

	public void set(JSONArray array, boolean reset) {
		set(array, reset, false);
	}

	public void set(JSONArray array, boolean reset, boolean front) {
		block();

		if (reset) {
			clear();
		}
		if (array != null && array.length() > 0) {
			if (front) {
				for (int i = array.length() - 1; i >= 0; i--) {
					JSONObject json = array.optJSONObject(i);
					if (json != null) {
						add(0, create(getContext(), json));
					}
				}
			} else {
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.optJSONObject(i);
					if (json != null) {
						add(create(getContext(), json));
					}
				}
			}
		}

		unblock();

		emit(EVENT_UPDATE);
	}

	public abstract T create(Context context, JSONObject json);

	public void add(T object) {
		mModels.add(object);
		emit(EVENT_UPDATE);

		object.on(EVENT_UPDATE, this, new Observer() {
			@Override
			public void onEmit() {
				CRModelList.this.emit(EVENT_UPDATE);
			}
		});
	}

	public void add(int index, T object) {
		mModels.add(index, object);
		emit(EVENT_UPDATE);

		object.on(EVENT_UPDATE, this, new Observer() {
			@Override
			public void onEmit() {
				CRModelList.this.emit(EVENT_UPDATE);
			}
		});
	}

	public int size() {
		return mModels.size();
	}

	public T get(int location) {
		return mModels.get(location);
	}

	public void clear() {
		mModels.clear();
		emit(EVENT_UPDATE);
	}

	public void remove(int location) {
		mModels.remove(location);
		emit(EVENT_UPDATE);
	}

	public void remove(T object) {
		mModels.remove(object);
		emit(EVENT_UPDATE);
	}

	public void sort(Comparator<T> comparator) {
		Collections.sort(mModels, comparator);
	}

	@Override
	public Iterator<T> iterator() {
		return mModels.iterator();
	}

	public void fetch(String path, JSONObject parameters, OnRequestComplete<Void> complete) {
		fetch(true, path, parameters, complete);
	}

	public void fetch(final boolean reset, String path, JSONObject parameters, final OnRequestComplete<Void> complete) {
		fetch(reset, false, path, parameters, complete);
	}

	public void fetch(final boolean reset, final boolean front, String path, JSONObject parameters,
			final OnRequestComplete<Void> complete) {
		getRestClient().getList(path, parameters, new OnRequestComplete<JSONArray>() {
			@Override
			public void onComplete(RestError error, JSONArray result) {
				if (error == null) {
					set(result, reset, front);
				}
				if (complete != null) {
					complete.onComplete(error, null);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void load(String fileName) {
		FileInputStream fis = null;
		try {
			fis = getContext().openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mModels = (List<T>) ois.readObject();
			for (T model : mModels) {
				model.setContext(getContext());
			}
		} catch (ClassCastException e) {
		} catch (ClassNotFoundException e) {
		} catch (FileNotFoundException e) {
		} catch (StreamCorruptedException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 모델 목록을 파일에 저장한다.
	 * 
	 * 해당 모델은 {@link java.io.Serializable} 인터페이스를 구현해야 한다.
	 * 
	 * @param fileName
	 */
	public void save(String fileName) {
		FileOutputStream fos = null;
		try {
			fos = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mModels);
			oos.flush();
			fos.getFD().sync();
		} catch (NotSerializableException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
