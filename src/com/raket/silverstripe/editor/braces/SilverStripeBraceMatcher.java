package com.raket.silverstripe.editor.braces;

import com.intellij.codeInsight.highlighting.BraceMatcher;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-25
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeBraceMatcher implements BraceMatcher {
	private static final TokenSet LEFT_BRACES = TokenSet.create(
		SS_BLOCK_START,
		SS_BLOCK_START_START,
		SS_BLOCK_SIMPLE_START,
		SS_VAR_START_DELIMITER
	);

	private static final TokenSet RIGHT_BRACES = TokenSet.create(
		SS_BLOCK_END,
		SS_VAR_END_DELIMITER
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
		return LEFT_BRACES.contains(tokenType1) && RIGHT_BRACES.contains(tokenType2)
				|| RIGHT_BRACES.contains(tokenType1) && LEFT_BRACES.contains(tokenType2);
	}

	@Override
	public boolean isLBraceToken(HighlighterIterator iterator, CharSequence fileText, FileType fileType) {
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

			if (iterator.getTokenType() == SS_END_KEYWORD) {
				break;
			}

			if (iterator.getTokenType() == SS_START_KEYWORD || iterator.getTokenType() == SS_SIMPLE_KEYWORD) {
				isLBraceToken = true;
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
		//return RIGHT_BRACES.contains(iterator.getTokenType());

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

			if (iterator.getTokenType() == SS_START_KEYWORD) {
				// the first open type token we encountered is a block opener,
				// so this is not a close brace (the paired close brace for these tokens
				// is at the end of the corresponding block close 'stache)
				break;
			}

			if (iterator.getTokenType() == SS_END_KEYWORD || iterator.getTokenType() == SS_SIMPLE_KEYWORD) {
				// the first open token we encountered was a simple opener (i.e. didn't start a block)
				// or the close brace of a close block 'stache for some open block.  Definitely a right brace.
				isRBraceToken = true;
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