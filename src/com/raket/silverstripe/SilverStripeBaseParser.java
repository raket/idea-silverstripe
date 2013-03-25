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
 * TODO Decide if different parse levels (tokens, elements and blocks) should be moved out to separate classes.
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-21
 * Time: 21:40
 */

public class SilverStripeBaseParser implements PsiParser {
	Stack<BlockLevel> blockLevelStack = new Stack<BlockLevel>();
	String[] startStatements = {"if", "loop", "with", "control"};
	String[] endStatements = {"end_if", "end_loop", "end_with", "end_control"};

	private class ParseResult {
		public boolean success = false;
		public PsiBuilder.Marker marker;
	}

	/**
	 * This class is responsible for creating block levels that contain finished block statements.
	 * It wraps the opening block statement in a temporary error which gets dropped if the block statement
	 * gets finished properly. Yay for being able to drop done markers.
	 */
	private class BlockLevel {
		public String levelType;
		public PsiBuilder.Marker level;
		public ParseResult levelResult;
		private PsiBuilder.Marker error;

		/**
		 * @param currentLevelType the type of level being started. "if", "loop", "with" or "control".
		 * @param currentLevelResult the block tag which gets wrapped in the block level.
		 */
		public BlockLevel(String currentLevelType, ParseResult currentLevelResult) {
			levelType = currentLevelType;
			level = currentLevelResult.marker.precede();
			levelResult = currentLevelResult;
			error = buildErrorStatement(levelResult, message("ss.parsing.unclosed.block", currentLevelType));
		}

		public void done(IElementType type) {
			level.done(type);
			error.drop();
		}

		public void drop() {
			level.drop();
		}

		public String toString() {
			return levelType;
		}
	}

	/**
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

        wrapperMarker.done(OUTER_WRAPPER);
        rootMarker.done(root);
		ASTNode tree = builder.getTreeBuilt();
        return tree;
    }

	// Tries to parse the whole tree
	private boolean parseTree(PsiBuilder builder) {
		ParseResult parseResult;
		IElementType type;
		String tokenValue;

		while(!builder.eof()) {
			type = builder.getTokenType();
			parseResult = new ParseResult();
			if (type == SS_BLOCK_START || type == SS_COMMENT_START) {
				tokenValue = getNextTokenValue(builder);
				parseResult = parseStatementsBlock(builder, tokenValue);
				if (parseResult.success)
					buildStatementsBlock(builder, tokenValue, parseResult);
			}
			else if (type == SS_VAR_START_DELIMITER) {
				parseResult = parseVarStatement(builder, type);
			}
			if (!parseResult.success)
				consumeToken(builder, type); // move to next token

			// These are errors, all blocks should be closed when we've reached the End Of File
			// We drop the block statement marker and put an error around the open block statement
			while (builder.eof() && !blockLevelStack.empty()) {
				BlockLevel currentLevel = blockLevelStack.pop();
				currentLevel.drop();
				//buildErrorStatement(currentLevel.levelResult, message("ss.parsing.unclosed.block", currentLevel.toString() ));
			}
		}

		return builder.eof();
	}

	private ParseResult parseVarStatement(PsiBuilder builder, IElementType type) {
		ParseResult parseResult = new ParseResult();
		boolean tokensConsumed;
		IElementType varKeyword = builder.lookAhead(1);
		IElementType[] varTokens = {SS_VAR_START_DELIMITER, varKeyword, SS_VAR_END_DELIMITER};
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
			blockLevelStack.push(new BlockLevel(tokenValue, parseResult));
		}
		// We have an ending statement
		else if (!blockLevelStack.empty() && tokenValue.contains(blockLevelStack.peek().toString())) {
			blockLevelStack.pop().done(SS_BLOCK_STATEMENT);
		}
		// No match, it's an error - we drop the block statement and mark the offending element
		else if (!blockLevelStack.empty()) {
			BlockLevel currentLevel = blockLevelStack.pop();
			currentLevel.drop();
			//buildErrorStatement(currentLevel.levelResult, message("ss.parsing.unclosed.block", currentLevel.levelType));
		}
	}

	/**
	 * This method will build an error statement. It will roll
	 * @param elementToMark
	 * @param errorMessage
	 */
	private PsiBuilder.Marker buildErrorStatement(ParseResult elementToMark, String errorMessage) {
		PsiBuilder.Marker errorMarker = elementToMark.marker.precede();
		errorMarker.error(errorMessage);
		return errorMarker;
	}

	private String getNextTokenValue(PsiBuilder builder) {
		String returnString;
		PsiBuilder.Marker rb = builder.mark();
		builder.advanceLexer();
		returnString = builder.getTokenText();
		rb.rollbackTo();
		return returnString;
	}
	// TODO Remove token remapping. Brace matcher does its magic before the parser is invoked.
    private ParseResult parseStatementsBlock(PsiBuilder builder, String tokenValue) {
        PsiBuilder.Marker marker = builder.mark();
        IElementType nextToken = builder.lookAhead(1);
	    ParseResult parseResult = new ParseResult();
		boolean markAsError = false;
		String errorMessage = "";

        boolean result = false;

		if (nextToken == SS_START_KEYWORD) {
			IElementType varToken = builder.lookAhead(2);
			builder.remapCurrentToken(SS_BLOCK_START_START);
			IElementType[] tokensToConsume = {SS_BLOCK_START_START, SS_START_KEYWORD, varToken, SS_BLOCK_END};
			result = createBlock(builder, SS_BLOCK_START_STATEMENT, tokensToConsume, TokenSet.create());
        }
        else if (nextToken == SS_END_KEYWORD) {
			builder.remapCurrentToken(SS_BLOCK_END_START);
			IElementType[] tokensToConsume = {SS_BLOCK_END_START, SS_END_KEYWORD, SS_BLOCK_END};
            result = createBlock(builder, SS_BLOCK_END_STATEMENT, tokensToConsume, TokenSet.create());

			// Was this end block expected? If not it needs to be marked as an error
			if (blockLevelStack.empty() || !tokenValue.contains(blockLevelStack.peek().toString())) {
				markAsError = true;
				if (!blockLevelStack.empty())
					errorMessage = message("ss.parsing.unexpected.end.statement.expected", "end_"+blockLevelStack.peek().toString());
				else
					errorMessage = message("ss.parsing.unexpected.end.statement");
			}
        }
        else if (nextToken == SS_SIMPLE_KEYWORD) {
			builder.remapCurrentToken(SS_BLOCK_SIMPLE_START);
			IElementType[] tokensToConsume = {SS_BLOCK_SIMPLE_START, SS_SIMPLE_KEYWORD, SS_BLOCK_VAR, SS_BLOCK_END};
            result = createBlock(builder, SS_BLOCK_SIMPLE_STATEMENT, tokensToConsume, TokenSet.create(SS_BLOCK_VAR));
        }
		else if (builder.getTokenType() == SS_COMMENT_START) {
			IElementType[] tokensToConsume = {SS_COMMENT_START, SS_COMMENT_END};
			result = createBlock(builder, SS_COMMENT_STATEMENT, tokensToConsume, TokenSet.create());
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
