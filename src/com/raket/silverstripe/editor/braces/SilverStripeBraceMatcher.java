package com.raket.silverstripe.editor.braces;

import com.intellij.codeInsight.highlighting.BraceMatcher;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-25
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeBraceMatcher implements BraceMatcher {
	private static final TokenSet LEFT_BRACES = TokenSet.create(
		SS_BLOCK_START
	);

	private static final TokenSet ALWAYS_LEFT = TokenSet.create(
		SS_VAR_START_DELIMITER,
		SS_COMMENT_START,
		LEFT_PAREN
	);

	private static final TokenSet RIGHT_BRACES = TokenSet.create(
		SS_BLOCK_END
	);

	private static final TokenSet ALWAYS_RIGHT = TokenSet.create(
		SS_VAR_END_DELIMITER,
		SS_COMMENT_END,
		RIGHT_PAREN
	);

	private static final TokenSet ALWAYS_BRACES = TokenSet.create(
		SS_SIMPLE_KEYWORD,
		SS_INCLUDE_KEYWORD,
		SS_ELSE_KEYWORD,
		SS_BAD_BLOCK_STATEMENT,
		SS_TRANSLATION_CONTENT,
		SS_ELSE_IF_KEYWORD
	);

	private static final TokenSet ALL_BRACES = TokenSet.orSet(
		LEFT_BRACES,
		RIGHT_BRACES,
		ALWAYS_LEFT,
		ALWAYS_RIGHT
	);

	private static final TokenSet LEFT_BLOCK_BRACES = TokenSet.orSet(
		TokenSet.create(SS_START_KEYWORD, SS_IF_KEYWORD, SS_CACHED_KEYWORD),
		ALWAYS_BRACES
	);

	private static final TokenSet RIGHT_BLOCK_BRACES = TokenSet.orSet(
			TokenSet.create(SS_END_KEYWORD), ALWAYS_BRACES
	);

	/*
	static {
		LEFT_BRACES.add(HbTokenTypes.OPEN);
		LEFT_BRACES.add(HbTokenTypes.OPEN_PARTIAL);
		LEFT_BRACES.add(HbTokenTypes.OPEN_UNESCAPED);
		LEFT_BRACES.add(HbTokenTypes.OPEN_BLOCK);
		LEFT_BRACES.add(HbTokenTypes.OPEN_INVERSE);

		RIGHT_BRACES.add(HbTokenTypes.CLOSE);
	} */

	@Override
	public boolean isPairBraces(IElementType tokenType1, IElementType tokenType2) {
		TokenSet myTokens = ALL_BRACES;
		return ALL_BRACES.contains(tokenType1) && ALL_BRACES.contains(tokenType2)
				|| ALL_BRACES.contains(tokenType1) && ALL_BRACES.contains(tokenType2);
	}

	@Override
	public boolean isLBraceToken(HighlighterIterator iterator, CharSequence fileText, FileType fileType) {
		boolean start = false;
		// These braces are always self contained
		if (ALWAYS_LEFT.contains(iterator.getTokenType())) return true;
		if (!LEFT_BRACES.contains(iterator.getTokenType())) {
			// definitely not a left brace
			return false;
		}

		boolean isLBraceToken = false;
		int iteratorAdvanceCount = 0;
		while (true) {

			iterator.advance();
			iteratorAdvanceCount++;
			if (iterator.atEnd()) {
				break;
			}
			IElementType tokenType = iterator.getTokenType();

			// This means we've reached the end of this block
			if (iterator.getTokenType() == SS_BLOCK_END) {
				break;
			}

			if (LEFT_BLOCK_BRACES.contains(tokenType)) {
				isLBraceToken = true;
				break;
			}
		}

		// reset the given iterator before returning
		while (iteratorAdvanceCount-- > 0) {
			iterator.retreat();
		}

		return isLBraceToken;
	}

	@Override
	public boolean isRBraceToken(HighlighterIterator iterator, CharSequence fileText, FileType fileType) {
		// These braces are always self contained
		if (ALWAYS_RIGHT.contains(iterator.getTokenType())) return true;
		if (!RIGHT_BRACES.contains(iterator.getTokenType())) {
			// definitely not a right brace
			return false;
		}

		boolean isRBraceToken = false;
		int iteratorRetreatCount = 0;
		while (true) {
			iterator.retreat();
			iteratorRetreatCount++;

			if (iterator.atEnd()) {
				break;
			}
			IElementType tokenType = iterator.getTokenType();

			// This means we've reached the beginning of this block
			if (tokenType == SS_BLOCK_START) {
				// the first open type token we encountered is a block opener,
				// so this is not a close brace (the paired close brace for these tokens
				// is at the end of the corresponding block close 'stache)
				break;
			}

			if (RIGHT_BLOCK_BRACES.contains(tokenType)) {
				// the first open token we encountered was a simple opener (i.e. didn't start a block)
				// or the close brace of a close block 'stache for some open block.  Definitely a right brace.
				isRBraceToken = true;
				break;
			}
		}

		// reset the given iterator before returning
		while (iteratorRetreatCount-- > 0) {
			iterator.advance();
		}

		return isRBraceToken;
	}

	@Override
	public int getBraceTokenGroupId(IElementType tokenType) {
		return 1;
	}

	@Override
	public boolean isStructuralBrace(HighlighterIterator iterator, CharSequence text, FileType fileType) {
		return false;
	}

	@Nullable
	@Override
	public IElementType getOppositeBraceTokenType(@NotNull IElementType type) {
		return null;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return openingBraceOffset;
	}
}