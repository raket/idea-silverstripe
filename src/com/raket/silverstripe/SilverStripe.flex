package com.raket.silverstripe;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.psi.SilverStripeTypes;
import com.raket.silverstripe.psi.SilverStripeTokenType;
import com.intellij.psi.TokenType;
import com.intellij.psi.*;
import com.intellij.psi.xml.*;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;

%%

%unicode
%class SilverStripeLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    private Stack<Integer> stack = new Stack<Integer>();

    public void yypushState(int newState) {
      stack.push(yystate());
      yybegin(newState);
    }

 	public IElementType checkVariable(IElementType success, IElementType fail) {
        String matchedVar = yytext().toString();
        if (matchedVar.matches("(\\$[a-zA-Z]+)((\\((\\\"(?=[a-zA-Z]*\\\")|\\'(?=[a-zA-Z]*\\')|[a-zA-Z](?=[a-zA-Z]+))[a-zA-Z\\\"\\']+\\))|\\.[a-zA-Z]+)*")) {
           return success;
        }
        else {
           return fail;
        }
 	}

  	public IElementType checkBlockVariable(IElementType success, IElementType fail) {
        String matchedVar = yytext().toString();
        if (matchedVar.matches("(\\$?[a-zA-Z]+)((\\((\\\"(?=[a-zA-Z]*\\\")|\\'(?=[a-zA-Z]*\\')|[a-zA-Z](?=[a-zA-Z]+))[a-zA-Z\\\"\\']+\\))|\\.[a-zA-Z]+)*")) {
           return success;
        }
        else {
           return fail;
        }
 	}

    public void yypopState() {
      yybegin(stack.pop());
    }

%}

CRLF= \n|\r|\r\n
WHITE_SPACE=[\ \t\f]
FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
SEPARATOR=[:=]
KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".

SS_VAR= (\$[a-zA-Z]+)((\((\"|\')?[a-zA-Z]+(\"|\')?\))|\.|([a-zA-Z]+))*
SS_VAR_DELIMITER= \{ | \}
SS_BLOCK_START= <%
SS_BLOCK_END= %>
SS_START_KEYWORD= loop | if | else_if | else | with | control
SS_SIMPLE_KEYWORD= include | base_tag
SS_END_KEYWORD= end_loop | end_if | end_with | end_control
SS_BLOCK_VAR=(\$?[a-zA-Z]+)((\((\"|\')?[a-zA-Z]+(\"|\')?\))|\.|([a-zA-Z]+))*

%state SS_VAR
%state SS_WITH_DELIMITER
%state SS_BLOCK_START
%state SS_BLOCK_VAR
%state SS_BAD_VAR
%state SS_BAD_BLOCK_STATEMENT
%%

<YYINITIAL> {
    !([^]*("<%"|\$[a-zA-Z]+|"{$")[^]*)                {
        if (yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("<")) {
            yypushback(1);
            yypushState(SS_BLOCK_START);
        }
        if (yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("$") ||
            yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("{") ) {
            yypushback(1);
            yybegin(SS_VAR);
        }

        // we stray from the Handlebars grammar a bit here since we need our WHITE_SPACE more clearly delineated
        //    and we need to avoid creating extra tokens for empty strings (makes the parser and formatter happier)
        if (!yytext().toString().equals("")) {
           if (yytext().toString().trim().length() == 0) {
               return TokenType.WHITE_SPACE;
           } else {
               return SilverStripeTypes.CONTENT;
           }
        }
    }
}

<SS_BLOCK_START> {WHITE_SPACE}+                             { yybegin(SS_BLOCK_START); return TokenType.WHITE_SPACE; }
<SS_BLOCK_START> {SS_BLOCK_START}                           { yybegin(SS_BLOCK_START); return SilverStripeTypes.SS_BLOCK_START; }
<SS_BLOCK_START> {SS_START_KEYWORD}                         { yybegin(SS_BLOCK_VAR); return SilverStripeTypes.SS_START_KEYWORD; }
<SS_BLOCK_START> {SS_SIMPLE_KEYWORD}                        { yybegin(SS_BLOCK_VAR); return SilverStripeTypes.SS_SIMPLE_KEYWORD; }
<SS_BLOCK_START> {SS_END_KEYWORD}                           { yybegin(SS_BLOCK_VAR); return SilverStripeTypes.SS_END_KEYWORD; }
<SS_BLOCK_START> .                                          { yybegin(SS_BAD_BLOCK_STATEMENT); yypushback(1); }

<SS_VAR> {
	{SS_VAR} {
        yybegin(YYINITIAL); return checkVariable(SilverStripeTypes.SS_VAR, SilverStripeTypes.SS_BAD_VAR);
	}
	{SS_VAR_DELIMITER}                                       { yybegin(SS_WITH_DELIMITER); return SilverStripeTypes.SS_VAR_DELIMITER; }
}
<SS_WITH_DELIMITER> {
	{SS_VAR} {
        yybegin(SS_WITH_DELIMITER); return checkVariable(SilverStripeTypes.SS_VAR, SilverStripeTypes.SS_BAD_VAR);
	}
	{SS_VAR_DELIMITER}                                       { yybegin(YYINITIAL); return SilverStripeTypes.SS_VAR_DELIMITER; }
}
<SS_BLOCK_VAR> {
    {WHITE_SPACE}+                                          { yybegin(SS_BLOCK_VAR); return TokenType.WHITE_SPACE; }
    {SS_BLOCK_VAR} {
        yybegin(SS_BLOCK_VAR); return checkBlockVariable(SilverStripeTypes.SS_BLOCK_VAR, SilverStripeTypes.SS_BAD_VAR);
	}
    {SS_BLOCK_END}                                          { yybegin(YYINITIAL); return SilverStripeTypes.SS_BLOCK_END; }
}
<SS_BAD_BLOCK_STATEMENT> {
    ~"%>"  { yybegin(SS_BLOCK_VAR); yypushback(2); return SilverStripeTypes.SS_BAD_BLOCK_STATEMENT; }
}

{CRLF}                                                      { yybegin(YYINITIAL); return SilverStripeTypes.CRLF; }
{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
.                                                           { return TokenType.BAD_CHARACTER; }