package com.raket.silverstripe.editor.actions;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-07
 * Time: 04:49
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeIncludeRefactorAction extends SilverStripeNewActionBase {
	public SilverStripeIncludeRefactorAction() {
		super(message("ss.newfile.menu.action.text"),
				message("ss.newfile.menu.action.description"), IconLoader.getIcon("/icons/silverstripe.png"));
	}

	protected String getDialogPrompt() {
		return message("ss.newfile.dialog.prompt");
	}

	protected String getDialogTitle() {
		return message("ss.newfile.dialog.title");
	}

	protected String getCommandName() {
		return message("ss.newfile.command.name");
	}

	protected String getActionName(PsiDirectory directory, String newName) {
		return message("ss.newfile.menu.action.text");
	}

	@NotNull
	@Override
	protected PsiElement[] doCreate(String newName, PsiDirectory directory) {
		return new PsiElement[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void update(final AnActionEvent event) {
		super.update(event);

		final Presentation presentation = event.getPresentation();
		final DataContext context = event.getDataContext();
		Module module = (Module) context.getData(LangDataKeys.MODULE.getName());
		PsiFile currentFile = (PsiFile) context.getData(LangDataKeys.PSI_FILE.getName());
		if (currentFile != null) {
			boolean isSSFile = currentFile.getFileType().getDefaultExtension().equals(SilverStripeFileType.DEFAULT_EXTENSION);

			if (!isSSFile) {
				presentation.setEnabled(false);
				presentation.setVisible(false);
			}
		}
		else {
			presentation.setEnabled(false);
			presentation.setVisible(false);
		}
	}
}
