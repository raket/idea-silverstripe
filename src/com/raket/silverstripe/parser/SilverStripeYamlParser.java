package com.raket.silverstripe.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-06
 * Time: 01:13
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeYamlParser implements PsiParser {

	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		while(!builder.eof()) {
			builder.advanceLexer();
		}
		return builder.getTreeBuilt();
	}
}
