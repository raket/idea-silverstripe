package com.raket.silverstripe.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.raket.silverstripe.project.SilverStripeProjectComponent;
import com.raket.silverstripe.psi.SilverStripePsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SilverStripeThemeDirReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key = null;

	public SilverStripeThemeDirReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeThemeDirReference(@NotNull PsiNamedElement element) {
		super(element);
		key = element.getName();

	}

	@Override
	public TextRange getRangeInElement() {
		return new TextRange(0, this.getElement().getNode().getTextLength());
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		final Project project = myElement.getProject();
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		SilverStripeProjectComponent component = (SilverStripeProjectComponent) project.getComponent("SilverStripeProjectComponent");
		PsiManager psiManager = PsiManager.getInstance(project);
		VirtualFile themeDir = component.getThemeDir();
		VirtualFile projectDir = component.getProjectDir();
		if (themeDir != null) {
			VirtualFile[] subDirs = themeDir.getChildren();
			if (subDirs != null) {
				for (VirtualFile subDir : subDirs) {
					PsiDirectory foundDir = psiManager.findDirectory(subDir);
					if (foundDir != null)
						results.add(new PsiElementResolveResult(foundDir));
				}
			}
		}
		if (projectDir != null) {
			PsiDirectory foundProjectDir = psiManager.findDirectory(projectDir);
			if (foundProjectDir != null)
				results.add(new PsiElementResolveResult(foundProjectDir));
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
		return EMPTY_ARRAY;
	}
}
