package com.raket.silverstripe.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-22
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripePsiElement extends ASTWrapperPsiElement {
    public SilverStripePsiElement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    // some common logic should come here
}
