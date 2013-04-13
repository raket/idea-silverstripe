package com.raket.silverstripe.parser.events;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.observers.SilverStripeObservable;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-13
 * Time: 19:59
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeBlockEndEventListener extends SilverStripeStatementEventListener {
	int statementPosition = 0;

	@Override
	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {
		PsiBuilder builder = parser.getBuilder();
		currentToken = token;
		nextToken = parser.getNextToken();

		if (currentToken != null && nextToken != null && !marking) {
			if (currentToken.equals(SS_BLOCK_START) && nextToken.equals(SS_END_KEYWORD)) {
				tokenText = parser.getNextTokenText();
				observerMarker = builder.mark();
				statementPosition = 0;
				marking = true;
			}
		}
	}

	@Override
	public void updateAfter(SilverStripeBaseParser parser, IElementType token) {
		PsiBuilder builder = parser.getBuilder();
		if (marking && currentToken.equals(SS_BLOCK_END)) {
			observerMarker.done(SS_BLOCK_END_STATEMENT);
			dispatcher.triggerEvent("end_statement_complete", false, observerMarker, tokenText);
			marking = false;
		}
		if (marking && !currentToken.equals(endStatementTokens[statementPosition])) {
			observerMarker.drop();
			marking = false;
		}
		if (marking)
			statementPosition++;
	}
}
