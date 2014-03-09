package com.croquis.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CREventEmitter {
	private class Binding {
		Object mOwner;
		boolean mOnce;
		Observer mObserver;

		Binding(Object owner, boolean once, Observer observer) {
			mOwner = owner;
			mOnce = once;
			mObserver = observer;
		}
	}

	private final Map<String, List<Binding>> mBindingsMap = new HashMap<String, List<Binding>>();

	public interface Observer {
		void onEmit();
	}

	private void add(String event, Binding binding) {
		List<Binding> bindings = mBindingsMap.get(event);
		if (bindings == null) {
			bindings = new ArrayList<Binding>();
			mBindingsMap.put(event, bindings);
		}
		bindings.add(binding);
	}

	public void on(String event, Object owner, Observer observer) {
		add(event, new Binding(owner, false, observer));
	}

	public void once(String event, Object owner, Observer observer) {
		add(event, new Binding(owner, true, observer));
	}

	public void offAll() {
		mBindingsMap.clear();
	}

	public void off(String event, Object owner) {
		List<Binding> bindings = mBindingsMap.get(event);
		if (bindings != null) {
			for (Iterator<Binding> it = bindings.iterator(); it.hasNext();) {
				Binding binding = it.next();
				if (binding.mOwner == owner) {
					it.remove();
				}
			}
		}
	}

	public void offWithEvent(String event) {
		mBindingsMap.remove(event);
	}

	public void offWithOwner(Object owner) {
		for (List<Binding> bindings : mBindingsMap.values()) {
			for (Iterator<Binding> it = bindings.iterator(); it.hasNext();) {
				Binding binding = it.next();
				if (binding.mOwner == owner) {
					it.remove();
				}
			}
		}
	}

	public void emit(String event) {
		if (isBlocked()) {
			return;
		}
		List<Binding> bindings = mBindingsMap.get(event);
		if (bindings != null) {
			// emit 중에 on, off가 호출될 경우 ConcurrentModificationException가 발생할 수 있기 때문에 복사본을 이용한다.
			List<Binding> clone = new ArrayList<Binding>();
			for (Binding binding : bindings) {
				clone.add(binding);
			}
			// 이벤트 emit
			for (Binding binding : clone) {
				binding.mObserver.onEmit();
			}
			// 한번만 호출해야 하는 이벤트 제거
			for (Iterator<Binding> it = bindings.iterator(); it.hasNext();) {
				Binding binding = it.next();
				if (binding.mOnce) {
					it.remove();
				}
			}
		}
	}

	private int mBlockCount;

	public boolean isBlocked() {
		return mBlockCount > 0;
	}

	public void block() {
		mBlockCount++;
	}

	public void unblock() {
		mBlockCount--;
	}
}
