package com.raket.silverstripe.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.psi.SilverStripeNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SilverStripeVariableImpl extends SilverStripeNamedElementImpl implements SilverStripeNamedElement {

	public SilverStripeVariableImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		ASTNode keyNode = this.getNode();
		if (keyNode != null) {
			return keyNode.getPsi();
		} else {
			return null;
		}
	}

	public String getName() {
		ASTNode keyNode = this.getNode();
		if (keyNode != null) {
			if (keyNode.getText().startsWith("$"))
				return keyNode.getText().substring(1);
			else
				return keyNode.getText();
		} else {
			return null;
		}
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
		return this;
	}
}