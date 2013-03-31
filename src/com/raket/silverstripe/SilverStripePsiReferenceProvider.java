package com.raket.silverstripe;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-30
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripePsiReferenceProvider extends PsiReferenceProvider {

	public static final PsiReferenceProvider[] EMPTY_ARRAY = new PsiReferenceProvider[0];
	public static String projectPath;
	public static Project project;
	public static PropertiesComponent properties;

	public SilverStripePsiReferenceProvider() {
	}

	/**
	 * Return reference or empty array
	 *
	 * @param element PsiElement
	 * @param context ProcessingContext
	 * @return PsiReference[]
	 */
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull final ProcessingContext context) {
		project = element.getProject();
		String elname = element.getClass().getName();
		properties = PropertiesComponent.getInstance(project);
		if (elname.endsWith("StringLiteralExpressionImpl")) {

			try {
				PsiFile file = element.getContainingFile();
				String path = file.getVirtualFile().getPath();
				projectPath = project.getBasePath().replace("\\", "/");
				/*
				int ProviderType = YiiRefsHelper.getYiiObjectType(path, element);
				switch (ProviderType) {
					case YiiRefsHelper.YII_TYPE_CONTROLLER_TO_VIEW_RENDER:
						return ControllerRenderViewReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_AR_RELATION:
						return ARRelationReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_VIEW_TO_VIEW_RENDER:
						return ViewRenderViewReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_WIDGET_CALL:
						return WidgetCallReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_CACTION_TO_VIEW_RENDER:
						return CActionRenderViewReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_WIDGET_VIEW_RENDER:
						return WidgetRenderViewReferenceProvider.getReference(path, element);
					case YiiRefsHelper.YII_TYPE_CONTROLLER_ACTIONS_CACTION:
						return ControlleActionsClassReferenceProvider.getReference(path, element);
				}
				*/
			} catch (Exception e) {
				//System.err.println("error" + e.getMessage());
			}
		}
		return PsiReference.EMPTY_ARRAY;
	}


}