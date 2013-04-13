package com.raket.silverstripe.parser.events;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.observers.SilverStripeObservable;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.psi.SilverStripeTypes.SS_BLOCK_END;

public class SilverStripeIfEventListener extends SilverStripeStatementEventListener {
	IElementType blockType;

	@Override
	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {
		currentToken = token;

		if (token != null && token.equals(SS_BLOCK_START) && !marking) {
			nextToken = parser.getNextToken();
			ifStatementTokens = TokenSet.orSet(ifStatementTokens, TokenSet.create(nextToken));
			if (ifTokens.contains(nextToken)) {
				PsiBuilder builder = parser.getBuilder();
				blockType = (nextToken == SS_IF_KEYWORD) ? SS_IF_STATEMENT : SS_ELSE_IF_STATEMENT;
				tokenText = parser.getNextTokenText();
				//setChanged().notifyObserversBefore(builder, tokenText);
				int currentOffset = builder.getCurrentOffset();
				observerMarker = builder.mark();
				marking = true;
			}
		}
	}

	@Override
	public void updateAfter(SilverStripeBaseParser parser, IElementType token) {
		if (marking && currentToken.equals(SS_BLOCK_END)) {
			observerMarker.done(blockType);
			dispatcher.triggerEvent("start_statement_complete", false, observerMarker, tokenText);
			marking = false;
		}
		if (marking && !ifStatementTokens.contains(currentToken)) {
			observerMarker.drop();
			marking = false;
		}
/*		if (builder.eof()) {
			setChanged().notifyObserversAfter(builder,tokenText);
		}*/
	}
}
