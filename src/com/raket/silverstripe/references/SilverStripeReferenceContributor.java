package com.raket.silverstripe.references;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.impl.SilverStripeFieldReferenceImpl;
import com.raket.silverstripe.psi.impl.SilverStripeIncludeImpl;
import com.raket.silverstripe.psi.impl.SilverStripeRequireImpl;
import com.raket.silverstripe.psi.impl.SilverStripeVariableImpl;
import com.raket.silverstripe.psi.references.SilverStripeIncludeReference;
import org.jetbrains.annotations.NotNull;
//import org.jetbrains.yaml.psi.impl.YAMLCompoundValueImpl;

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
						return new PsiReference[]{new SilverStripeIncludeReference(ssElement, ssElement.getReferenceTextRange())};
					}
					return new PsiReference[0];
				}
			}
		);

		PsiElementPattern.Capture<SilverStripeRequireImpl> psiElementCapture = PlatformPatterns.psiElement(
			SilverStripeRequireImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			psiElementCapture,
			new SilverStripeRequirePsiReferenceProvider());

		PsiElementPattern.Capture<SilverStripeVariableImpl> variableCapture = PlatformPatterns.psiElement(
			SilverStripeVariableImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			variableCapture,
			new SilverStripeVariablePsiReferenceProvider());

		PsiElementPattern.Capture<SilverStripeFieldReferenceImpl> fieldCapture = PlatformPatterns.psiElement(
			SilverStripeFieldReferenceImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			fieldCapture,
			new SilverStripeVariablePsiReferenceProvider());
	}
}