package com.raket.silverstripe.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.psi.SilverStripeTypes;

public class SilverStripePsiImplUtil  {
    public static String getKeyword(PsiElement element) {
        TokenSet tokens = TokenSet.create(SilverStripeTypes.SS_START_KEYWORD, SilverStripeTypes.SS_SIMPLE_KEYWORD, SilverStripeTypes.SS_END_KEYWORD);
        ASTNode keyNode = element.getNode().findChildByType(tokens);
        if (keyNode != null) {
            return keyNode.getText();
        } else {
            return null;
        }
    }
}