package com.raket.silverstripe.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.impl.*;
import com.raket.silverstripe.psi.references.SilverStripeIncludeReference;
import com.raket.silverstripe.psi.references.SilverStripeRequireReference;
import com.raket.silverstripe.psi.references.SilverStripeThemeDirReference;
import com.raket.silverstripe.psi.references.SilverStripeThemeFilePathReference;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
//import org.jetbrains.yaml.psi.impl.YAMLCompoundValueImpl;

public class SilverStripeReferenceContributor extends PsiReferenceContributor {
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar) {

		registrar.registerReferenceProvider(PlatformPatterns.psiElement(SilverStripeTypes.SS_INCLUDE_STATEMENT).withLanguage(SilverStripeLanguage.INSTANCE),
			new PsiReferenceProvider() {
				@NotNull
				@Override
				public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
					SilverStripeIncludeImpl ssElement = (SilverStripeIncludeImpl) element;
					return new PsiReference[]{new SilverStripeIncludeReference(ssElement, ssElement.getReferenceTextRange())};
				}
			}
		);

		PsiElementPattern.Capture<SilverStripeRequireImpl> psiElementCapture = PlatformPatterns.psiElement(
			SilverStripeRequireImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			psiElementCapture,
			new SilverStripeRequirePsiReferenceProvider());

		PsiElementPattern.Capture<SilverStripeVariableImpl> variableCapture = PlatformPatterns.psiElement(
			SilverStripeVariableImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			variableCapture,
			new SilverStripeVariablePsiReferenceProvider());

		PsiElementPattern.Capture<SilverStripeFieldReferenceImpl> fieldCapture = PlatformPatterns.psiElement(
			SilverStripeFieldReferenceImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			fieldCapture,
			new SilverStripeVariablePsiReferenceProvider());

		PsiElementPattern.Capture<SilverStripeThemeDirImpl> themeDirCapture = PlatformPatterns.psiElement(
			SilverStripeThemeDirImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			themeDirCapture,
			new PsiReferenceProvider() {
				@NotNull
				@Override
				public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
					SilverStripeThemeDirImpl se = (SilverStripeThemeDirImpl) element;
					return new PsiReference[]{new SilverStripeThemeDirReference(se)};
				}
			});

		PsiElementPattern.Capture<SilverStripeThemeFilePathImpl> themePathCapture = PlatformPatterns.psiElement(
			SilverStripeThemeFilePathImpl.class).withLanguage(SilverStripeLanguage.INSTANCE);
		registrar.registerReferenceProvider(
			themePathCapture,
			new PsiReferenceProvider() {
				@NotNull
				@Override
				public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
					ArrayList<SilverStripeThemeFilePathReference> references = new ArrayList<SilverStripeThemeFilePathReference>();
					SilverStripeThemeFilePathImpl se = (SilverStripeThemeFilePathImpl) element;
					String filePath = se.getName();
					if (filePath != null) {
						String[] pathSegments = filePath.split("/");
						ArrayUtils.reverse(pathSegments);
						int startOffset = (filePath.length()+9);
						for (String pathSegment : pathSegments) {
							if (!pathSegment.isEmpty()) {
								references.add(new SilverStripeThemeFilePathReference(se, new TextRange(startOffset - pathSegment.length(), startOffset)));
								startOffset = (startOffset - pathSegment.length() - 1);
							} else {
								startOffset--;
							}
						}
						return references.toArray(new PsiReference[references.size()]);
					}
					return new PsiReference[]{new SilverStripeThemeFilePathReference(se)};
				}
			});
	}
}