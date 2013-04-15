package com.raket.silverstripe.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.psi.impl.SilverStripeRequireImpl;
import com.raket.silverstripe.psi.references.SilverStripeRequireReference;
import org.jetbrains.annotations.NotNull;
//import org.jetbrains.yaml.psi.impl.YAMLCompoundValueImpl;

public class SilverStripeRequirePsiReferenceProvider extends PsiReferenceProvider {
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

		// IF we touch down here, we've got a translation!
		if (element instanceof SilverStripeRequireImpl) {
			SilverStripeRequireImpl se = (SilverStripeRequireImpl) element;
			return new PsiReference[]{new SilverStripeRequireReference(se)};
		}

		//TwigViewPsiReference psiReference = new TwigViewPsiReference(se, element.getProject());

		return PsiReference.EMPTY_ARRAY;
	}
}