// This is a generated file. Not intended for manual editing.
package com.raket.silverstripe.parser;

import org.jetbrains.annotations.*;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.openapi.diagnostic.Logger;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SilverStripeParser implements PsiParser {

  public static Logger LOG_ = Logger.getInstance("com.raket.silverstripe.parser.SilverStripeParser");

  @NotNull
  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    int level_ = 0;
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this);
    if (root_ == OUTER_WRAPPER) {
      result_ = outer_wrapper(builder_, level_ + 1);
    }
    else if (root_ == SS_BAD_BLOCK_STATEMENT) {
      result_ = ss_bad_block_statement(builder_, level_ + 1);
    }
    else if (root_ == SS_BLOCK_END_STATEMENT) {
      result_ = ss_block_end_statement(builder_, level_ + 1);
    }
    else if (root_ == SS_BLOCK_SIMPLE_STATEMENT) {
      result_ = ss_block_simple_statement(builder_, level_ + 1);
    }
    else if (root_ == SS_BLOCK_START_STATEMENT) {
      result_ = ss_block_start_statement(builder_, level_ + 1);
    }
    else if (root_ == SS_BLOCK_STATEMENT) {
      result_ = ss_block_statement(builder_, level_ + 1);
    }
    else if (root_ == SS_FRAGMENT) {
      result_ = ss_fragment(builder_, level_ + 1);
    }
    else if (root_ == SS_VAR_STATEMENT) {
      result_ = ss_var_statement(builder_, level_ + 1);
    }
    else {
      Marker marker_ = builder_.mark();
      result_ = parse_root_(root_, builder_, level_);
      while (builder_.getTokenType() != null) {
        builder_.advanceLexer();
      }
      marker_.done(root_);
    }
    return builder_.getTreeBuilt();
  }

  protected boolean parse_root_(final IElementType root_, final PsiBuilder builder_, final int level_) {
    return silverstripeFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // COMMENT|CRLF|CONTENT|ss_block_start_statement|ss_block_simple_statement|ss_block_end_statement|SS_VAR|SS_VAR_START_DELIMITER|SS_VAR_END_DELIMITER|SS_BAD_VAR|SS_BLOCK_START_START|SS_BLOCK_SIMPLE_START
  static boolean item_(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMENT);
    if (!result_) result_ = consumeToken(builder_, CRLF);
    if (!result_) result_ = consumeToken(builder_, CONTENT);
    if (!result_) result_ = ss_block_start_statement(builder_, level_ + 1);
    if (!result_) result_ = ss_block_simple_statement(builder_, level_ + 1);
    if (!result_) result_ = ss_block_end_statement(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SS_VAR);
    if (!result_) result_ = consumeToken(builder_, SS_VAR_START_DELIMITER);
    if (!result_) result_ = consumeToken(builder_, SS_VAR_END_DELIMITER);
    if (!result_) result_ = consumeToken(builder_, SS_BAD_VAR);
    if (!result_) result_ = consumeToken(builder_, SS_BLOCK_START_START);
    if (!result_) result_ = consumeToken(builder_, SS_BLOCK_SIMPLE_START);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // item_*
  public static boolean outer_wrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "outer_wrapper")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<outer wrapper>");
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!item_(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "outer_wrapper");
        break;
      }
      offset_ = next_offset_;
    }
    marker_.done(OUTER_WRAPPER);
    exitErrorRecordingSection(builder_, level_, true, false, _SECTION_GENERAL_, null);
    return true;
  }

  /* ********************************************************** */
  // item_*
  static boolean silverstripeFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "silverstripeFile")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!item_(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "silverstripeFile");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // ()
  public static boolean ss_bad_block_statement(PsiBuilder builder_, int level_) {
    builder_.mark().done(SS_BAD_BLOCK_STATEMENT);
    return true;
  }

  /* ********************************************************** */
  // SS_BLOCK_END_START SS_END_KEYWORD SS_BLOCK_END
  public static boolean ss_block_end_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ss_block_end_statement")) return false;
    if (!nextTokenIs(builder_, SS_BLOCK_END_START)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, SS_BLOCK_END_START, SS_END_KEYWORD, SS_BLOCK_END);
    if (result_) {
      marker_.done(SS_BLOCK_END_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // SS_BLOCK_START SS_SIMPLE_KEYWORD SS_BLOCK_VAR? SS_BLOCK_END
  public static boolean ss_block_simple_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ss_block_simple_statement")) return false;
    if (!nextTokenIs(builder_, SS_BLOCK_START)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, SS_BLOCK_START, SS_SIMPLE_KEYWORD);
    result_ = result_ && ss_block_simple_statement_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SS_BLOCK_END);
    if (result_) {
      marker_.done(SS_BLOCK_SIMPLE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // SS_BLOCK_VAR?
  private static boolean ss_block_simple_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ss_block_simple_statement_2")) return false;
    consumeToken(builder_, SS_BLOCK_VAR);
    return true;
  }

  /* ********************************************************** */
  // SS_BLOCK_START SS_START_KEYWORD SS_BLOCK_VAR SS_BLOCK_END
  public static boolean ss_block_start_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ss_block_start_statement")) return false;
    if (!nextTokenIs(builder_, SS_BLOCK_START)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, SS_BLOCK_START, SS_START_KEYWORD, SS_BLOCK_VAR, SS_BLOCK_END);
    if (result_) {
      marker_.done(SS_BLOCK_START_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // ()
  public static boolean ss_block_statement(PsiBuilder builder_, int level_) {
    builder_.mark().done(SS_BLOCK_STATEMENT);
    return true;
  }

  /* ********************************************************** */
  // ()
  public static boolean ss_fragment(PsiBuilder builder_, int level_) {
    builder_.mark().done(SS_FRAGMENT);
    return true;
  }

  /* ********************************************************** */
  // SS_VAR_START_DELIMITER SS_VAR SS_VAR_END_DELIMITER
  public static boolean ss_var_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ss_var_statement")) return false;
    if (!nextTokenIs(builder_, SS_VAR_START_DELIMITER)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, SS_VAR_START_DELIMITER, SS_VAR, SS_VAR_END_DELIMITER);
    if (result_) {
      marker_.done(SS_VAR_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

}
