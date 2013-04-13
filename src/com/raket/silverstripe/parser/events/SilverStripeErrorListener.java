package com.raket.silverstripe.parser.events;

import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

import static com.raket.silverstripe.SSErrorTokenTypes.ERROR_TOKENS;
import static com.raket.silverstripe.SSErrorTokenTypes.ERROR_TOKEN_MESSAGES;

public class SilverStripeErrorListener extends SilverStripeParserEventListener {

	@Override
	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {
		if (ERROR_TOKENS.contains(token)) {
			observerMarker = parser.getBuilder().mark();
			marking = true;
		}
	}

	@Override
	public void updateAfter(SilverStripeBaseParser parser, IElementType token) {
		if (marking) {
			observerMarker.error(ERROR_TOKEN_MESSAGES.get(token));
			marking = false;
		}
	}
}