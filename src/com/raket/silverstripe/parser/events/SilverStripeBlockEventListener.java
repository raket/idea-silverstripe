package com.raket.silverstripe.parser.events;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.Stack;
import com.raket.silverstripe.eventdispatcher.Event;

import static com.raket.silverstripe.psi.SilverStripeTypes.SS_BLOCK_STATEMENT;

public class SilverStripeBlockEventListener extends SilverStripeStatementEventListener {
	Stack<Pair<PsiBuilder.Marker, String>> blockStack = new Stack<Pair<PsiBuilder.Marker, String>>();

	@Override
	public void handleEvent(Event e) {
		PsiBuilder.Marker marker = (PsiBuilder.Marker)e.getContext();
		String tokenText = (String)e.getUserInfo();

		if (e.getName().equals("start_statement_complete")) {
			blockStack.push(Pair.create(marker, tokenText));
		}
		else {
			if (!blockStack.isEmpty()) {
				Pair<PsiBuilder.Marker, String> blockLevel = blockStack.peek();
				String endString = "end_"+blockLevel.getSecond();
				if (endString.equals(tokenText)) {
					PsiBuilder.Marker blockMarker = blockLevel.getFirst().precede();
					blockMarker.done(SS_BLOCK_STATEMENT);
					blockStack.pop();
				}
			}
		}
	}
}