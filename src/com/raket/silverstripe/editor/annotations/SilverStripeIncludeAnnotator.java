package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.impl.SilverStripeIncludeImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.raket.silverstripe.SilverStripeBundle.message;
public class SilverStripeIncludeAnnotator implements Annotator {
	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element instanceof SilverStripeIncludeImpl) {
			SilverStripeIncludeImpl ssElement = (SilverStripeIncludeImpl) element;
			String fileName = ssElement.getName();
			Project project = element.getProject();
			PsiFile currentFile = ssElement.getContainingFile();
			String fileExtension = currentFile.getFileType().getDefaultExtension();
			String fullFileName = fileName + "." + fileExtension;
			List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project, fullFileName);
			TextRange range = new TextRange(element.getTextRange().getStartOffset(),
					element.getTextRange().getStartOffset());
			if (properties.size() >= 1) {
				Annotation annotation = holder.createInfoAnnotation(ssElement.getNode(), null);
				//annotation.setTextAttributes(SyntaxHighlighterColors.LINE_COMMENT);
			}
			if (properties.size() == 0) {
				Annotation annotation = holder
						.createErrorAnnotation(ssElement.getNameIdentifier(), message("ss.annotations.file.not.found", fullFileName));
				annotation.setTextAttributes(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
				annotation.registerFix(new CreateIncludeQuickFix(fileName));
			}
		}
	}
}