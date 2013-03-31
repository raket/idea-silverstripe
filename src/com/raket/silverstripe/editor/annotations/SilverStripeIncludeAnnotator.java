package com.raket.silverstripe.editor.annotations;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripePsiElement;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.SilverStripeBundle.message;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public class SilverStripeIncludeAnnotator implements Annotator {
	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element instanceof SilverStripePsiElement) {
			SilverStripePsiElement ssElement = (SilverStripePsiElement) element;
			IElementType nodeType = ssElement.getNode().getElementType();
			if (nodeType == SS_INCLUDE_FILE) {
				String fileName = ssElement.getText();
				Project project = element.getProject();
				PsiFile currentFile = ssElement.getContainingFile();
				String fileExtension = currentFile.getFileType().getDefaultExtension();
				String fullFileName = fileName + "." + fileExtension;
				List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project, fullFileName);
				TextRange range = new TextRange(element.getTextRange().getStartOffset(),
						element.getTextRange().getEndOffset());
				/*if (properties.size() >= 1) {
					Annotation annotation = holder.createInfoAnnotation(range, null);
					annotation.setTextAttributes(SyntaxHighlighterColors.LINE_COMMENT);
				} */
				if (properties.size() == 0) {
					Annotation annotation = holder.createErrorAnnotation(ssElement.getNode(), message("ss.annotations.file.not.found", fullFileName));
					//annotation.setTextAttributes(CodeInsightColors.WARNINGS_ATTRIBUTES);
				}
			}
		}
	}
}