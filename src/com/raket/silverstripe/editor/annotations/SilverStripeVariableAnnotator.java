package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.psi.PsiElement;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import static com.raket.silverstripe.SilverStripeBundle.message;


public class SilverStripeVariableAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == SilverStripeTypes.SS_VAR) {
			String varValue = element.getText();
			PsiElement nextSibling = element.getNextSibling();
			if (varValue != null && !varValue.startsWith("$")) {
				Annotation annotation = holder
					.createWarningAnnotation(element, message("ss.annotations.freestring.var"));
				annotation.setTextAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES);
				annotation.registerFix(new AddDollarSignQuickFix(element));
				if (nextSibling != null) {
					ASTNode siblingNode = nextSibling.getNode();
					if (siblingNode == null ||
						(!siblingNode.getElementType().equals(SilverStripeTypes.DOT) &&
						!siblingNode.getElementType().equals(SilverStripeTypes.LEFT_PAREN))) {
						annotation.registerFix(new QuoteVariableQuickFix(element));
					}
				} else {
					annotation.registerFix(new QuoteVariableQuickFix(element));
				}
			}
		}
	}
}
