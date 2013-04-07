package com.raket.silverstripe.editor.actions;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.raket.silverstripe.file.SilverStripeFileType;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.io.IOException;

import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-07
 * Time: 04:49
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeIncludeRefactorAction extends SilverStripeNewActionBase {
	private String selectedText = null;
	private SelectionModel selectonModel = null;
	private Editor editor = null;
	private PsiFile currentFile = null;

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
	protected PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
		final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
		descriptor.setRoots(project.getBaseDir());
		PsiDirectory currentDir = currentFile.getContainingDirectory();
		PsiDirectory includeDir = currentDir.findSubdirectory("Includes");
		VirtualFile selectDir = directory.getVirtualFile();
		if (includeDir == null) {
			PsiDirectory parentDir = currentDir.getParentDirectory();
			if (parentDir != null) {
				includeDir = parentDir.findSubdirectory("Includes");
			}
		}
		if (includeDir != null)
			selectDir = includeDir.getVirtualFile();

		final VirtualFile myFolder = FileChooser.chooseFile(descriptor, project, selectDir);
		if (myFolder != null) {
			PsiDirectory myDirectory = PsiManager.getInstance(project).findDirectory(myFolder);
			final MyInputValidator validator = new MyInputValidator(project, myDirectory);
			final String fileName = Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

			final PsiElement[] elements = validator.getCreatedElements();
			if (elements.length > 0) {
				ApplicationManager.getApplication().invokeLater(new Runnable() {
					@Override
					public void run() {
						ApplicationManager.getApplication().runWriteAction(new Runnable() {
							@Override
							public void run() {
								try {
									CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
									CaretModel caretModel = editor.getCaretModel();
									int caretOffset = selectonModel.getSelectionStart();
									EditorModificationUtil.deleteSelectedText(editor);
									caretModel.moveToOffset(caretOffset);
									int modOffset = EditorModificationUtil.insertStringAtCaret(editor, "<% include " + fileName + " %>", true, false);
									codeStyleManager.reformat(currentFile);
									//codeStyleManager.adjustLineIndent(currentFile, caretOffset);
									//caretModel.moveToOffset(caretOffset + modOffset);
									PsiFile createdFile = (PsiFile) elements[0];
									codeStyleManager.reformat(createdFile);
									FileEditorManager.getInstance(project).openFile(currentFile.getVirtualFile(), true);
									TextRange formatRange =  new TextRange(caretOffset,modOffset);
									//codeStyleManager.adjustLineIndent(currentFile, formatRange);
								} catch (Exception e) {
									e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
								}
							}
						});
					}
				});
			}
			return elements;
		}
		return PsiElement.EMPTY_ARRAY;
	}

	@NotNull
	protected PsiElement[] doCreate(String newName, PsiDirectory directory) {
		PsiFile file = SilverStripeTemplatesFactory.createFromTemplate(directory, newName, selectedText);
		PsiElement child = file.getLastChild();
		return child != null ? new PsiElement[]{file, child} : new PsiElement[]{file};
	}

	public void update(final AnActionEvent event) {
		super.update(event);

		final Presentation presentation = event.getPresentation();
		final DataContext context = event.getDataContext();
		Module module = (Module) context.getData(LangDataKeys.MODULE.getName());
		PsiFile currentFile = (PsiFile) context.getData(LangDataKeys.PSI_FILE.getName());
		Editor editor = (Editor)  context.getData(PlatformDataKeys.EDITOR.getName());
		//VirtualFile currentFile = (VirtualFile) context.getData(PlatformDataKeys.VIRTUAL_FILE.getName());
		if (currentFile != null) {
			boolean isSSFile = currentFile.getFileType().getDefaultExtension().equals(SilverStripeFileType.DEFAULT_EXTENSION);
			SelectionModel selectionModel = editor.getSelectionModel();
			String selectedText = selectionModel.getSelectedText();
			this.selectedText = selectedText;
			this.selectonModel = selectionModel;
			this.editor =  editor;
			this.currentFile = currentFile;
			if (selectedText == null) {
				presentation.setEnabled(false);
			}
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
