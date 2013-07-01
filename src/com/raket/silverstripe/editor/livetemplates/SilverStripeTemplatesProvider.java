package com.raket.silverstripe.editor.livetemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import com.intellij.openapi.diagnostic.Logger;

public class SilverStripeTemplatesProvider implements DefaultLiveTemplatesProvider {
	private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.template.impl.TemplateSettings");

	public SilverStripeTemplatesProvider() {
		LOG.info("Hello");
	}

	@Override
	public String[] getDefaultLiveTemplateFiles() {
		return new String[]{"liveTemplates/SilverStripe"};
	}

	@Override
	public String[] getHiddenLiveTemplateFiles() {
		return null;
	}
}