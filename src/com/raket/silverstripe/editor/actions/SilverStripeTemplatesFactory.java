package com.raket.silverstripe.editor.actions;

import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.file.SilverStripeFileType;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeTemplatesFactory implements FileTemplateGroupDescriptorFactory {
	private static final String FILE_NAME = "bash-script.sh";
	private final FileTemplateGroupDescriptor templateGroup;
	private static final Logger log = Logger.getInstance("#BashTemplateFactory");

	public SilverStripeTemplatesFactory() {
		templateGroup = new FileTemplateGroupDescriptor(message("ss.file.template.group.title.silverstripe"), SilverStripeFileType.FILE_ICON);
		templateGroup.addTemplate(FILE_NAME);
	}

	public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
		return templateGroup;
	}

	public static PsiFile createFromTemplate(final PsiDirectory directory, final String name, String fileName) throws IncorrectOperationException {
		log.debug("createFromTemplate: dir:" + directory + ", filename: " + fileName);

		final String text = "#!/bin/sh\n";
		final PsiFileFactory factory = PsiFileFactory.getInstance(directory.getProject());

		log.debug("Create file from text");
		final PsiFile file = factory.createFileFromText(fileName, SilverStripeFileType.INSTANCE, "");

		log.debug("Adding file to directory");
		return (PsiFile) directory.add(file);
	}

}