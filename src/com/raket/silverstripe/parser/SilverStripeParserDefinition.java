package com.raket.silverstripe.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.SilverStripeLexer;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripePsiElement;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.impl.SilverStripeIncludeImpl;
import com.raket.silverstripe.psi.impl.SilverStripeRequireImpl;
import com.raket.silverstripe.psi.impl.SilverStripeTranslationImpl;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

public class SilverStripeParserDefinition implements ParserDefinition{
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet HTML = TokenSet.create(SilverStripeTypes.CONTENT);
    public static final TokenSet COMMENTS = TokenSet.create(SilverStripeTypes.COMMENT);

    public static final IFileElementType FILE = new IFileElementType(Language.<SilverStripeLanguage>findInstance(SilverStripeLanguage.class));

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new SilverStripeLexer((Reader) null));
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new SilverStripeBaseParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SilverStripeFile(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if (type == SilverStripeTypes.SS_INCLUDE_STATEMENT) {
            return new SilverStripeIncludeImpl(node);
        } else if (type == SilverStripeTypes.SS_TRANSLATION_STATEMENT) {
			return new SilverStripeTranslationImpl(node);
		} else if (type == SilverStripeTypes.SS_REQUIRE_STATEMENT) {
			return new SilverStripeRequireImpl(node);
		} else if (type == SilverStripeTypes.NAMED_VAR) {
	        return new SilverStripeVariableImpl(node);
        }
        return new SilverStripePsiElement(node);
    }
}