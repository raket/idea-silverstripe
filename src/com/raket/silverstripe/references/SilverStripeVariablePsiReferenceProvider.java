package com.raket.silverstripe.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.psi.impl.SilverStripeVariableImpl;
import com.raket.silverstripe.psi.references.SilverStripeVariableReference;
import org.jetbrains.annotations.NotNull;

public class SilverStripeVariablePsiReferenceProvider extends PsiReferenceProvider {
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

		// IF we touch down here, we've got a variable!
		if (element instanceof SilverStripeVariableImpl) {
			SilverStripeVariableImpl se = (SilverStripeVariableImpl) element;
			return new PsiReference[]{new SilverStripeVariableReference(se)};
		}
		return PsiReference.EMPTY_ARRAY;
	}
}