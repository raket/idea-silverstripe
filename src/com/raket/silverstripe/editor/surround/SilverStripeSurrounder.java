package com.raket.silverstripe.editor.surround;

import com.intellij.lang.ASTNode;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SilverStripeSurrounder implements Surrounder {
	private final String description;
	private final String tagName;

	SilverStripeSurrounder(String description, String tagName) {
		this.description = description;
		this.tagName = tagName;
	}

	@Override
	public String getTemplateDescription() {
		return description;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isApplicable(@NotNull PsiElement[] elements) {
		return elements.length > 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Nullable
	@Override
	public TextRange surroundElements(@NotNull final Project project, @NotNull final Editor editor, @NotNull PsiElement[] elements) throws IncorrectOperationException {
		final int startPosition = editor.getSelectionModel().getSelectionStart();
		final int endPosition = editor.getSelectionModel().getSelectionEnd();

		ApplicationManager.getApplication().runWriteAction(new Runnable() {
			@Override
			public void run() {
				try {
					PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
					CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
					CaretModel caretModel = editor.getCaretModel();
					PsiElement blockElement;

					editor.getSelectionModel().removeSelection();
					psiDocumentManager.commitDocument(editor.getDocument());
					caretModel.moveToOffset(endPosition);
					EditorModificationUtil.insertStringAtCaret(editor, "\n<% end_"+tagName+" %>", true, false);
					caretModel.moveToOffset(startPosition);
					EditorModificationUtil.insertStringAtCaret(editor, "<% "+tagName+" $Var %>\n", true, false);
					psiDocumentManager.commitDocument(editor.getDocument());
					caretModel.moveToOffset(startPosition);
					blockElement = PsiUtilBase.getElementAtCaret(editor).getParent().getParent();
					ASTNode statementBlock = blockElement.getNode().findChildByType(TokenSet.create(SilverStripeTypes.SS_STATEMENTS));
					codeStyleManager.reformat(statementBlock.getPsi());
/*					codeStyleManager.adjustLineIndent(editor.getDocument(), editor.getDocument().getLineStartOffset(caretModel.getLogicalPosition().line));
					caretModel.moveToOffset(endPosition);
					codeStyleManager.adjustLineIndent(editor.getDocument(), editor.getDocument().getLineStartOffset(caretModel.getLogicalPosition().line));
					psiDocumentManager.commitDocument(editor.getDocument());*/
				} catch (Exception e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			}
		});

		return new TextRange(startPosition+5+tagName.length(),startPosition+8+tagName.length());  //To change body of implemented methods use File | Settings | File Templates.
	}
}