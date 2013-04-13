package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.observers.SilverStripeObservable;
import com.raket.silverstripe.observers.SilverStripeObserver;

abstract class SilverStripeParserObserver extends SilverStripeObservable implements SilverStripeObserver {
	protected  boolean marking = false;
	protected PsiBuilder.Marker observerMarker;
	protected IElementType currentToken;
}