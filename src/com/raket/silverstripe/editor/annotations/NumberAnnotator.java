package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-12
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public class NumberAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == SilverStripeTypes.NUMBER) {
			Annotation annotation = holder
				.createWarningAnnotation(element, message("ss.annotations.number.string"));
		}
	}
}