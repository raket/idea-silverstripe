package com.raket.silverstripe.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.raket.silverstripe.psi.SilverStripeNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SilverStripeNamedElementImpl extends ASTWrapperPsiElement implements SilverStripeNamedElement {
	public SilverStripeNamedElementImpl(@NotNull ASTNode node) {
		super(node);
	}
}