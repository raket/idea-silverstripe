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
		List<SilverStripeFile> result =  new ArrayList<SilverStripeFile>();
		List<SilverStripeFile> files = findFiles(project);
		for (SilverStripeFile file : files) {
			if (file.getName().matches(key)) {
				result.add(file);
			}
		}
		return result;
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

	public static List<SilverStripeFile> findFilesByDir(Project project, String key) {
		List<SilverStripeFile> result =  new ArrayList<SilverStripeFile>();
		List<SilverStripeFile> files = findFiles(project);
		for (SilverStripeFile file : files) {
			if (file.getContainingDirectory().getName().matches(key)) {
				result.add(file);
			}
		}
		return result;
	}

}
