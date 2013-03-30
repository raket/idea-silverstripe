package com.raket.silverstripe.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.impl.PsiFileEx;
import com.raket.silverstripe.file.SilverStripeFileType;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SilverStripeFile extends PsiFileBase implements PsiFileEx {
    public SilverStripeFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SilverStripeLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SilverStripeFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "SilverStripe Template File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
