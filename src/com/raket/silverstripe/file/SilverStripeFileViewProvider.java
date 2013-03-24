package com.raket.silverstripe.file;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.psi.SilverStripeTypes;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

public class SilverStripeFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
        implements ConfigurableTemplateLanguageFileViewProvider {

    private final PsiManager myManager;
    private final VirtualFile myFile;

    public SilverStripeFileViewProvider(PsiManager manager, VirtualFile file, boolean physical) {
        super(manager, file, physical);

        myManager = manager;
        myFile = file;

        getTemplateDataLanguage(myManager, myFile);
    }

    private Language getTemplateDataLanguage(PsiManager manager, VirtualFile file) {
        Language dataLang = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
        if(dataLang == null) {
            dataLang = SilverStripeLanguage.getDefaultTemplateLang().getLanguage();
        }

        Language substituteLang = LanguageSubstitutors.INSTANCE.substituteLanguage(dataLang, file, manager.getProject());

        // only use a substituted language if it's templateable
        if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLang)) {
            dataLang = substituteLang;
        }

        return dataLang;
    }

    @NotNull
    @Override
    public Language getBaseLanguage() {
        return SilverStripeLanguage.INSTANCE;
    }

    @NotNull
    @Override
    public Language getTemplateDataLanguage() {
        return getTemplateDataLanguage(myManager, myFile);
    }

    @NotNull
    @Override
    public Set<Language> getLanguages() {
        return new THashSet<Language>(Arrays.asList(new Language[] {SilverStripeLanguage.INSTANCE, getTemplateDataLanguage(myManager, myFile)}));
    }

    @Override
    protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile virtualFile) {
        return new SilverStripeFileViewProvider(getManager(), virtualFile, false);
    }

    @Override
    protected PsiFile createFile(@NotNull Language lang) {
        ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
        if (parserDefinition == null) {
            return null;
        }

        Language templateDataLanguage = getTemplateDataLanguage(myManager, myFile);
        if (lang == templateDataLanguage) {
            PsiFileImpl file = (PsiFileImpl) parserDefinition.createFile(this);
            file.setContentElementType(new TemplateDataElementType("SS_TEMPLATE_DATA", templateDataLanguage, SilverStripeTypes.CONTENT, SilverStripeTypes.OUTER_WRAPPER));
            return file;
        } else if (lang == SilverStripeLanguage.INSTANCE) {
            return parserDefinition.createFile(this);
        } else {
            return null;
        }
    }
}