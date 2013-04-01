package com.raket.silverstripe.editor.annotations;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CreateIncludeQuickFix extends BaseIntentionAction {
	private String key;

	CreateIncludeQuickFix(String key) {
		this.key = key;
	}

	@NotNull
	@Override
	public String getText() {
		return "Create Include File";
	}

	@NotNull
	@Override
	public String getFamilyName() {
		return "Include Files";
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		return true;
	}

	@Override
	public void invoke(@NotNull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
		ApplicationManager.getApplication().invokeLater(new Runnable() {
			@Override
			public void run() {
				final VirtualFile currentFile = file.getVirtualFile();
				PsiDirectory currentDir = file.getContainingDirectory();
				PsiDirectory includeDir = currentDir.findSubdirectory("Includes");
				VirtualFile selectDir = null;
				if (includeDir == null) {
					PsiDirectory parentDir = currentDir.getParentDirectory();
					if (parentDir != null) {
						includeDir = parentDir.findSubdirectory("Includes");
						currentDir = parentDir;
					}
				}
				if (includeDir != null)
					selectDir = includeDir.getVirtualFile();

				final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
				descriptor.setRoots(currentDir.getVirtualFile());
				final VirtualFile myFolder = FileChooser.chooseFile(descriptor, project, selectDir);

				if (myFolder != null) {

					ApplicationManager.getApplication().runWriteAction(new Runnable() {
						@Override
						public void run() {
							try {
								VirtualFile createdFile = myFolder.createChildData(this, key+"."+SilverStripeFileType.DEFAULT_EXTENSION);
								FileEditorManager.getInstance(project).openFile(createdFile, false);
								// Move back focus to the original file
								FileEditorManager.getInstance(project).openFile(currentFile, true);
								//OpenFileDescriptor editorOpenFile = new OpenFileDescriptor(project, createdFile, 0);
								/*
								PsiFileFactory.getInstance(project).
										createFileFromText(key, SilverStripeFileType.INSTANCE, "");*/
							} catch (IOException e) {
								e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
							}
						}
					});
				}
			}
		});
	}
}
