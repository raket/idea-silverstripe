package com.raket.silverstripe.editor.highlighting;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.SilverStripeLexer;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

public class SilverStripeSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey SEPARATOR = createTextAttributesKey("SS_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEY = createTextAttributesKey("SS_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VALUE = createTextAttributesKey("SS_VALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT = createTextAttributesKey("SS_COMMENT", XmlHighlighterColors.HTML_COMMENT);
    public static final TextAttributesKey SS_BLOCK = createTextAttributesKey("SS_BLOCK", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey SS_KEYWORD = createTextAttributesKey("SS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey SS_BLOCK_VAR_KEY = createTextAttributesKey("SS_BLOCK_VAR", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

	private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
	private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] SS_BLOCK_KEYS = new TextAttributesKey[]{SS_BLOCK};
    private static final TextAttributesKey[] SS_KEYWORD_KEYS = new TextAttributesKey[]{SS_KEYWORD};
    private static final TextAttributesKey[] SS_BLOCK_VAR_KEYS = new TextAttributesKey[]{SS_BLOCK_VAR_KEY};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	private static final TextAttributesKey[] DOT_KEYS = new TextAttributesKey[]{DefaultLanguageHighlighterColors.DOT};
	private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{DefaultLanguageHighlighterColors.COMMA};
	private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{DefaultLanguageHighlighterColors.PARENTHESES};
	private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{DefaultLanguageHighlighterColors.STRING};
	private static final TextAttributesKey[] TAG_KEYS = new TextAttributesKey[]{DefaultLanguageHighlighterColors.MARKUP_TAG};

	private static final TokenSet BRACES = TokenSet.create(
			SS_VAR_START_DELIMITER,
			SS_VAR_END_DELIMITER
	);

	private static final TokenSet TAGS = TokenSet.create(
			SS_BLOCK_START,
			SS_BLOCK_END
	);

	private static final TokenSet COMMENTS = TokenSet.create(
			SS_COMMENT_START,
			SilverStripeTypes.COMMENT,
			SS_COMMENT_END
	);

	private static final TokenSet VARS = TokenSet.create(SS_BLOCK_VAR, SS_VAR, SS_INCLUDE_FILE);
	private static final TokenSet KEYWORDS = TokenSet.create(SS_START_KEYWORD, SS_END_KEYWORD, SS_IF_KEYWORD
			, SS_ELSE_IF_KEYWORD, SS_ELSE_KEYWORD, SS_SIMPLE_KEYWORD, SS_INCLUDE_KEYWORD, SS_CACHED_KEYWORD);
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
        } else if (TAGS.contains(tokenType)) {
		   return TAG_KEYS;
	   }  else if (VARS.contains(tokenType)) {
			return SS_BLOCK_VAR_KEYS;
		} else if (KEYWORDS.contains(tokenType)) {
            return SS_KEYWORD_KEYS;
        } else if (SEPARATORS.contains(tokenType)) {
			return SEPARATOR_KEYS;
		} else if (COMMENTS.contains(tokenType)) {
			return COMMENT_KEYS;
		} else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else if (tokenType.equals(SilverStripeTypes.DOT)) {
			return DOT_KEYS;
		} else if (tokenType.equals(SilverStripeTypes.COMMA)) {
			return COMMA_KEYS;
		} else if (tokenType.equals(SilverStripeTypes.SS_STRING)) {
			return STRING_KEYS;
		} else if (tokenType.equals(LEFT_PAREN) || tokenType.equals(RIGHT_PAREN)) {
			return PAREN_KEYS;
		}
        else {
			return EMPTY_KEYS;
        }
    }
}
