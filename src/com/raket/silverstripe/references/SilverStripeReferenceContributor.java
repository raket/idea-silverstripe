package com.raket.silverstripe.references;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.references.SilverStripeReference;
import org.jetbrains.annotations.NotNull;

public class SilverStripeReferenceContributor extends PsiReferenceContributor {
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
		registrar.registerReferenceProvider(PlatformPatterns.psiElement(SilverStripeTypes.SS_INCLUDE_FILE).withLanguage(SilverStripeLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						IElementType nodeType = element.getNode().getElementType();
						if (nodeType == SilverStripeTypes.SS_INCLUDE_FILE) {
							return new PsiReference[]{new SilverStripeReference(element)};
						}
						return new PsiReference[0];
					}
				});
	}
}