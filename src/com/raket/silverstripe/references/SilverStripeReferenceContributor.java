package com.raket.silverstripe.references;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.impl.SilverStripeIncludeImpl;
import com.raket.silverstripe.psi.references.SilverStripeReference;
import org.jetbrains.annotations.NotNull;

public class SilverStripeReferenceContributor extends PsiReferenceContributor {
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
		registrar.registerReferenceProvider(PlatformPatterns.psiElement(SilverStripeTypes.SS_INCLUDE_STATEMENT).withLanguage(SilverStripeLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						SilverStripeIncludeImpl ssElement = (SilverStripeIncludeImpl) element;
						if (ssElement != null) {
							return new PsiReference[]{new SilverStripeReference(ssElement, ssElement.getReferenceTextRange())};
						}
						return new PsiReference[0];
					}
				});
	}
}