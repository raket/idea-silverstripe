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

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-10
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class AddDollarSignQuickFix extends BaseIntentionAction {
	private PsiElement element;

	AddDollarSignQuickFix(PsiElement element) {
		this.element = element;
	}

	@NotNull
	@Override
	public String getText() {
		return "Add $ sign to variable";
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
				PsiDocumentManager psiDocumentManager =  PsiDocumentManager.getInstance(project);
				editor.getDocument().insertString(startOffset, "$");
				psiDocumentManager.commitDocument(psiDocumentManager.getDocument(file));
			}
		});
	}
}