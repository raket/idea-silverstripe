package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.observers.SilverStripeObservable;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;

public class SilverStripeEndObserver extends SilverStripeStatementObserver {
	IElementType nextToken;
	String tokenText;
	int statementPosition = 0;

	@Override
	public void updateBefore(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		currentToken = builder.getTokenType();
		nextToken = ((SilverStripeBaseParser)o).getNextToken();

		if (currentToken != null && nextToken != null && !marking) {
			if (currentToken.equals(SS_BLOCK_START) && nextToken.equals(SS_END_KEYWORD)) {
				tokenText = ((SilverStripeBaseParser)o).getNextTokenText();
				observerMarker = builder.mark();
				statementPosition = 0;
				marking = true;
			}
		}
	}

	@Override
	public void updateAfter(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		if (marking && currentToken.equals(SS_BLOCK_END)) {
			observerMarker.done(SS_BLOCK_END_STATEMENT);
			notifyObserversAfter(builder, observerMarker, tokenText, true);
			marking = false;
		}
		if (marking && !currentToken.equals(endStatementTokens[statementPosition])) {
			observerMarker.drop();
			notifyObserversAfter(builder, observerMarker, tokenText, false);
			marking = false;
		}
		if (marking)
			statementPosition++;
	}
}
