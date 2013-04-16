package com.raket.silverstripe.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.raket.silverstripe.psi.SilverStripePsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SilverStripeFieldReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key = null;

	public SilverStripeFieldReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeFieldReference(@NotNull PsiNamedElement element) {
		super(element);
		key = element.getName();

	}

	@Override
	public TextRange getRangeInElement() {
		return new TextRange(this.getElement().getNode().getLastChildNode().getPsi().getStartOffsetInParent(), this.getElement().getNode().getTextLength());
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		final String key = this.key;
		final Project project = myElement.getProject();
		List<ResolveResult> results = SilverStripePsiUtil.getFieldMethodResolverResults(project, key);
		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return EMPTY_ARRAY;
	}
}