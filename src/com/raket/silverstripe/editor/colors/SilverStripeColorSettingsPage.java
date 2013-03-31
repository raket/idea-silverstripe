package com.raket.silverstripe.editor.colors;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.raket.silverstripe.editor.highlighting.SilverStripeSyntaxHighlighter;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class SilverStripeColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Key", SilverStripeSyntaxHighlighter.KEY),
            new AttributesDescriptor("Separator", SilverStripeSyntaxHighlighter.SEPARATOR),
            new AttributesDescriptor("Value", SilverStripeSyntaxHighlighter.VALUE),
            new AttributesDescriptor("Delimiter", SilverStripeSyntaxHighlighter.SS_BLOCK),
            new AttributesDescriptor("Keyword", SilverStripeSyntaxHighlighter.SS_KEYWORD),
            new AttributesDescriptor("Variable", SilverStripeSyntaxHighlighter.SS_BLOCK_VAR_KEY),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return SilverStripeFileType.FILE_ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new SilverStripeSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "# You are reading the \".properties\" entry.\n" +
                "! The exclamation mark can also mark text as comments.\n" +
                "website = http://en.wikipedia.org/\n" +
                "language = English\n" +
                "# The backslash below tells the application to continue reading\n" +
                "# the value onto the next line.\n" +
                "message = Welcome to \\\n" +
                "          Wikipedia!\n" +
                "# Add spaces to the key\n" +
                "key\\ with\\ spaces = This is the value that could be looked up with the key \"key with spaces\".\n" +
                "# Unicode\n" +
                "tab : \\u0009\n" +
                "Variable $Var\n" +
                "Variable in quote \"$Var\"\n" +
                "<% loop $Var %>";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "SilverStripe Template";
    }
}
