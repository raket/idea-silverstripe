package com.raket.silverstripe.editor.annotations;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-10
 * Time: 21:14
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeComparisonAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == SilverStripeTypes.SS_COMPARISON_OPERATOR) {
			final Project project = element.getProject();
			String varValue = element.getText();
			if (varValue.startsWith("<") || varValue.startsWith(">")) {
				PsiFile version = SilverStripeFileUtil.getVersion(project);
				String content = version.getText();
				if (content != null && !content.trim().isEmpty()) {
					String versionString = content;
					String[] parts = versionString.split("\\.");
					boolean versionBool = false;
					if (parts.length > 0) {
						int majorVersion = Integer.parseInt(parts[0]);
						int minorVersion = Integer.parseInt(parts[1]);
						if (majorVersion < 3 || (majorVersion == 3 && minorVersion < 1)) {
							holder.createErrorAnnotation(element, message("ss.annotations.comparison.not.supported"));
						}
					}
				}
			}
		}
	}
}