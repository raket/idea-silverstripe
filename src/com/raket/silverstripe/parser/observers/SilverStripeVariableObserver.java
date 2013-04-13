package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.observers.SilverStripeObservable;

import static com.raket.silverstripe.psi.SilverStripeTypes.NAMED_VAR;
import static com.raket.silverstripe.psi.SilverStripeTypes.SS_VAR;

public class SilverStripeVariableObserver extends SilverStripeParserObserver {
	@Override
	public void updateBefore(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		IElementType token = (IElementType) arg[1];
		if (token.equals(SS_VAR)) {
			observerMarker = builder.mark();
			marking = true;
		}
	}

	@Override
	public void updateAfter(SilverStripeObservable o, Object... arg) {
		if (marking) {
			observerMarker.done(NAMED_VAR);
			marking = false;
		}
	}
}