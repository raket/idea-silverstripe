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
%public
%class SilverStripeLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    private Stack<Integer> stack = new Stack<Integer>();

    public void yypushstate(int newState) {
      stack.push(yystate());
      yybegin(newState);
    }

    public void yypopstate() {
      yybegin(stack.pop());
    }

    public void yycleanstates() {
       while(!stack.isEmpty()) {
          yybegin(stack.pop());
       }
    }

	public IElementType checkContent() {
        if (!yytext().toString().equals("")) {
           if (yytext().toString().trim().length() == 0) {
               return TokenType.WHITE_SPACE;
           } else {
               return SilverStripeTypes.CONTENT;
           }
        }
    	return null;
	}
%}

CRLF= \n|\r|\r\n
WHITE_SPACE=[\ \t\f]
COMMA= ,
LEFT_PAREN= \(
RIGHT_PAREN= \)
DOT= \.
NUMBER=[0-9]+
VAR= \$?[a-zA-Z]+([a-zA-Z0-9])*
SS_VAR= (\$[a-zA-Z]+)((\(((\')[^\']+(\')|(\")[^\"]+(\")|[a-zA-Z0-9,\ \t\f]+)\))|\.|([a-zA-Z]+))*
SS_VAR_START_DELIMITER= \{
SS_VAR_END_DELIMITER= \}
SS_BLOCK_START= <%
SS_BLOCK_END= %>
SS_START_KEYWORD= loop | with | control
SS_IF_KEYWORD= if
SS_ELSE_IF_KEYWORD= else_if
SS_ELSE_KEYWORD= else
SS_COMPARISON_OPERATOR= "==" | "!=" | "=" | "not"
SS_AND_OR_OPERATOR= "&&" | "||"
SS_STRING= \"[^\"]*\" | \'[^\']*\'
SS_DOUBLE_WITH_VAR= [^\"{]*
SS_SINGLE_WITH_VAR= [^\'{]*
SS_SIMPLE_KEYWORD= base_tag
SS_INCLUDE_KEYWORD= include
SS_INCLUDE_FILE= [a-zA-Z\-_]+
SS_CACHED_KEYWORD= cached
SS_END_KEYWORD= end_loop | end_if | end_with | end_control | end_cached
SS_BLOCK_VAR=(\$?[a-zA-Z]+)((\(((\')[^\']+(\')|(\")[^\"]+(\")|[a-zA-Z0-9,\ \t\f]+)\))|\.|([a-zA-Z]+))*
SS_COMMENT_START= <%--
SS_COMMENT_END= --%>
SS_TRANSLATION_START= <%t
SS_DOUBLE=\"
SS_SINGLE=\'
SS_TRANSLATION_IDENTIFIER= [a-zA-Z]+\.[a-zA-Z]+

%state SS_VAR
%state SS_BLOCK_START
%state SS_BLOCK_VAR
%state SS_BAD_BLOCK_STATEMENT
%state SS_COMMENT
%state SS_TRANSLATION
%state SS_IF_STATEMENT
%state SS_INCLUDE_STATEMENT
%state SS_METHOD_ARGUMENTS
%state SS_CACHED_STATEMENT
%state SS_INCLUDE_VARS
%state SS_STRING
%state SS_DOUBLE
%state SS_SINGLE
%%

<YYINITIAL> {
    !([^]*("<%"|\$[a-zA-Z]+|"{$")[^]*)                {
        if (yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("<")) {
            yypushback(1);
            yypushstate(SS_BLOCK_START);
        }
        if (yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("$") ||
            yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("{") ) {
            yypushback(1);
            yypushstate(SS_VAR);
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



<SS_BLOCK_START> {
	{SS_BLOCK_START}                    { return SilverStripeTypes.SS_BLOCK_START; }
	{SS_START_KEYWORD}                  { yypushstate(SS_BLOCK_VAR); return SilverStripeTypes.SS_START_KEYWORD; }
	{SS_SIMPLE_KEYWORD}                 { yypushstate(SS_BLOCK_VAR); return SilverStripeTypes.SS_SIMPLE_KEYWORD; }
	{SS_INCLUDE_KEYWORD}                { yypushstate(SS_INCLUDE_STATEMENT); return SilverStripeTypes.SS_INCLUDE_KEYWORD; }
	{SS_CACHED_KEYWORD}                 { yypushstate(SS_CACHED_STATEMENT); return SilverStripeTypes.SS_CACHED_KEYWORD; }
	{SS_TRANSLATION_START}              { yypushstate(SS_TRANSLATION);  yypushback(3); return SilverStripeTypes.SS_BLOCK_START; }
	{SS_COMMENT_START}                  { yypushstate(SS_COMMENT); return SilverStripeTypes.SS_COMMENT_START; }
	{SS_IF_KEYWORD}                     { yypushstate(SS_IF_STATEMENT); return SilverStripeTypes.SS_IF_KEYWORD; }
	{SS_ELSE_IF_KEYWORD}                { yypushstate(SS_IF_STATEMENT); return SilverStripeTypes.SS_ELSE_IF_KEYWORD; }
	{SS_ELSE_KEYWORD}                   { return SilverStripeTypes.SS_ELSE_KEYWORD; }
	{SS_END_KEYWORD}                    { return SilverStripeTypes.SS_END_KEYWORD; }
    {SS_COMMENT_END}                    { yypopstate(); return SilverStripeTypes.SS_COMMENT_END; }
    {SS_BLOCK_END}                      { yypopstate(); return SilverStripeTypes.SS_BLOCK_END; }
	{WHITE_SPACE}+                      { return TokenType.WHITE_SPACE; }
	.                                   { yypushstate(SS_BAD_BLOCK_STATEMENT); yypushback(yylength()); }
}

<SS_INCLUDE_STATEMENT> {
    {SS_INCLUDE_FILE}                   { yypushstate(SS_INCLUDE_VARS); return SilverStripeTypes.SS_INCLUDE_FILE; }
	{WHITE_SPACE}+                      { return TokenType.WHITE_SPACE; }
    .                                   { yypopstate(); yypushback(yylength()); }
}

<SS_INCLUDE_VARS> {
	{VAR}                               { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR; }
	{COMMA}                             { return SilverStripeTypes.COMMA; }
    {SS_COMPARISON_OPERATOR}            { return SilverStripeTypes.SS_COMPARISON_OPERATOR; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
}

<SS_IF_STATEMENT> {
    {SS_COMPARISON_OPERATOR}           { return SilverStripeTypes.SS_COMPARISON_OPERATOR; }
    {SS_AND_OR_OPERATOR}               { return SilverStripeTypes.SS_AND_OR_OPERATOR; }
    {SS_STRING}                        { return SilverStripeTypes.SS_STRING; }
	{VAR}                              { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
}

<SS_DOUBLE> {
	{SS_VAR_START_DELIMITER}            { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR_START_DELIMITER; }
    {SS_DOUBLE_WITH_VAR}                { return SilverStripeTypes.SS_STRING; }
    {SS_DOUBLE}                         { yypopstate(); return SilverStripeTypes.SS_DOUBLE_RIGHT; }
	{WHITE_SPACE}+                      { return TokenType.WHITE_SPACE; }
    .                                   { yypopstate(); yypushback(yylength()); }
}

<SS_SINGLE> {
	{SS_VAR_START_DELIMITER}            { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR_START_DELIMITER; }
    {SS_SINGLE_WITH_VAR}                { return SilverStripeTypes.SS_STRING; }
    {SS_SINGLE}                         { yypopstate(); return SilverStripeTypes.SS_SINGLE_RIGHT; }
	{WHITE_SPACE}+                      { return TokenType.WHITE_SPACE; }
    .                                   { yypopstate(); yypushback(yylength()); }
}

<SS_TRANSLATION> {
	{SS_BLOCK_START}                    { return SilverStripeTypes.SS_BLOCK_START; }
	"t"                                 { return SilverStripeTypes.SS_TRANSLATION_KEYWORD; }
    {SS_DOUBLE}                         { yypushstate(SS_DOUBLE); return SilverStripeTypes.SS_DOUBLE_LEFT; }
    {SS_SINGLE}                         { yypushstate(SS_SINGLE); return SilverStripeTypes.SS_SINGLE_LEFT; }
    {SS_TRANSLATION_IDENTIFIER}         { return SilverStripeTypes.SS_TRANSLATION_IDENTIFIER; }
	{VAR}                               { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR; }
    {SS_COMPARISON_OPERATOR}            { return SilverStripeTypes.SS_COMPARISON_OPERATOR; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
}

<SS_BLOCK_VAR> {
    {VAR}                              { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
}

<SS_BAD_BLOCK_STATEMENT> {
    ~"%>"  { yypopstate(); yypushback(2); return SilverStripeTypes.SS_BAD_BLOCK_STATEMENT; }
}

<SS_COMMENT> {
	~"--%>"  { yypopstate(); yypushback(4); return SilverStripeTypes.COMMENT; }
}

<SS_CACHED_STATEMENT> {
	{VAR} { yypushstate(SS_VAR); return SilverStripeTypes.SS_VAR; }
	{COMMA}  { return SilverStripeTypes.COMMA; }
	{SS_STRING} { return SilverStripeTypes.SS_STRING; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
}

<SS_VAR> {
	{SS_VAR_START_DELIMITER} { return SilverStripeTypes.SS_VAR_START_DELIMITER; }
	{VAR} { return SilverStripeTypes.SS_VAR; }
	{DOT} { return SilverStripeTypes.DOT; }
    {LEFT_PAREN} { yypushstate(SS_METHOD_ARGUMENTS); return SilverStripeTypes.LEFT_PAREN; }

	{SS_VAR_END_DELIMITER} { yypopstate(); return SilverStripeTypes.SS_VAR_END_DELIMITER; }
	{SS_BLOCK_END}                      { yycleanstates(); return SilverStripeTypes.SS_BLOCK_END; }
	.                                   { yypopstate(); yypushback(yylength()); }
}

<SS_METHOD_ARGUMENTS> {
	{COMMA}  { return SilverStripeTypes.COMMA; }
	{VAR} { return SilverStripeTypes.SS_VAR; }
	{DOT} { return SilverStripeTypes.DOT; }
	{SS_STRING} { return SilverStripeTypes.SS_STRING; }
	{NUMBER} { return SilverStripeTypes.NUMBER; }
    {RIGHT_PAREN} { yypopstate(); return SilverStripeTypes.RIGHT_PAREN; }
	{WHITE_SPACE}+                      { return TokenType.WHITE_SPACE; }
    .                                   { yypopstate(); yypushback(yylength()); }
}

{WHITE_SPACE}+                                              { return TokenType.WHITE_SPACE; }
{CRLF}
{
	if (!stack.isEmpty()) {
		   yypopstate(); yypushback(yylength());
		} else {
		return SilverStripeTypes.CRLF;
	}
}
.
{   return TokenType.BAD_CHARACTER;
/*	if (!stack.isEmpty()) {
	   yypopstate(); yypushback(yylength());
	} else {
		return TokenType.BAD_CHARACTER;
	}*/
}