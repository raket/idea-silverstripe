package com.raket.silverstripe;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.templateLanguages.TemplateLanguage;

public class SilverStripeLanguage extends Language implements TemplateLanguage {
    public static final SilverStripeLanguage INSTANCE = new SilverStripeLanguage();

    private SilverStripeLanguage() {
        super("SilverStripe");
    }

    public static LanguageFileType getDefaultTemplateLang() {
        return StdFileTypes.HTML;
    }
}
