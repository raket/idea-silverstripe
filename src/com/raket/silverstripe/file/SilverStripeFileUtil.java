package com.raket.silverstripe.file;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.raket.silverstripe.psi.SilverStripeFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SilverStripeFileUtil {
	public static List<SilverStripeFile> findFiles(Project project, String key) {
		List<SilverStripeFile> result = null;
		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SilverStripeFileType.INSTANCE,
				GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			SilverStripeFile simpleFile = (SilverStripeFile) PsiManager.getInstance(project).findFile(virtualFile);
			String fileName = simpleFile.getName();
			if (simpleFile != null) {
				if (result == null) {
					result = new ArrayList<SilverStripeFile>();
				}
				if (simpleFile.getName().matches(key)) {
					result.add(simpleFile);
				}
			}
		}
		return result != null ? result : Collections.<SilverStripeFile>emptyList();
	}

	public static List<SilverStripeFile> findFiles(Project project) {
		List<SilverStripeFile> result = new ArrayList<SilverStripeFile>();
		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SilverStripeFileType.INSTANCE,
				GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			SilverStripeFile simpleFile = (SilverStripeFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (simpleFile != null) {
				Collections.addAll(result, simpleFile);
			}
		}
		return result;
	}
}
