package com.raket.silverstripe.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.psi.impl.SilverStripeTranslationImpl;
import org.jetbrains.annotations.NotNull;
//import org.jetbrains.yaml.psi.impl.YAMLCompoundValueImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-06
 * Time: 02:47
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeTranslationPsiReferenceProvider extends PsiReferenceProvider {
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

		// IF we touch down here, we've got a translation!
		if (element instanceof SilverStripeTranslationImpl) {
			SilverStripeTranslationImpl se = (SilverStripeTranslationImpl) element;
		}

		//TwigViewPsiReference psiReference = new TwigViewPsiReference(se, element.getProject());

		return PsiReference.EMPTY_ARRAY;
	}
}