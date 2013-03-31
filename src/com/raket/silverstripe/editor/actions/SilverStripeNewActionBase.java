package com.raket.silverstripe.editor.actions;

import com.intellij.CommonBundle;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
abstract class SilverStripeNewActionBase extends CreateElementActionBase {
	private static final Logger log = Logger.getInstance("#NewActionBase");

	public SilverStripeNewActionBase(String text, String description, Icon icon) {
		super(text, description, icon);
	}

	@NotNull
	protected final PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
		log.debug("invokeDialog");
		final MyInputValidator validator = new MyInputValidator(project, directory);
		Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

		final PsiElement[] elements = validator.getCreatedElements();
		log.debug("Result: " + elements);
		return elements;
	}

	public void update(final AnActionEvent event) {
		log.debug("update");
		super.update(event);

		final Presentation presentation = event.getPresentation();
		final DataContext context = event.getDataContext();
		Module module = (Module) context.getData(LangDataKeys.MODULE.getName());

		log.debug("update: module: " + module);

		final boolean hasModule = module != null;
		presentation.setEnabled(hasModule);
		presentation.setVisible(hasModule);
	}

	protected static PsiFile createFileFromTemplate(final PsiDirectory directory,
													String className,
													@NonNls String templateName,
													@NonNls String... parameters) throws IncorrectOperationException {
		log.debug("createFileFromTemplate");
		final String ext = "." + SilverStripeFileType.DEFAULT_EXTENSION;
		String filename = (className.endsWith(ext)) ? className : className + ext;
		return SilverStripeTemplatesFactory.createFromTemplate(directory, className, filename);
	}

	@NotNull
	protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
		log.debug("create " + newName + ", dir: " + directory);
		return doCreate(newName, directory);
	}

	@NotNull
	protected abstract PsiElement[] doCreate(String newName, PsiDirectory directory);

	protected abstract String getDialogPrompt();

	protected abstract String getDialogTitle();

	protected String getErrorTitle() {
		return CommonBundle.getErrorTitle();
	}

	protected void checkBeforeCreate(String newName, PsiDirectory directory) throws IncorrectOperationException {
		checkCreateFile(directory, newName);
	}

	public static void checkCreateFile(@NotNull PsiDirectory directory, String name) throws IncorrectOperationException {
		final String fileName = name + "." + SilverStripeFileType.DEFAULT_EXTENSION;
		directory.checkCreateFile(fileName);
	}
}