package com.raket.silverstripe.editor.surround;

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NotNull;

public class SilverStripeSurroundDescriptor implements SurroundDescriptor {
	private static final Surrounder[] SURROUNDERS = {
		new SilverStripeSurrounder("Surround with \"if\" block", "if"),
		new SilverStripeSurrounder("Surround with \"loop\" block", "loop"),
		new SilverStripeSurrounder("Surround with \"with\" block", "with")
	};


	@NotNull
	@Override
	public PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
		FileViewProvider viewProvider = file.getViewProvider();
		PsiElement element1 = viewProvider.findElementAt(startOffset, SilverStripeLanguage.INSTANCE);
		PsiElement element2 = viewProvider.findElementAt(endOffset - 1, SilverStripeLanguage.INSTANCE);
		if (element1 instanceof PsiWhiteSpace) {
			startOffset = element1.getTextRange().getEndOffset();
			element1 = file.findElementAt(startOffset);
		}
		if (element2 instanceof PsiWhiteSpace) {
			endOffset = element2.getTextRange().getStartOffset();
			element2 = file.findElementAt(endOffset - 1);
		}
		if (element1 == null || element2 == null) return PsiElement.EMPTY_ARRAY;
		return new PsiElement[]{element1, element2};
/*		PsiElement parent = PsiTreeUtil.findCommonParent(element1, element2);
		if (parent != null && parent.getNode().getElementType().equals(SilverStripeTypes.SS_BLOCK_STATEMENT))
			return new PsiElement[]{parent};

		List<PsiElement> result = new ArrayList<PsiElement>();
		while(true) {
			result.add(element1);
			if (element1 == element2) break;
			element1 = element1.getNextSibling();
			if (element1 == null) break;
		}
		return PsiUtilBase.toPsiElementArray(result);*/
	}

	@NotNull
	@Override
	public Surrounder[] getSurrounders() {
		return SURROUNDERS;
	}

	@Override
	public boolean isExclusive() {
		return false;
	}
}
