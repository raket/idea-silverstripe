package com.raket.silverstripe.observers;

import com.raket.silverstripe.observers.SilverStripeObservable;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-12
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public interface SilverStripeObserver {
	void updateBefore(SilverStripeObservable o, Object... arg);
	void updateAfter(SilverStripeObservable o, Object... arg);
}
