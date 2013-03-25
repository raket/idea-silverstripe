package com.raket.silverstripe;

import java.util.HashMap;
import java.util.Map;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;
import static com.raket.silverstripe.SilverStripeBundle.message;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-24
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class SSErrorTokenTypes {
	public static Map<IElementType, String> ERROR_TOKEN_MESSAGES;
	public static TokenSet ERROR_TOKENS;

	private SSErrorTokenTypes() {
	}

	static {
		ERROR_TOKENS = TokenSet.create(SS_BAD_VAR, SS_BAD_BLOCK_STATEMENT);
		ERROR_TOKEN_MESSAGES = new HashMap<IElementType, String>();
		ERROR_TOKEN_MESSAGES.put(SS_BAD_VAR, message("ss.parsing.bad.var"));
		ERROR_TOKEN_MESSAGES.put(SS_VAR_STATEMENT, message("ss.parsing.missing.delimiter"));
		ERROR_TOKEN_MESSAGES.put(SS_BAD_BLOCK_STATEMENT, message("ss.parsing.bad.block.statement"));
	}
}
