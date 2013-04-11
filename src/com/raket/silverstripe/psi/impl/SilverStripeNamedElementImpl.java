package com.raket.silverstripe.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.raket.silverstripe.psi.SilverStripeNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SilverStripeNamedElementImpl extends ASTWrapperPsiElement implements SilverStripeNamedElement {
	public SilverStripeNamedElementImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public PsiReference[] getReferences() {
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	@Override
	public PsiReference getReference() {
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}
}