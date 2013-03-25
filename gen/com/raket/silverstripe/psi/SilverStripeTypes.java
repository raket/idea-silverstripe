// This is a generated file. Not intended for manual editing.
package com.raket.silverstripe.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.raket.silverstripe.psi.impl.*;

public interface SilverStripeTypes {

  IElementType OUTER_WRAPPER = new SilverStripeElementType("OUTER_WRAPPER");
  IElementType SS_BLOCK_END_STATEMENT = new SilverStripeElementType("SS_BLOCK_END_STATEMENT");
  IElementType SS_BLOCK_SIMPLE_STATEMENT = new SilverStripeElementType("SS_BLOCK_SIMPLE_STATEMENT");
  IElementType SS_BLOCK_START_STATEMENT = new SilverStripeElementType("SS_BLOCK_START_STATEMENT");
  IElementType SS_BLOCK_STATEMENT = new SilverStripeElementType("SS_BLOCK_STATEMENT");
  IElementType SS_VAR_STATEMENT = new SilverStripeElementType("SS_VAR_STATEMENT");

  IElementType COMMENT = new SilverStripeTokenType("COMMENT");
  IElementType CONTENT = new SilverStripeTokenType("CONTENT");
  IElementType CRLF = new SilverStripeTokenType("CRLF");
  IElementType SS_BAD_BLOCK_STATEMENT = new SilverStripeTokenType("SS_BAD_BLOCK_STATEMENT");
  IElementType SS_BAD_VAR = new SilverStripeTokenType("SS_BAD_VAR");
  IElementType SS_BLOCK_END = new SilverStripeTokenType("SS_BLOCK_END");
  IElementType SS_BLOCK_START = new SilverStripeTokenType("SS_BLOCK_START");
  IElementType SS_BLOCK_VAR = new SilverStripeTokenType("SS_BLOCK_VAR");
  IElementType SS_END_KEYWORD = new SilverStripeTokenType("SS_END_KEYWORD");
  IElementType SS_SIMPLE_KEYWORD = new SilverStripeTokenType("SS_SIMPLE_KEYWORD");
  IElementType SS_START_KEYWORD = new SilverStripeTokenType("SS_START_KEYWORD");
  IElementType SS_VAR = new SilverStripeTokenType("SS_VAR");
  IElementType SS_VAR_DELIMITER = new SilverStripeTokenType("SS_VAR_DELIMITER");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == OUTER_WRAPPER) {
        return new SilverStripeOuterWrapperImpl(node);
      }
      else if (type == SS_BLOCK_END_STATEMENT) {
        return new SilverStripeSsBlockEndStatementImpl(node);
      }
      else if (type == SS_BLOCK_SIMPLE_STATEMENT) {
        return new SilverStripeSsBlockSimpleStatementImpl(node);
      }
      else if (type == SS_BLOCK_START_STATEMENT) {
        return new SilverStripeSsBlockStartStatementImpl(node);
      }
      else if (type == SS_BLOCK_STATEMENT) {
        return new SilverStripeSsBlockStatementImpl(node);
      }
      else if (type == SS_VAR_STATEMENT) {
        return new SilverStripeSsVarStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
