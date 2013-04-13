package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.observers.SilverStripeObservable;
import com.raket.silverstripe.parser.SilverStripeBaseParser;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

public class SilverStripeIfObserver extends SilverStripeStatementObserver {
	IElementType blockType;
	IElementType nextToken;
	String tokenText;

	@Override
	public void updateBefore(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
	    IElementType token = builder.getTokenType();
		currentToken = token;

		if (token != null && token.equals(SS_BLOCK_START) && !marking) {
			nextToken = ((SilverStripeBaseParser)o).getNextToken();
			ifStatementTokens = TokenSet.orSet(ifStatementTokens, TokenSet.create(nextToken));
			if (ifTokens.contains(nextToken)) {
				blockType = (nextToken == SS_IF_KEYWORD) ? SS_IF_STATEMENT : SS_ELSE_IF_STATEMENT;
				tokenText = ((SilverStripeBaseParser)o).getNextTokenText();
				//setChanged().notifyObserversBefore(builder, tokenText);
				int currentOffset = builder.getCurrentOffset();
				observerMarker = builder.mark();
				marking = true;
			}
		}
	}

	@Override
	public void updateAfter(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		if (marking && currentToken.equals(SS_BLOCK_END)) {
			observerMarker.done(blockType);
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
