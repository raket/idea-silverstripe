package com.raket.silverstripe.editor.actions;

import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeNewFileAction extends SilverStripeNewActionBase {

	public SilverStripeNewFileAction() {
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
	protected PsiElement[] doCreate(String newName, PsiDirectory directory) {
		PsiFile file = createFileFromTemplate(directory, newName, "template.ss");
		PsiElement child = file.getLastChild();
		return child != null ? new PsiElement[]{file, child} : new PsiElement[]{file};
	}
}