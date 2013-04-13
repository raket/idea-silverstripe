package com.raket.silverstripe.observers;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;

/**
 *  Our own Observable class that supports notifying before and after an event
 */
public abstract class SilverStripeObservable {
	private boolean changed = false;
	private Vector<SilverStripeObserver> obs;


	public SilverStripeObservable() {
		obs = new Vector<SilverStripeObserver>();
	}

	public synchronized SilverStripeObservable addObservers(@NotNull SilverStripeObserver... o) {
		for (int i = 0; i < o.length; i++) {
			addObserver(o[i]);
		}
		return this;
	}

	public synchronized SilverStripeObservable addObserver(@NotNull SilverStripeObserver o) {
		if (!obs.contains(o)) {
			obs.addElement(o);
		}
		return this;
	}

	public synchronized SilverStripeObservable deleteObserver(@NotNull SilverStripeObserver o) {
		obs.removeElement(o);
		return this;
	}

	public SilverStripeObservable notifyObserversBefore() {
		notifyObservers(true);
		return this;
	}

	public SilverStripeObservable notifyObserversAfter() {
		notifyObservers(false);
		return this;
	}

	public SilverStripeObservable notifyObserversBefore(Object... arg) {
		notifyObservers(true, arg);
		return this;
	}

	public SilverStripeObservable notifyObserversAfter(Object... arg) {
		notifyObservers(false, arg);
		return this;
	}

	private SilverStripeObservable notifyObservers(boolean before, Object... arg) {

		Object[] arrLocal;

		synchronized (this) {
			if (!changed)
				return this;
			arrLocal = obs.toArray();
			clearChanged();
		}
		if (before) {
			for (int i = 0; i < arrLocal.length; i++)
				((SilverStripeObserver)arrLocal[i]).updateBefore(this, arg);
		}
		else {
			for (int i = 0; i < arrLocal.length; i++)
				((SilverStripeObserver)arrLocal[i]).updateAfter(this, arg);
		}
		return this;
	}

	public synchronized SilverStripeObservable deleteObservers() {
		obs.removeAllElements();
		return this;
	}

	protected synchronized SilverStripeObservable setChanged() {
		changed = true;
		return this;
	}

	protected synchronized SilverStripeObservable clearChanged() {
		changed = false;
		return this;
	}

	public synchronized boolean hasChanged() {
		return changed;
	}

	public synchronized int countObservers() {
		return obs.size();
	}
}
