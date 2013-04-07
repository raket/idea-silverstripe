package com.raket.silverstripe.references;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class SilverStripeYamlReferenceContributor extends PsiReferenceContributor {
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
		PsiElementPattern.Capture<YAMLKeyValueImpl> psiElementCapture = PlatformPatterns.psiElement(
				YAMLKeyValueImpl.class);

		registrar.registerReferenceProvider(
				psiElementCapture,
				new SilverStripeYamlPsiReferenceProvider(),
				PsiReferenceRegistrar.DEFAULT_PRIORITY);
	}
}
