package com.raket.silverstripe.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.util.*;
import com.jetbrains.php.PhpCaches;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class SilverStripeVariableReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private static final Key<CachedValue<PsiElement[]>> PHP_METHODS = new Key<CachedValue<PsiElement[]>>("PHP_METHODS");
	private String key = null;

	public SilverStripeVariableReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeVariableReference(@NotNull PsiNamedElement element) {
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
		final String key = this.key;
		final Project project = myElement.getProject();
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		PhpIndex  phpIndex = PhpIndex.getInstance(project);
		Collection<PhpClass> classes = phpIndex.getAllSubclasses("Object");
		Collection<PhpClass> extensionClasses = phpIndex.getAllSubclasses("Extension");
		classes.addAll(extensionClasses);

		for (PhpClass phpClass : classes) {
			Method phpMethod = phpClass.findOwnMethodByName(key);
			if (phpMethod == null) phpMethod = phpClass.findOwnMethodByName("get"+key);
			if (phpMethod != null) {
				results.add(new PsiElementResolveResult(phpMethod));
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