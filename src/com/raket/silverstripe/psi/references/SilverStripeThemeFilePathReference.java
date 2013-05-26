package com.raket.silverstripe.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
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
	private TextRange myTextRange = null;

	public SilverStripeThemeFilePathReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		myTextRange = textRange;
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
		if (myTextRange != null) {
			key = key.substring(0,  myTextRange.getEndOffset()-9);
		}
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
		ArrayList<VirtualFile> subDirs = getFileDirs();

		for (VirtualFile subDir : subDirs) {
		    VirtualFile resultFile = subDir.findFileByRelativePath(key);
			if (resultFile != null) {
				PsiFile psiFile = psiManager.findFile(resultFile);
				if (psiFile != null) {
					results.add(new PsiElementResolveResult(psiFile));
				}
				else {
					PsiDirectory psiDir = psiManager.findDirectory(resultFile);
					if (psiDir != null)
						results.add(new PsiElementResolveResult(psiDir));
				}
			}
		}
		return results.toArray(new ResolveResult[results.size()]);
	}

	@NotNull
	private ArrayList<VirtualFile> getFileDirs() {
		SilverStripeProjectComponent component = (SilverStripeProjectComponent) myElement.getProject().getComponent("SilverStripeProjectComponent");
		VirtualFile themeDir = component.getThemeDir();
		VirtualFile projectDir = component.getProjectDir();

		if (themeDir != null) {
			ArrayList<VirtualFile> subDirs = new ArrayList<VirtualFile>(Arrays.asList(themeDir.getChildren()));
			if (projectDir != null) {
				subDirs.add(projectDir);
			}
			return  subDirs;
		}

		return new ArrayList<VirtualFile>();
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@Override
	public TextRange getRangeInElement() {
		if (myTextRange != null)
			return  myTextRange;
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

	public List<LookupElement> getCompletions() {
		ArrayList<VirtualFile> subDirs = getFileDirs();
		String path = key.replace("IntellijIdeaRulezzz", "").trim();
		List<LookupElement> variants = new ArrayList<LookupElement>();
		PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
		String[] pathParts = path.split("/");
		if (pathParts.length > 0) {
			String searchPart = pathParts[pathParts.length-1];
			path = path.substring(0, path.length()-searchPart.length());
			for (VirtualFile subDir : subDirs) {
				VirtualFile resultFile = subDir.findFileByRelativePath(path);
				resultFile = (resultFile == null) ? subDir : resultFile;
				VirtualFile[] children = resultFile.getChildren();
				if (children != null) {
					for (VirtualFile child : children) {
						if (child.getName().contains(searchPart)) {
							PsiFile psiFile = psiManager.findFile(resultFile);
							if (psiFile != null) {
								variants.add(LookupElementBuilder.create(psiFile).
									withTypeText(child.getPath()));
							}
							else {
								PsiDirectory psiDir = psiManager.findDirectory(resultFile);
								if (psiDir != null) {
									variants.add(LookupElementBuilder.create(psiDir).
										withTypeText(child.getPath()));
								}
							}
						}
					}
					boolean gotHere = true;
				}
			}
		}
		return variants;
	}


}