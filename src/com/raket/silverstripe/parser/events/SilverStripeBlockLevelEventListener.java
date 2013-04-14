package com.raket.silverstripe.parser.events;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.Stack;
import com.raket.silverstripe.eventdispatcher.Event;

import static com.raket.silverstripe.psi.SilverStripeTypes.SS_BLOCK_STATEMENT;
import static com.raket.silverstripe.psi.SilverStripeTypes.SS_STATEMENTS;

public class SilverStripeBlockLevelEventListener extends SilverStripeStatementEventListener {
	Stack<Pair<PsiBuilder.Marker, String>> blockStack = new Stack<Pair<PsiBuilder.Marker, String>>();
	Stack<PsiBuilder.Marker> statementsStack = new Stack<PsiBuilder.Marker>();

	@Override
	public void handleEvent(Event e) {
		SilverStripeParserEventListener listener =  (SilverStripeParserEventListener)e.getContext();
		PsiBuilder.Marker marker = listener.getObserverMarker();
		String tokenText = (String)e.getUserInfo();

		if (e.getName().equals("block_start_statement_complete")) {
			blockStack.push(Pair.create(marker, tokenText));
			statementsStack.push(listener.getBuilder().mark());
		}
		else if (e.getName().equals("if_continue_statement_complete")) {
			if (!statementsStack.isEmpty()) {
				statementsStack.pop().doneBefore(SS_STATEMENTS, marker);
				statementsStack.push(listener.getBuilder().mark());
			}
		}
		else if (e.getName().equals("block_end_statement_complete")) {
			if (!statementsStack.isEmpty()) {
				statementsStack.pop().doneBefore(SS_STATEMENTS, marker);
			}
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
		else if (e.getName().equals("builder_eof")) {
			while (!statementsStack.isEmpty()) {
				statementsStack.pop().drop();
			}
		}
	}
}