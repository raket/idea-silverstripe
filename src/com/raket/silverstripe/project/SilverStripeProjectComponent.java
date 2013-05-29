package com.raket.silverstripe.project;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Variable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;


public class SilverStripeProjectComponent implements ProjectComponent {
	private String silverStripeVersion;
	private VirtualFile versionFile;
	private VirtualFile frameworkDir;
	private VirtualFile cmsDir;
	private VirtualFile projectDir = null;
	private PsiFile psiVersionFile;
	private Project project;
	private VirtualFile themeDir;

	public SilverStripeProjectComponent(Project project) {
		this.project = project;
	}

	public void initComponent() {
		// TODO: insert component initialization logic here
	}

	public void disposeComponent() {
		// TODO: insert component disposal logic here
	}

	@NotNull
	public String getComponentName() {
		return "SilverStripeProjectComponent";
	}

	public VirtualFile getFrameworkDir() {
		return frameworkDir;
	}

	public VirtualFile getCmsDir() {
		return cmsDir;
	}

	public VirtualFile getThemeDir() {
		return themeDir;
	}

	public void setProjectDir(VirtualFile dir) {
		projectDir = dir;
	}

	public VirtualFile getProjectDir() {
		if (projectDir == null) {
			Collection<Variable> projectVariables = PhpIndex.getInstance(project).getVariablesByName("project");
			for (Variable variable : projectVariables) {
				PsiFile variableFile = variable.getContainingFile();
				PsiDirectory dir = variableFile.getContainingDirectory();
				String dirName = variable.getParent().getLastChild().getText();
				PsiDirectory parentDir = dir;
				String parentDirName = "";
				int depth = 0;
				while (parentDir != null && depth < 2) {
					parentDirName = parentDir.getName();
					if (parentDirName.equals("tests")) {
						break;
					}
					parentDir = parentDir.getParentDirectory();
					depth++;
				}
				if (parentDirName.equals("tests")) {
					continue;
				}
				if (!dirName.equals(";") && dirName.length() > 0) {
					assert dir != null;
					projectDir = dir.getVirtualFile();
					break;
				}
			}
		}
		return projectDir;
	}

	public void projectOpened() {
		// called when project is opened
		VirtualFile versionFile = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
				+ File.separatorChar + "framework" + File.separatorChar + "silverstripe_version");
		if (versionFile != null) {
			this.versionFile = versionFile;
		}

		frameworkDir = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
			+ File.separatorChar + "framework");
		cmsDir = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
			+ File.separatorChar + "cms");
		themeDir = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
			+ File.separatorChar + "themes");


	}

	public void projectClosed() {
		// called when project is being closed
	}

	public String getSilverStripeVersion() {
		if (versionFile != null && psiVersionFile == null) {
			psiVersionFile = PsiManager.getInstance(project).findFile(versionFile);
			silverStripeVersion = psiVersionFile.getText().trim();
		}
		return silverStripeVersion;
	}
}
