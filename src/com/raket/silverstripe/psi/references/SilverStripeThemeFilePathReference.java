package com.raket.silverstripe.psi.references;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.project.SilverStripeProjectComponent;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SilverStripeThemeFilePathReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key;

	public SilverStripeThemeFilePathReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeThemeFilePathReference(@NotNull PsiNamedElement element) {
		super(element);
		key = element.getName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
//		String[] pathParts = key.split(File.separator);
		Project project = myElement.getProject();
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		SilverStripeProjectComponent component = (SilverStripeProjectComponent) project.getComponent("SilverStripeProjectComponent");
		PsiManager psiManager = PsiManager.getInstance(project);
		VirtualFile themeDir = component.getThemeDir();
		VirtualFile projectDir = component.getProjectDir();
		if (themeDir != null) {
			ArrayList<VirtualFile> subDirs = new ArrayList<VirtualFile>(Arrays.asList(themeDir.getChildren()));
			if (projectDir != null)
				subDirs.add(projectDir);

			for (VirtualFile subDir : subDirs) {
			    VirtualFile resultFile = subDir.findFileByRelativePath(key);
				if (resultFile != null) {
					PsiFile psiFile = psiManager.findFile(resultFile);
					if (psiFile != null)
						results.add(new PsiElementResolveResult(psiFile));
				}
			}
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
		ASTNode parentNode = this.getElement().getNode();
		ASTNode requireFile = parentNode.findChildByType(SilverStripeTypes.SS_STRING);
		if (requireFile != null) {
			return new TextRange(requireFile.getPsi().getStartOffsetInParent(),
				requireFile.getPsi().getStartOffsetInParent()+requireFile.getPsi().getTextLength());
		}
		return new TextRange(0, parentNode.getTextLength());
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return EMPTY_ARRAY;
	}
}