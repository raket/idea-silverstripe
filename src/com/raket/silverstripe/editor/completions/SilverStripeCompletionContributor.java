package com.raket.silverstripe.editor.completions;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.file.SilverStripeFileType;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.SilverStripeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SilverStripeCompletionContributor extends CompletionContributor {
	public SilverStripeCompletionContributor() {
		extend(CompletionType.BASIC,
				PlatformPatterns.psiElement(SilverStripeTypes.SS_INCLUDE_FILE).withLanguage(SilverStripeLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {
						PsiFile currentFile = parameters.getOriginalFile();
						Project project = currentFile.getProject();
						List<SilverStripeFile> files = SilverStripeFileUtil.findFilesByDir(project, "Includes");
						for (SilverStripeFile file : files) {
							String fileName = file.getName();
							fileName = fileName.substring(0, fileName.length()-3);
							resultSet.addElement(LookupElementBuilder.create(" "+fileName).withIcon(SilverStripeFileType.FILE_ICON)
								.withTypeText(file.getName()));
						}
					}
				}
		);
	}

	public void fillCompletionVariants(final CompletionParameters parameters, CompletionResultSet result) {
		super.fillCompletionVariants(parameters, result);
	}
}