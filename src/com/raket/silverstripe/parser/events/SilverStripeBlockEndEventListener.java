package com.raket.silverstripe.parser.events;

/*
public class SilverStripeBlockEndEventListener extends SilverStripeStatementEventListener {
	int statementPosition = 0;

	@Override
	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {
		builder = parser.getBuilder();
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
		if (marking && currentToken.equals(SS_BLOCK_END)) {
			observerMarker.done(SS_BLOCK_END_STATEMENT);
			dispatcher.triggerEvent("block_end_statement_complete", false, this, tokenText);
			marking = false;
		}
		if (marking && !currentToken.equals(endStatementTokens[statementPosition])) {
			observerMarker.drop();
			marking = false;
		}
		if (marking)
			statementPosition++;

		super.updateAfter(parser, token);
	}
} */