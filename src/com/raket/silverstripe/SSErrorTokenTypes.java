package com.raket.silverstripe;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.HashMap;
import java.util.Map;

import static com.raket.silverstripe.SilverStripeBundle.message;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

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
