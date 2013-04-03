package com.raket.silverstripe.editor.completions;

import com.intellij.codeInsight.completion.CompletionConfidence;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThreeState;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: marcus
 * Date: 2013-04-03
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeCompletionConfidence extends CompletionConfidence {

	@NotNull
	@Override
	public ThreeState shouldFocusLookup(@NotNull CompletionParameters parameters) {
		return ThreeState.YES;
	}

	@NotNull
	@Override
	public ThreeState shouldSkipAutopopup(@NotNull PsiElement contextElement, @NotNull PsiFile psiFile, int offset) {
		return ThreeState.UNSURE;
	}
}
