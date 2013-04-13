package com.raket.silverstripe.project;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.raket.silverstripe.eventdispatcher.EventDispatcher;
import com.raket.silverstripe.parser.events.SilverStripeBlockEndEventListener;
import com.raket.silverstripe.parser.events.SilverStripeBlockEventListener;
import com.raket.silverstripe.parser.events.SilverStripeBlockLevelEventListener;
import com.raket.silverstripe.parser.events.SilverStripeVariableEventListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: marcus
 * Date: 2013-04-12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeProjectComponent implements ProjectComponent {
	private String silverStripeVersion;
	private VirtualFile versionFile;
	private PsiFile psiVersionFile;
	private Project project;

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

	public void projectOpened() {
		// called when project is opened
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		SilverStripeVariableEventListener varEventListener = new SilverStripeVariableEventListener();
		SilverStripeBlockEventListener ifEventListener = new SilverStripeBlockEventListener();
		SilverStripeBlockEndEventListener endEventListener = new SilverStripeBlockEndEventListener();
		SilverStripeBlockLevelEventListener blockEventListener = new SilverStripeBlockLevelEventListener();
		dispatcher.addListeners("before_consume",
			varEventListener, ifEventListener, endEventListener
		);
		dispatcher.addListeners("after_consume",
			varEventListener, ifEventListener, endEventListener
		);
		dispatcher.addListener("block_start_statement_complete", blockEventListener);
		dispatcher.addListener("block_end_statement_complete", blockEventListener);

		VirtualFile versionFile = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
				+ File.separatorChar + "framework" + File.separatorChar + "silverstripe_version");
		if (versionFile != null) {
			this.versionFile = versionFile;
		}


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
