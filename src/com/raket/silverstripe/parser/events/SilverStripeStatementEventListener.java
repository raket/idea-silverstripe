package com.raket.silverstripe.parser.events;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.HashMap;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;


public class SilverStripeStatementEventListener extends SilverStripeParserEventListener {
	IElementType nextToken;
	String tokenText;
	static TokenSet string = TokenSet.create(SS_DOUBLE_LEFT, SS_DOUBLE_RIGHT, SS_SINGLE_LEFT, SS_SINGLE_RIGHT, SS_STRING);
	static TokenSet varTokens = TokenSet.create(SS_VAR, DOT, COMMA, LEFT_PAREN, RIGHT_PAREN, NUMBER, SS_STRING, SS_IDENTIFIER);
	static TokenSet varStatementTokens = TokenSet.orSet(TokenSet.create(SS_VAR_START_DELIMITER), varTokens);

	static HashMap<IElementType, TokenSet> STATEMENT_MAP = new HashMap<IElementType, TokenSet>();
	static HashMap<IElementType, IElementType> BLOCK_TYPE_MAP = new HashMap<IElementType, IElementType>();

	static TokenSet BLOCK_STATEMENTS = TokenSet.create(SS_IF_STATEMENT, SS_BLOCK_START_STATEMENT);
	static TokenSet ifStatementTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_AND_OR_OPERATOR,
		SS_COMPARISON_OPERATOR, SS_STRING), varTokens);
	static TokenSet elseTokens = TokenSet.create(SS_BLOCK_START, SS_ELSE_KEYWORD);
	static TokenSet startStatementTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_START_KEYWORD), varTokens);
	static TokenSet includeTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_INCLUDE_KEYWORD, SS_INCLUDE_FILE,
		COMMA, SS_COMPARISON_OPERATOR), varTokens);
	static TokenSet requireTokens = TokenSet.create(SS_BLOCK_START, SS_REQUIRE_KEYWORD, SS_REQUIRE_CSS, SS_REQUIRE_JS,
		SS_REQUIRE_THEME_CSS, SS_SINGLE_LEFT, SS_SINGLE_RIGHT, SS_DOUBLE_LEFT, SS_DOUBLE_RIGHT,
		LEFT_PAREN, RIGHT_PAREN, SS_STRING);
	static TokenSet cachedTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_CACHED_KEYWORD), varTokens);
	static TokenSet translationTokens = TokenSet.orSet(TokenSet.create(SS_BLOCK_START, SS_TRANSLATION_KEYWORD, SS_TRANSLATION_IDENTIFIER,
	SS_COMPARISON_OPERATOR, SS_VAR_START_DELIMITER, SS_VAR_END_DELIMITER), varTokens, string);
	static TokenSet simpleTokens = TokenSet.create(SS_BLOCK_START, SS_SIMPLE_KEYWORD, SS_BLOCK_END);
	static TokenSet badBlockTokens = TokenSet.create(SS_BLOCK_START, SS_BAD_BLOCK_STATEMENT, SS_BLOCK_END);
	static TokenSet commentTokens = TokenSet.create(SS_COMMENT_START, SS_COMMENT_END);

	static {
		STATEMENT_MAP.put(SS_IF_KEYWORD, TokenSet.orSet(TokenSet.create(SS_IF_KEYWORD), ifStatementTokens));
		STATEMENT_MAP.put(SS_ELSE_IF_KEYWORD, TokenSet.orSet(TokenSet.create(SS_ELSE_IF_KEYWORD), ifStatementTokens));
		STATEMENT_MAP.put(SS_ELSE_KEYWORD, elseTokens);
		STATEMENT_MAP.put(SS_START_KEYWORD, startStatementTokens);
		STATEMENT_MAP.put(SS_INCLUDE_KEYWORD, includeTokens);
		STATEMENT_MAP.put(SS_REQUIRE_KEYWORD, requireTokens);
		STATEMENT_MAP.put(SS_CACHED_KEYWORD, cachedTokens);
		STATEMENT_MAP.put(SS_TRANSLATION_KEYWORD, translationTokens);
		STATEMENT_MAP.put(SS_SIMPLE_KEYWORD, simpleTokens);
		STATEMENT_MAP.put(SS_BAD_BLOCK_STATEMENT, badBlockTokens);

		BLOCK_TYPE_MAP.put(SS_IF_KEYWORD, SS_IF_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_ELSE_IF_KEYWORD, SS_ELSE_IF_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_ELSE_KEYWORD, SS_ELSE_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_START_KEYWORD, SS_BLOCK_START_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_INCLUDE_KEYWORD, SS_INCLUDE_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_REQUIRE_KEYWORD, SS_REQUIRE_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_CACHED_KEYWORD, SS_CACHED_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_TRANSLATION_KEYWORD, SS_TRANSLATION_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_SIMPLE_KEYWORD, SS_BLOCK_SIMPLE_STATEMENT);
		BLOCK_TYPE_MAP.put(SS_BAD_BLOCK_STATEMENT, SS_BAD_BLOCK);
	}
	IElementType[] endStatementTokens = {SS_BLOCK_START, SS_END_KEYWORD, SS_BLOCK_END};
}
