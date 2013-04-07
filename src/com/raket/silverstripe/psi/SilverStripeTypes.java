package com.raket.silverstripe.psi;

import com.intellij.psi.tree.IElementType;


public interface SilverStripeTypes {
	IElementType OUTER_WRAPPER = new SilverStripeElementType("OUTER_WRAPPER");
	IElementType SS_BAD_BLOCK_STATEMENT = new SilverStripeElementType("SS_BAD_BLOCK_STATEMENT");
	IElementType SS_BLOCK_END_STATEMENT = new SilverStripeElementType("SS_BLOCK_END_STATEMENT");
	IElementType SS_BLOCK_SIMPLE_STATEMENT = new SilverStripeElementType("SS_BLOCK_SIMPLE_STATEMENT");
	IElementType SS_BLOCK_START_STATEMENT = new SilverStripeElementType("SS_BLOCK_START_STATEMENT");
	IElementType SS_BLOCK_STATEMENT = new SilverStripeElementType("SS_BLOCK_STATEMENT");
	IElementType SS_BAD_BLOCK = new SilverStripeElementType("SS_BAD_BLOCK");
	IElementType SS_COMMENT_STATEMENT = new SilverStripeElementType("SS_COMMENT_STATEMENT");
	IElementType SS_FRAGMENT = new SilverStripeElementType("SS_FRAGMENT");
	IElementType SS_VAR_STATEMENT = new SilverStripeElementType("SS_VAR_STATEMENT");
	IElementType SS_IF_STATEMENT = new SilverStripeElementType("SS_IF_STATEMENT");
	IElementType SS_ELSE_IF_STATEMENT = new SilverStripeElementType("SS_ELSE_IF_STATEMENT");
	IElementType SS_ELSE_STATEMENT = new SilverStripeElementType("SS_ELSE_STATEMENT");
	IElementType SS_TRANSLATION_STATEMENT = new SilverStripeElementType("SS_TRANSLATION_STATEMENT");
	IElementType SS_STATEMENTS = new SilverStripeElementType("SS_STATEMENTS");
	IElementType SS_INCLUDE_STATEMENT = new SilverStripeElementType("SS_INCLUDE_STATEMENT");
	IElementType SS_CACHED_STATEMENT = new SilverStripeElementType("Cache statement");



	IElementType COMMENT = new SilverStripeTokenType("Comment");
	IElementType CONTENT = new SilverStripeTokenType("Content");
	IElementType COMMA = new SilverStripeTokenType("Comma");
	IElementType CRLF = new SilverStripeTokenType("CRLF");
	IElementType NUMBER = new SilverStripeTokenType("Number");
	IElementType LEFT_PAREN = new SilverStripeTokenType("(");
	IElementType RIGHT_PAREN = new SilverStripeTokenType(")");
	IElementType DOT = new SilverStripeTokenType("Dot");
	IElementType SS_STRING = new SilverStripeTokenType("String");
	IElementType SS_VAR_START = new SilverStripeTokenType("$");
	IElementType SS_VAR = new SilverStripeTokenType("Variable");
	IElementType SS_CACHED_KEYWORD = new SilverStripeTokenType("Cached keyword");
	IElementType SS_TRANSLATION_KEYWORD = new SilverStripeTokenType("Translation keyword");
	IElementType SS_TRANSLATION_IDENTIFIER = new SilverStripeTokenType("Translation identifier");
	IElementType SS_DOUBLE_LEFT = new SilverStripeTokenType("Left double quote");
	IElementType SS_DOUBLE_RIGHT = new SilverStripeTokenType("Right double quote");
	IElementType SS_SINGLE_LEFT = new SilverStripeTokenType("Left single quote");
	IElementType SS_SINGLE_RIGHT = new SilverStripeTokenType("Right single quote");

	IElementType SS_BAD_VAR = new SilverStripeTokenType("SS_BAD_VAR");
	IElementType SS_BLOCK_END = new SilverStripeTokenType("SS_BLOCK_END");
	IElementType SS_BLOCK_END_START = new SilverStripeTokenType("SS_BLOCK_END_START");
	IElementType SS_BLOCK_SIMPLE_START = new SilverStripeTokenType("SS_BLOCK_SIMPLE_START");
	IElementType SS_BLOCK_START = new SilverStripeTokenType("SS_BLOCK_START");
	IElementType SS_BLOCK_START_START = new SilverStripeTokenType("SS_BLOCK_START_START");
	IElementType SS_BLOCK_VAR = new SilverStripeTokenType("SS_BLOCK_VAR");
	IElementType SS_COMMENT_END = new SilverStripeTokenType("SS_COMMENT_END");
	IElementType SS_COMMENT_START = new SilverStripeTokenType("SS_COMMENT_START");
	IElementType SS_END_KEYWORD = new SilverStripeTokenType("SS_END_KEYWORD");
	IElementType SS_IF_KEYWORD = new SilverStripeTokenType("SS_IF_KEYWORD");
	IElementType SS_ELSE_IF_KEYWORD = new SilverStripeTokenType("SS_ELSE_IF_KEYWORD");
	IElementType SS_ELSE_KEYWORD = new SilverStripeTokenType("SS_ELSE_KEYWORD");
	IElementType SS_COMPARISON_OPERATOR = new SilverStripeTokenType("SS_COMPARISON_OPERATOR");
	IElementType SS_AND_OR_OPERATOR = new SilverStripeTokenType("SS_AND_OR_OPERATOR");
	IElementType SS_TRANSLATION_CONTENT = new SilverStripeTokenType("SS_TRANSLATION_CONTENT");

	IElementType SS_SIMPLE_KEYWORD = new SilverStripeTokenType("SS_SIMPLE_KEYWORD");
	IElementType SS_INCLUDE_KEYWORD = new SilverStripeTokenType("SS_INCLUDE_KEYWORD");
	IElementType SS_INCLUDE_FILE = new SilverStripeTokenType("SS_INCLUDE_FILE");
	IElementType SS_START_KEYWORD = new SilverStripeTokenType("SS_START_KEYWORD");
	IElementType SS_VAR_END_DELIMITER = new SilverStripeTokenType("SS_VAR_END_DELIMITER");
	IElementType SS_VAR_START_DELIMITER = new SilverStripeTokenType("SS_VAR_START_DELIMITER");

	//SimpleEntry<IElementType, SilverStripeTokenType>
}
