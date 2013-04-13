package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.raket.silverstripe.observers.SilverStripeObservable;

import static com.raket.silverstripe.SSErrorTokenTypes.ERROR_TOKEN_MESSAGES;
import static com.raket.silverstripe.psi.SilverStripeTypes.*;

public class SilverStripeVariableStatementObserver extends SilverStripeStatementObserver {

	@Override
	public void updateBefore(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		currentToken = (IElementType) arg[1];

		if (currentToken.equals(SS_VAR_START_DELIMITER) && !marking) {
			observerMarker = builder.mark();
			marking = true;
		}
	}

	@Override
	public void updateAfter(SilverStripeObservable o, Object... arg) {
		if (currentToken != null) {
			if (marking && currentToken.equals(SS_VAR_END_DELIMITER)) {
				observerMarker.done(SS_VAR_STATEMENT);
				marking = false;
			}
			if (marking && !varStatementTokens.contains(currentToken)) {
				observerMarker.error(ERROR_TOKEN_MESSAGES.get(SS_VAR_STATEMENT));
				marking = false;
			}
		}
	}
}
