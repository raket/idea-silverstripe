package com.raket.silverstripe.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.TemplateLanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SilverStripeFileType extends LanguageFileType implements TemplateLanguageFileType {
    public static final Icon FILE_ICON = IconLoader.getIcon("/icons/silverstripe.png");
    public static final SilverStripeFileType INSTANCE = new SilverStripeFileType();

    @NonNls
    public static final String DEFAULT_EXTENSION = "ss";

    protected SilverStripeFileType() {
        super(SilverStripeLanguage.INSTANCE);

/*        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider() {
            public EditorHighlighter getEditorHighlighter(@Nullable Project project,
                                                          @NotNull FileType fileType,
                                                          @Nullable VirtualFile virtualFile,
                                                          @NotNull EditorColorsScheme editorColorsScheme) {
                return new SilverStripeTemplateHighlighter(project, virtualFile, editorColorsScheme);
            }
        });*/
    }

    @NotNull
    @Override
    public String getName() {
        return "SilverStripe";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "SilverStripe template file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return FILE_ICON;
    }
}
