package com.raket.silverstripe.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.raket.silverstripe.file.SilverStripeFileType;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SilverStripeReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key;

	public SilverStripeReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeReference(@NotNull PsiNamedElement element) {
		super(element);
		key = element.getName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {

		Project project = myElement.getProject();
		final List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project, key+".ss");
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		for (SilverStripeFile property : properties) {
			results.add(new PsiElementResolveResult(property));
		}
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
		Project project = myElement.getProject();
		List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project);
		List<LookupElement> variants = new ArrayList<LookupElement>();
		for (final SilverStripeFile property : properties) {
			variants.add(LookupElementBuilder.create(property).
					withIcon(SilverStripeFileType.FILE_ICON).
					withTypeText(property.getContainingFile().getName())
			);
		}
		return variants.toArray();
	}
}
