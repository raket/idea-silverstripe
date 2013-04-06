package com.raket.silverstripe.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.psi.references.SilverStripeYamlReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class SilverStripeYamlPsiReferenceProvider extends PsiReferenceProvider {
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

		// IF we touch down here, we've got YAML!
		YAMLKeyValueImpl se = (YAMLKeyValueImpl) element;
		if (se != null) {
			String name = se.getName();
			return new PsiReference[]{new SilverStripeYamlReference(se)};
		}
		//TwigViewPsiReference psiReference = new TwigViewPsiReference(se, element.getProject());

		return PsiReference.EMPTY_ARRAY;
	}
}
