package com.raket.silverstripe.parser.events;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.psi.SilverStripeTypes.SS_BLOCK_END;


public class SilverStripeStatementEventListener extends SilverStripeParserEventListener {
	IElementType nextToken;
	String tokenText;

	String[] startStatements = {"if", "loop", "with", "control", "cached"};
	String[] endStatements = {"end_if", "end_loop", "end_with", "end_control", "end_cached"};
	String[] statementContainers = {"if", "loop", "with", "control", "else_if", "else", "cached"};
	TokenSet string = TokenSet.create(SS_DOUBLE_LEFT, SS_DOUBLE_RIGHT, SS_SINGLE_LEFT, SS_SINGLE_RIGHT, SS_STRING);
	TokenSet varTokens = TokenSet.create(SS_VAR, DOT, COMMA, LEFT_PAREN, RIGHT_PAREN, NUMBER, SS_STRING, SS_IDENTIFIER);
	TokenSet varStatementTokens = TokenSet.orSet(TokenSet.create(SS_VAR_START_DELIMITER), varTokens);

	TokenSet ifTokens = TokenSet.create(SS_IF_KEYWORD, SS_ELSE_IF_KEYWORD);
	TokenSet ifStatementTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_AND_OR_OPERATOR,
		SS_COMPARISON_OPERATOR, SS_STRING), varTokens);
	IElementType[] elseTokens = {SS_BLOCK_START, SS_ELSE_KEYWORD, SS_BLOCK_END};
	IElementType[] endStatementTokens = {SS_BLOCK_START, SS_END_KEYWORD, SS_BLOCK_END};


	IElementType consumedToken;
	boolean validStatement = false;
}
