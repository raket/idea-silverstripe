package com.raket.silverstripe.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.impl.PsiFileEx;
import com.raket.silverstripe.SilverStripeLanguage;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NotNull;

public class SilverStripeFile extends PsiFileBase implements PsiFileEx {

    public SilverStripeFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SilverStripeLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SilverStripeFileType.INSTANCE;
    }
}
