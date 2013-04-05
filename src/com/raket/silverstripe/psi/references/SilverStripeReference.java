package com.raket.silverstripe.psi.references;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripeTypes;
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

	@Override
	public TextRange getRangeInElement() {
		TextRange origRange = super.getRangeInElement();
		ASTNode parentNode = this.getElement().getNode();
		ASTNode includeKeyword = parentNode.findChildByType(SilverStripeTypes.SS_INCLUDE_KEYWORD);
		ASTNode includeFile = parentNode.findChildByType(SilverStripeTypes.SS_INCLUDE_FILE);
		int startOffset = includeKeyword.getPsi().getStartOffsetInParent()+includeKeyword.getPsi().getTextLength();
		if (includeFile != null) {
			return new TextRange(includeFile.getPsi().getStartOffsetInParent(),
					includeFile.getPsi().getStartOffsetInParent()+includeFile.getPsi().getTextLength());
		}
		return new TextRange(startOffset, origRange.getEndOffset());
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return EMPTY_ARRAY;
		/*
		Project project = myElement.getProject();
		List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project);
		List<LookupElement> variants = new ArrayList<LookupElement>();
		for (final SilverStripeFile property : properties) {
			variants.add(LookupElementBuilder.create(property).
					withIcon(SilverStripeFileType.FILE_ICON).
					withTypeText(property.getContainingFile().getName())
			);
		}
		return variants.toArray();*/
	}
}
