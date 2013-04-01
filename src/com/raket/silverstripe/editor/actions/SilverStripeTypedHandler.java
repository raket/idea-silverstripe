package com.raket.silverstripe.editor.actions;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.file.SilverStripeFileViewProvider;
import com.raket.silverstripe.psi.SilverStripePsiElement;
import com.raket.silverstripe.psi.SilverStripePsiUtil;
import org.jetbrains.annotations.NotNull;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 00:24
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeTypedHandler extends TypedHandlerDelegate {
	/*
	@Override
	public Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
		//if (!(file instanceof SilverStripeFile)) return Result.CONTINUE;
		int offset = editor.getCaretModel().getOffset();

		String isInclude = editor.getDocument().getText(new TextRange(offset - 9, offset -1));
		if (isInclude.equals("include")) {
			CompletionAutoPopupHandler.invokeCompletion(CompletionType.BASIC, true, project, editor, 0, false);
			return Result.STOP;
		}

		return Result.CONTINUE;
	}*/

	@Override
	public TypedHandlerDelegate.Result charTyped(char c, Project project, Editor editor, @NotNull PsiFile file) {
		int offset = editor.getCaretModel().getOffset();
		FileViewProvider provider = file.getViewProvider();

		if (offset < 2 || offset > editor.getDocument().getTextLength()) {
			return TypedHandlerDelegate.Result.CONTINUE;
		}

		String previousChar = editor.getDocument().getText(new TextRange(offset - 2, offset - 1));
		String isInclude = editor.getDocument().getText(new TextRange(offset - 8, offset - 1));

		if (file.getViewProvider() instanceof SilverStripeFileViewProvider) {
			// if we're looking at a close stache, we may have some business too attend to
			if (c == '>' && previousChar.equals("%")) {
				autoInsertCloseTag(project, offset, editor, provider);
				adjustMustacheFormatting(project, offset, editor, file, provider);
			}

			if (c == '%' && previousChar.equals("<")) {

			}
			if (isInclude.equals("include")) { /*
				ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(file.getVirtualFile());
				CodeCompletionHandlerBase codeCompleter = new CodeCompletionHandlerBase(CompletionType.BASIC);
				codeCompleter.invokeCompletion(project, editor); */
			}
			/*
			EditorEventMulticaster.add  */
		}

		return TypedHandlerDelegate.Result.CONTINUE;
	}

	/**
	 * When appropriate, auto-inserts Handlebars close tags.  i.e.  When "{{#tagId}}" or "{{^tagId}} is typed,
	 *      {{/tagId}} is automatically inserted
	 */
	private void autoInsertCloseTag(Project project, int offset, Editor editor, FileViewProvider provider) {

		PsiDocumentManager.getInstance(project).commitAllDocuments();

		PsiElement elementAtCaret = provider.findElementAt(offset - 1, SilverStripeLanguage.class);

		PsiElement openTag = SilverStripePsiUtil.findParentOpenTagElement(elementAtCaret);
		if (openTag != null && openTag.getChildren().length > 1) {
			// we've got an open block type stache... find its ID
			SilverStripePsiElement idElem = (SilverStripePsiElement) openTag.getChildren()[1];
			IElementType nodeType = idElem.getNode().getElementType();
			if (idElem != null
					&& (nodeType == SS_START_KEYWORD || nodeType == SS_IF_KEYWORD)) {
				// insert the corresponding close tag
				editor.getDocument().insertString(offset, "<% end_" + idElem.getText() + " %>");
			}
		}
	}

	/**
	 * When appropriate, adjusts the formatting for some 'staches, particularily close 'staches
	 *  and simple inverses ("{{^}}" and "{{else}}")
	 */
	private void adjustMustacheFormatting(Project project, int offset, Editor editor, PsiFile file, FileViewProvider provider) {

		/**/
		PsiElement elementAtCaret = provider.findElementAt(offset - 1, SilverStripeLanguage.class);
		PsiElement closeOrSimpleInverseParent = PsiTreeUtil.findFirstParent(elementAtCaret, true, new Condition<PsiElement>() {
			@Override
			public boolean value(PsiElement element) {
				if (element == null || element.getNode() == null) return false;
				IElementType nodeType = element.getNode().getElementType();
				return (nodeType == SS_ELSE_IF_STATEMENT
						|| nodeType == SS_ELSE_STATEMENT
						|| nodeType == SS_BLOCK_END_STATEMENT);
			}
		});

		// run the formatter if the user just completed typing a SIMPLE_INVERSE or a CLOSE_BLOCK_STACHE
		if (closeOrSimpleInverseParent != null) {
			// grab the current caret position (AutoIndentLinesHandler is about to mess with it)
			PsiDocumentManager.getInstance(project).commitAllDocuments();
			CaretModel caretModel = editor.getCaretModel();
			CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
			codeStyleManager.adjustLineIndent(file, editor.getDocument().getLineStartOffset(caretModel.getLogicalPosition().line));
		}
	}
}