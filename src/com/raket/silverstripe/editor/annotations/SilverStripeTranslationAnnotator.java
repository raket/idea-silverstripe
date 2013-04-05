package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Stack;
import com.raket.silverstripe.psi.impl.SilverStripeTranslationImpl;
import com.intellij.lang.ASTNode;

import static com.raket.silverstripe.SilverStripeBundle.message;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-05
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeTranslationAnnotator implements Annotator {
	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		Stack<String> varKeywords = new Stack<String>();
		if (element instanceof SilverStripeTranslationImpl) {
			SilverStripeTranslationImpl ssElement = (SilverStripeTranslationImpl) element;
			ASTNode node = element.getNode();
			ASTNode[] children = node.getChildren(TokenSet.create(SS_STRING, SS_DOUBLE_RIGHT, SS_SINGLE_RIGHT, SS_VAR, SS_COMPARISON_OPERATOR));
			String name = ssElement.getName();
			boolean collectedVars = false;
			for (int i = 0; i < children.length; i++) {
				if (children[i].getElementType().equals(SS_DOUBLE_RIGHT) || children[i].getElementType().equals(SS_SINGLE_RIGHT)) {
					collectedVars = true;
					continue;
				}
				if (!collectedVars && children[i].getElementType().equals(SS_VAR)) {
					 varKeywords.add(0,children[i].getText());
				}

				if (collectedVars && children[i].getElementType().equals(SS_VAR) && !varKeywords.isEmpty()) {
					if (varKeywords.peek().equals(children[i].getText())) {
						varKeywords.pop();
					}
				}
			}

			if (name == null) {
				holder.createErrorAnnotation(ssElement, message("ss.annotations.translation.no.identifier"));
			}
			else if (varKeywords.size() > 0) {
				holder.createErrorAnnotation(ssElement, message("ss.annotations.translation.no.parameter.match", varKeywords.peek()));
			}
		}
	}
}