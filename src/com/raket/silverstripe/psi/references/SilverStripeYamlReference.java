package com.raket.silverstripe.psi.references;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.raket.silverstripe.file.SilverStripeFileUtil;
import com.raket.silverstripe.psi.impl.SilverStripeTranslationImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLElementTypes;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-06
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeYamlReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String key = null;

	public SilverStripeYamlReference(@NotNull PsiNamedElement element, TextRange textRange) {
		super(element, textRange);
		key = element.getName();
	}

	public SilverStripeYamlReference(@NotNull PsiNamedElement element) {
		super(element);
		YAMLKeyValueImpl yaml = (YAMLKeyValueImpl) element;
		IElementType yamlType = yaml.getNode().getElementType();
		PsiElement yamlParent = PsiTreeUtil.findFirstParent(yaml, true, new Condition<PsiElement>() {
			@Override
			public boolean value(PsiElement element) {
				if (element == null || element.getNode() == null) return false;
				IElementType nodeType = element.getNode().getElementType();
				boolean isKeyValue = (nodeType == YAMLElementTypes.KEY_VALUE_PAIR);
				return isKeyValue;
			}
		});

		if (yamlParent != null) {
			YAMLKeyValueImpl yamlRealParent = (YAMLKeyValueImpl)yamlParent;
			key = yamlRealParent.getName() + "." + yaml.getName();
		}
	}

	@Override
	public TextRange getRangeInElement() {
		ASTNode includeKeyword = this.getElement().getNode().getFirstChildNode();
		return new TextRange(includeKeyword.getPsi().getStartOffsetInParent(),
				includeKeyword.getPsi().getStartOffsetInParent()+includeKeyword.getPsi().getTextLength()-1);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		Project project = myElement.getProject();
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		if (key != null) {
			final List<SilverStripeTranslationImpl> translations = SilverStripeFileUtil.findTranslations(project, key);
			for (SilverStripeTranslationImpl translation : translations) {
				results.add(new PsiElementResolveResult(translation));
			}
		}

		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return EMPTY_ARRAY;
		/*
		Project project = myElement.getProject();
		List<SilverStripeFile> properties = SilverStripeFileUtil.findFiles(project);
		List<LookupElement> variants = new ArrayList<LookupElement>();
		for (final SilverStripeFile property : properties) {
			variants.add(LookupElementBuilder.create(property).
					withIcon(SilverStripeFileType.FILE_ICON).
					withTypeText(property.getContainingFile().getName())
			);
		}
		return variants.toArray();*/
	}
}