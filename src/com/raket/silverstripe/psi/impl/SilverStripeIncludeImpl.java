package com.raket.silverstripe.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.psi.SilverStripeNamedElement;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SilverStripeIncludeImpl extends SilverStripeNamedElementImpl implements SilverStripeNamedElement {

	public SilverStripeIncludeImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		ASTNode keyNode = this.getNode().findChildByType(SilverStripeTypes.SS_INCLUDE_FILE);
		if (keyNode != null) {
			return keyNode.getPsi();
		} else {
			return null;
		}
	}

	public String getName() {
		ASTNode keyNode = this.getNode().findChildByType(SilverStripeTypes.SS_INCLUDE_FILE);
		if (keyNode != null) {
			return keyNode.getText();
		} else {
			return null;
		}
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
		return this;
	}

	/*
	@Override
	public PsiReference[] getReferences() {
		Project project = getProject();
		String key = this.getName();
		final List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project, key+"."+ SilverStripeFileType.DEFAULT_EXTENSION);
		List<PsiReference> results = new ArrayList<PsiReference>();
		for (SilverStripeFile property : properties) {
			results.add(new SilverStripeReference(property, new TextRange(0, property.getTextLength())));
		}
		PsiReference[] returnValues = results.toArray(new PsiReference[results.size()]);
		return results.toArray(new PsiReference[results.size()]);
	}

	@Override
	public PsiReference getReference() {
		PsiReference[] resolveResults = getReferences();
		return resolveResults.length == 1 ? resolveResults[0] : null;
	}*/
}
