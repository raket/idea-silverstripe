package com.raket.silverstripe;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.Reader;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SilverStripeSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey SEPARATOR = createTextAttributesKey("SS_SEPARATOR", SyntaxHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEY = createTextAttributesKey("SS_KEY", SyntaxHighlighterColors.KEYWORD);
    public static final TextAttributesKey VALUE = createTextAttributesKey("SS_VALUE", SyntaxHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT = createTextAttributesKey("SS_COMMENT", SyntaxHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SS_BLOCK = createTextAttributesKey("SS_BLOCK", SyntaxHighlighterColors.BRACES);
    public static final TextAttributesKey SS_KEYWORD = createTextAttributesKey("SS_KEYWORD", SyntaxHighlighterColors.KEYWORD);
    public static final TextAttributesKey SS_BLOCK_VAR_KEY = createTextAttributesKey("SS_BLOCK_VAR", SyntaxHighlighterColors.STRING);

    static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SS_BAD_CHARACTER",
            new TextAttributes(Color.RED, null, null, null, Font.BOLD));

	private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
	private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] SS_BLOCK_KEYS = new TextAttributesKey[]{SS_BLOCK};
    private static final TextAttributesKey[] SS_KEYWORD_KEYS = new TextAttributesKey[]{SS_KEYWORD};
    private static final TextAttributesKey[] SS_BLOCK_VAR_KEYS = new TextAttributesKey[]{SS_BLOCK_VAR_KEY};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	private static final TokenSet BRACES = TokenSet.create(
			SS_BLOCK_START,
			SS_BLOCK_END,
			SS_COMMENT_START,
			SS_COMMENT_END,
			SS_VAR_START_DELIMITER,
			SS_VAR_END_DELIMITER
	);

	private static final TokenSet VARS = TokenSet.create(SS_BLOCK_VAR, SS_VAR, SilverStripeTypes.SS_STRING);
	private static final TokenSet KEYWORDS = TokenSet.create(SS_START_KEYWORD, SS_END_KEYWORD, SS_IF_KEYWORD
			, SS_ELSE_IF_KEYWORD, SS_ELSE_KEYWORD, SS_SIMPLE_KEYWORD);
	private static final TokenSet SEPARATORS = TokenSet.create(SS_COMPARISON_OPERATOR, SS_AND_OR_OPERATOR);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new SilverStripeLexer((Reader) null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (BRACES.contains(tokenType)) {
            return SS_BLOCK_KEYS;
        } else if (VARS.contains(tokenType)) {
			return SS_BLOCK_VAR_KEYS;
		} else if (KEYWORDS.contains(tokenType)) {
            return SS_KEYWORD_KEYS;
        } else if (SEPARATORS.contains(tokenType)) {
			return SEPARATOR_KEYS;
		} else if (tokenType.equals(COMMENT)) {
			return COMMENT_KEYS;
		} else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }
        else {
			return EMPTY_KEYS;
        }
    }
}
