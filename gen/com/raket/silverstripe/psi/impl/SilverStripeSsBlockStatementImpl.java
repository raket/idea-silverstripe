// This is a generated file. Not intended for manual editing.
package com.raket.silverstripe.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.raket.silverstripe.psi.*;

public class SilverStripeSsBlockStatementImpl extends ASTWrapperPsiElement implements SilverStripeSsBlockStatement {

  public SilverStripeSsBlockStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SilverStripeVisitor) ((SilverStripeVisitor)visitor).visitSsBlockStatement(this);
    else super.accept(visitor);
  }

}
