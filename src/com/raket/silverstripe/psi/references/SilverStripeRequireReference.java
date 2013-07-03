package com.raket.silverstripe.psi.references;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.css.CssFileType;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-06
 * Time: 03:19
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeRequireReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key = null;

	public SilverStripeRequireReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeRequireReference(@NotNull PsiNamedElement element) {
		super(element);
		key = element.getName();

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
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		Project project = myElement.getProject();
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		if (key == null || key.trim().equals("")) {
			return results.toArray(new ResolveResult[results.size()]);
		}
		String[] cssParts = key.split("\\.");
		String fileExtemsion = cssParts[cssParts.length-1];
		if (key != null && cssParts.length > 0 && (fileExtemsion.equals("css") || fileExtemsion.equals("js"))) {
			VirtualFile cssFile = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
				+ File.separatorChar + key);
			PsiFile cssPsiFile = PsiManager.getInstance(project).findFile(cssFile);
			results.add(new PsiElementResolveResult(cssPsiFile));
		}
		// We just have a name, no file ending
		else {
			List<PsiFile> cssFiles = SilverStripeFileUtil.findFiles(project, CssFileType.INSTANCE, key+".css");
			for (PsiFile cssFile : cssFiles) {
				results.add(new PsiElementResolveResult(cssFile));
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

	@NotNull
	@Override
	public Object[] getVariants() {
		return EMPTY_ARRAY;
	}
}
