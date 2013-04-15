package com.raket.silverstripe.file;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.*;
import com.intellij.util.indexing.FileBasedIndex;
import com.raket.silverstripe.psi.SilverStripeFile;
import com.raket.silverstripe.psi.impl.SilverStripeTranslationImpl;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SilverStripeFileUtil {
	private static final Key<CachedValue<PsiFile>> SILVER_STRIPE_VERSION = new Key<CachedValue<PsiFile>>("SILVER_STRIPE_VERSION");
	public static String VERSION_STRING = "latest";

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

	public static List<PsiFile> findFiles(Project project, FileType fileType, String key) {
		List<PsiFile> result =  new ArrayList<PsiFile>();
		List<PsiFile> files = findFiles(project, fileType);
		for (PsiFile file : files) {
			if (file.getName().matches(key)) {
				result.add(file);
			}
		}
		return result;
	}

	public static List<PsiFile> findFiles(Project project, FileType fileType) {
		List<PsiFile> result = new ArrayList<PsiFile>();
		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, fileType,
			GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			PsiFile simpleFile = PsiManager.getInstance(project).findFile(virtualFile);
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

	public static List<SilverStripeTranslationImpl> findTranslations(Project project, String key) {
		List<SilverStripeTranslationImpl> result = new ArrayList<SilverStripeTranslationImpl>();
		Collection<SilverStripeFile> virtualFiles = findFiles(project);
		for (SilverStripeFile virtualFile : virtualFiles) {
			PsiElement[] translations = PsiTreeUtil.collectElements(virtualFile, new PsiElementFilter() {
				@Override
				public boolean isAccepted(PsiElement element) {
					return element instanceof SilverStripeTranslationImpl;
				}
			});

			for (PsiElement translation : translations) {
				SilverStripeTranslationImpl realTranslation = (SilverStripeTranslationImpl) translation;
				if (key.equals(realTranslation.getName())) {
					result.add(realTranslation);
				}
			}
		}
		return result;
	}

	public static PsiFile getVersion(final Project project) {
		CachedValue<PsiFile> ssVersion = project.getUserData(SILVER_STRIPE_VERSION);
		if (ssVersion == null) {
			ssVersion = CachedValuesManager.getManager(project).createCachedValue(
				new CachedValueProvider<PsiFile>() {
					@Nullable
					@Override
					public Result<PsiFile> compute() {
						VirtualFile versionFile = LocalFileSystem.getInstance().findFileByPath(project.getBasePath()
							+ File.separatorChar + "framework" + File.separatorChar + "silverstripe_version");
						PsiFile contents = PsiManager.getInstance(project).findFile(versionFile);
						return CachedValueProvider.Result.create(contents);
					}
				}
			);
			project.putUserData(SILVER_STRIPE_VERSION, ssVersion);
		}
		return ssVersion.getValue();
	}
}
