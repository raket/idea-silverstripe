package com.raket.silverstripe.parser.events;

import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

import static com.raket.silverstripe.psi.SilverStripeTypes.NAMED_VAR;
import static com.raket.silverstripe.psi.SilverStripeTypes.SS_VAR;

public class SilverStripeVariableEventListener extends SilverStripeParserEventListener {

	@Override
	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {
		if (token.equals(SS_VAR)) {
			observerMarker = parser.getBuilder().mark();
			marking = true;
		}
	}

	@Override
	public void updateAfter(SilverStripeBaseParser parser, IElementType token) {
		if (marking) {
			observerMarker.done(NAMED_VAR);
			marking = false;
		}
	}
}
