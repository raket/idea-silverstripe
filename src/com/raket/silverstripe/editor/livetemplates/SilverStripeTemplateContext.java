package com.raket.silverstripe.editor.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import com.raket.silverstripe.psi.SilverStripeFile;
import org.jetbrains.annotations.NotNull;

public class SilverStripeTemplateContext extends TemplateContextType {
	protected SilverStripeTemplateContext() {
		super("SILVERSTRIPE", "SilverStripe Template");
	}

	@Override
	public boolean isInContext(@NotNull PsiFile file, int offset) {
		return (file instanceof SilverStripeFile);
	}
}
