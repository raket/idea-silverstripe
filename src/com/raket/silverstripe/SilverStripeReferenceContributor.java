package com.raket.silverstripe;

import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeReferenceContributor extends PsiReferenceContributor {
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
		SilverStripePsiReferenceProvider provider = new SilverStripePsiReferenceProvider();

		//registrar.registerReferenceProvider(StandardPatterns.instanceOf(XmlAttributeValue.class), provider);
		//registrar.registerReferenceProvider(StandardPatterns.instanceOf(XmlTag.class), provider);

		registrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiElement.class), provider);
	}
}