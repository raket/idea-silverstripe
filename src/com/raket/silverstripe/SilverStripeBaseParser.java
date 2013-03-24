package com.raket.silverstripe;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Stack;
import com.raket.silverstripe.psi.SilverStripeCompositeElementType;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.SSErrorTokenTypes.ERROR_TOKEN_MESSAGES;
import static com.raket.silverstripe.SSErrorTokenTypes.ERROR_TOKENS;
import static com.raket.silverstripe.SilverStripeBundle.message;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-21
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */

public class SilverStripeBaseParser implements PsiParser {
	Stack<String> levelTypeStack = new Stack<String>();
	Stack<PsiBuilder.Marker> levelStack = new Stack<PsiBuilder.Marker>();
	String[] startStatements = {"if", "loop", "with", "control"};
	String[] endStatements = {"end_if", "end_loop", "end_with", "end_control"};

	private class ParseResult {
		public boolean success = false;
		public PsiBuilder.Marker marker;
	}

	/**
	 * TODO Split up parse method into smaller parts.
	 * TODO Find a way to rollback for error marking?
	 *
	 * @param root The file currently being parsed
	 * @param builder used to build the parse tree
	 * @return parsed tree
	 */

    @NotNull
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder) {
		PsiBuilder.Marker rootMarker = builder.mark();
		PsiBuilder.Marker wrapperMarker = builder.mark();

		builder.setDebugMode(true);
        // Process all tokens
		parseTree(builder);

        wrapperMarker.done(SilverStripeTypes.OUTER_WRAPPER);
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

	// Tries to parse the whole tree
	private boolean parseTree(PsiBuilder builder) {
		ParseResult parseResult;
		IElementType type;
		String tokenValue;

		while(!builder.eof()) {
			type = builder.getTokenType();
			parseResult = new ParseResult();
			if (type == SS_BLOCK_START) {
				tokenValue = getNextTokenValue(builder);
				parseResult = parseStatementsBlock(builder, tokenValue);
				if (parseResult.success)
					buildStatementsBlock(builder, tokenValue, parseResult);
			}
			else if (type == SS_VAR_DELIMITER) {
				parseResult = parseVarStatement(builder, type);
			}
			if (!parseResult.success)
				consumeToken(builder, type); // move to next token

			// These are errors, all blocks should be closed when we've reached the End Of File
			if (builder.eof() && !levelStack.empty()) {
				levelStack.pop().rollbackTo();
				String levelType = levelTypeStack.pop();
				buildErrorStatement(parseStatementsBlock(builder, levelType),message("ss.parsing.unclosed.block", levelType ));
			}
		}

		return builder.eof();
	}

	private ParseResult parseVarStatement(PsiBuilder builder, IElementType type) {
		ParseResult parseResult = new ParseResult();
		boolean tokensConsumed;
		IElementType varKeyword = builder.lookAhead(1);
		IElementType[] varTokens = {SS_VAR_DELIMITER, varKeyword, SS_VAR_DELIMITER};
		PsiBuilder.Marker varMarker = builder.mark();

		tokensConsumed = consumeTokens(builder, varTokens, TokenSet.create());
		if (tokensConsumed) {
			varMarker.done(SS_VAR_STATEMENT);
			parseResult.success = true;
		}
		else {
			varMarker.error(ERROR_TOKEN_MESSAGES.get(SS_VAR_STATEMENT));
		}
		parseResult.marker = varMarker;
		return parseResult;
	}

	/**
	 * TODO Move the logic of deciding if to execute to another method. {@link #parse(com.intellij.psi.tree.IElementType, com.intellij.lang.PsiBuilder)}
	 *
	 * @param builder currently not needed. TODO Remove?
	 * @param tokenValue is the type of block being processed.
	 * @param parseResult is the latest result from the block parsing.
	 *                    expecting either finished start or end block.
	 */
	private void buildStatementsBlock(PsiBuilder builder, String tokenValue, ParseResult parseResult) {
		if (!Arrays.asList(startStatements).contains(tokenValue) && !Arrays.asList(endStatements).contains(tokenValue))
			return;
		// Starts the statements block
		if (Arrays.asList(startStatements).contains(tokenValue)) {
			levelStack.push(parseResult.marker.precede());
			levelTypeStack.push(tokenValue);
		}
		// We have an ending statement
		else if (!levelStack.empty() && tokenValue.contains(levelTypeStack.peek())) {
			levelTypeStack.pop();
			levelStack.pop().done(SilverStripeTypes.SS_BLOCK_STATEMENT);
		}
		// No match, it's an error - we roll back to mark the error
		else if (!levelStack.empty()) {
			String levelType = levelTypeStack.pop();
			PsiBuilder.Marker level = levelStack.pop();

			level.rollbackTo();
			buildErrorStatement(parseStatementsBlock(builder, levelType), message("ss.parsing.unclosed.block", tokenValue));
		}
	}

	/**
	 * This method will build an error statement. It will roll
	 * @param elementToMark
	 * @param errorMessage
	 */
	private void buildErrorStatement(ParseResult elementToMark, String errorMessage) {
		PsiBuilder.Marker errorMarker = elementToMark.marker.precede();
		errorMarker.error(errorMessage);
	}

	private String getNextTokenValue(PsiBuilder builder) {
		String returnString;
		PsiBuilder.Marker rb = builder.mark();
		builder.advanceLexer();
		returnString = builder.getTokenText();
		rb.rollbackTo();
		return returnString;
	}

    private ParseResult parseStatementsBlock(PsiBuilder builder, String tokenValue) {
        PsiBuilder.Marker marker = builder.mark();
        IElementType nextToken = builder.lookAhead(1);
	    ParseResult parseResult = new ParseResult();
		boolean markAsError = false;
		String errorMessage = "";

        boolean result = false;

		if (nextToken == SS_START_KEYWORD) {
			IElementType varToken = builder.lookAhead(2);
			IElementType[] tokensToConsume = {SS_BLOCK_START, SS_START_KEYWORD, varToken, SS_BLOCK_END};
			result = createBlock(builder, SS_BLOCK_START_STATEMENT, tokensToConsume, TokenSet.create());
			if (varToken == SS_BAD_VAR) {
				//markAsError = true;
				//errorMessage = "Incomplete var statement. Add closing delimiter.";

			}
        }
        else if (nextToken == SS_END_KEYWORD) {
			IElementType[] tokensToConsume = {SS_BLOCK_START, SS_END_KEYWORD, SS_BLOCK_END};
            result = createBlock(builder, SS_BLOCK_END_STATEMENT, tokensToConsume, TokenSet.create());

			// Was this end block expected? If not it needs to be marked as an error
			if (levelTypeStack.empty() || !tokenValue.contains(levelTypeStack.peek())) {
				markAsError = true;
				errorMessage = message("ss.parsing.unexpected.end.statement");
			}
        }
        else if (nextToken == SS_SIMPLE_KEYWORD) {
			IElementType[] tokensToConsume = {SS_BLOCK_START, SS_SIMPLE_KEYWORD, SS_BLOCK_VAR, SS_BLOCK_END};
            result = createBlock(builder, SS_BLOCK_SIMPLE_STATEMENT, tokensToConsume, TokenSet.create(SS_BLOCK_VAR));
        }

		parseResult.marker = marker;
		parseResult.success = result;
		if (result) {
            marker.done(new SilverStripeCompositeElementType("SS_"+tokenValue.toUpperCase()));
			if (markAsError)
				buildErrorStatement(parseResult, errorMessage);
        }
        else {
            marker.rollbackTo();
        }
        return parseResult;
    }

	/**
	 *
	 * Attempts to create a block of the type markerType containing the tokens specified.
	 * The tokens in exclude are optional and the parser will continue parsing the block if they are missing.
	 *
	 * @param builder the builder for the parser.
	 * @param markerType the element type of the block being created.
	 * @param tokens an array of tokens that need to be consumed.
	 * @param exclude a TokenSet of tokens that are optional.
	 * @return true if block creation is successful
	 */
    private boolean createBlock(PsiBuilder builder, IElementType markerType, IElementType[] tokens, TokenSet exclude) {
        PsiBuilder.Marker marker = builder.mark();
        boolean result;
        result = consumeTokens(builder, tokens, exclude);
        if (result) {
            marker.done(markerType);
        }
        else {
            marker.rollbackTo();
        }
        return result;
    }

	/**
	 *
	 * TODO Rewind builder with a marker on failure?
	 * @param builder the builder for the parser.
	 * @param tokens an array of tokens that need to be consumed.
	 * @param exclude a TokenSet of tokens that are optional.
	 * @return success or failure of token consumption
	 */
	private boolean consumeTokens(PsiBuilder builder, IElementType[] tokens, TokenSet exclude) {

		for (int i = 0, tokensLength = tokens.length; i < tokensLength; i++) {
			if (tokens[i] != builder.getTokenType() && exclude.contains(tokens[i])) {
				continue;
			}
			if (tokens[i] == builder.getTokenType()) {
				consumeToken(builder, tokens[i]);
			}
			else {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * Consumes a single token by advancing the lexer.
	 * Is error token aware and will error mark these tokens.
	 * Uses {@link SSErrorTokenTypes.ERROR_TOKENS} to determine bad tokens and {@link SSErrorTokenTypes.ERROR_TOKEN_MESSAGES} for error messages.
	 * @param builder the builder for the parser.
	 * @param token the token to be consumed.
	 */
	private void consumeToken(PsiBuilder builder, IElementType token) {
		if (ERROR_TOKENS.contains(token)) {
			PsiBuilder.Marker errorMarker = builder.mark();
			builder.advanceLexer();
			errorMarker.error(ERROR_TOKEN_MESSAGES.get(token));
		}
		else {
			builder.advanceLexer();
		}
	}
}
