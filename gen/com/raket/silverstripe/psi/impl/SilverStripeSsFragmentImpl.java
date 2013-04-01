// This is a generated file. Not intended for manual editing.
package com.raket.silverstripe.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class SilverStripeSsFragmentImpl extends ASTWrapperPsiElement implements SilverStripeSsFragment {

  public SilverStripeSsFragmentImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SilverStripeVisitor) ((SilverStripeVisitor)visitor).visitSsFragment(this);
    else super.accept(visitor);
  }

}
