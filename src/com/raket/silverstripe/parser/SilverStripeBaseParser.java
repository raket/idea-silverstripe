package com.raket.silverstripe.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.parser.events.SilverStripeParserEventListener;
import org.jetbrains.annotations.NotNull;

import static com.raket.silverstripe.psi.SilverStripeTypes.SS_STATEMENTS;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-21
 * Time: 21:40
 */

public class SilverStripeBaseParser extends SilverStripeParserEventListener implements PsiParser {
	IElementType nextToken;
	String nextTokenText;

	private void getNextTokenValue() {
		PsiBuilder.Marker rb = builder.mark();
		builder.advanceLexer();
		nextToken = builder.getTokenType();
		nextTokenText = builder.getTokenText();
		rb.rollbackTo();
	}

	public IElementType getNextToken() {
		if (nextToken == null) {
			getNextTokenValue();
		}
		return  nextToken;
	}

	public String getNextTokenText() {
		if (nextTokenText == null) {
			getNextTokenValue();
		}
		return  nextTokenText;
	}

	/**
	 * @param root    The file currently being parsed
	 * @param builder used to build the parse tree
	 * @return parsed tree
	 */

	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		this.builder = builder;
		PsiBuilder.Marker rootMarker = builder.mark();
		PsiBuilder.Marker wrapperMarker = builder.mark();

		builder.setDebugMode(true);
		// Process all tokens
		parseTree(builder);

		wrapperMarker.done(SS_STATEMENTS);
		rootMarker.done(root);
		return builder.getTreeBuilt();
	}

	// Tries to parse the whole tree
	private boolean parseTree(PsiBuilder builder) {
		IElementType type;

		while (!builder.eof()) {
			type = builder.getTokenType();
			dispatcher.triggerEvent("before_consume", false, this, type);
			builder.advanceLexer();
			dispatcher.triggerEvent("after_consume", false, this, type);
			nextToken = null;
			nextTokenText = null;
		}
		dispatcher.triggerEvent("builder_eof", false, this, null);
		return builder.eof();
	}
}