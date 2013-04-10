package com.raket.silverstripe.editor.annotations;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class QuoteVariableQuickFix extends BaseIntentionAction {
	private PsiElement element;

	QuoteVariableQuickFix(PsiElement element) {
		this.element = element;
	}

	@NotNull
	@Override
	public String getText() {
		return "Quote variable";
	}

	@NotNull
	@Override
	public String getFamilyName() {
		return "Variable quickfixes";
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		return true;
	}

	@Override
	public void invoke(@NotNull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
		ApplicationManager.getApplication().runWriteAction(new Runnable() {
			@Override
			public void run() {
				int startOffset = element.getTextOffset();
				int elementLength = element.getTextLength();
				PsiDocumentManager psiDocumentManager =  PsiDocumentManager.getInstance(project);
				editor.getDocument().insertString(startOffset+elementLength, "\"");
				editor.getDocument().insertString(startOffset, "\"");
				psiDocumentManager.commitDocument(psiDocumentManager.getDocument(file));
			}
		});
	}
}