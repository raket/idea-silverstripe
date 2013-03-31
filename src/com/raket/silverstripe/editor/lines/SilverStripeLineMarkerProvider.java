package com.raket.silverstripe.editor.lines;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.file.SilverStripeFileType;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripePsiElement;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class SilverStripeLineMarkerProvider extends RelatedItemLineMarkerProvider {
	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
		if (element instanceof SilverStripePsiElement) {
			SilverStripePsiElement ssElement = (SilverStripePsiElement) element;
			IElementType nodeType = ssElement.getNode().getElementType();
			if (nodeType == SS_BLOCK_SIMPLE_STATEMENT) {
				ASTNode fileNode = ssElement.getNode().findChildByType(SS_BLOCK_VAR);
				if (fileNode != null) {
					String fileName = fileNode.getText();
					Project project = element.getProject();
					PsiFile currentFile = ssElement.getContainingFile();
					String fileExtension = currentFile.getFileType().getDefaultExtension();
					final List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project, fileName+"."+fileExtension);
					if (properties.size() > 0) {
						NavigationGutterIconBuilder<PsiElement> builder =
								NavigationGutterIconBuilder.create(SilverStripeFileType.FILE_ICON).
										setTargets(properties).
										setTooltipText("Navigate to include file");
						result.add(builder.createLineMarkerInfo(element));
					}
				}
			}
		}
	}
}