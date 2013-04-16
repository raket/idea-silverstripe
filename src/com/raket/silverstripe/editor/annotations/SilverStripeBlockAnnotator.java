package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;

import static com.raket.silverstripe.SilverStripeBundle.message;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class SilverStripeBlockAnnotator implements Annotator {
	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType().equals(SS_BLOCK_END_STATEMENT)) {
			PsiElement myElement = element.getParent();
			if (!myElement.getNode().getElementType().equals(SS_BLOCK_STATEMENT)) {
				ASTNode[] startChildren = myElement.getNode().getChildren(TokenSet.create(SS_IF_STATEMENT, SS_BLOCK_START_STATEMENT));
				ASTNode[] endChildren = myElement.getNode().getChildren(TokenSet.create(SS_BLOCK_END_STATEMENT));
				Collections.reverse(Arrays.asList(startChildren));
				int myPosition = 1;
				for (int i = 1; i <= endChildren.length; i++) {
					if (endChildren[i-1] == element.getNode()) {
						myPosition = i;
						break;
					}
				}
				if (startChildren.length == 0 || myPosition > startChildren.length) {
					holder.createErrorAnnotation(element, message("ss.parsing.unexpected.end.statement"));
				} else {
					ASTNode child = startChildren[myPosition-1].findChildByType(TokenSet.create(SS_START_KEYWORD, SS_IF_KEYWORD));
					if (child != null)
						holder.createErrorAnnotation(element, message("ss.parsing.unexpected.end.statement.expected", "end_" + child.getText()));
				}
			}
		}
		else if (element.getNode().getElementType().equals(SS_BLOCK_START_STATEMENT)
			|| element.getNode().getElementType().equals(SS_IF_STATEMENT)) {
			PsiElement myElement = element.getParent();
			if (!myElement.getNode().getElementType().equals(SS_BLOCK_STATEMENT)) {
				ASTNode child = element.getNode().findChildByType(TokenSet.create(SS_START_KEYWORD, SS_IF_KEYWORD));
				if (child != null)
					holder.createErrorAnnotation(element, message("ss.parsing.unclosed.block",child.getText()));
			}
		}
	}
}