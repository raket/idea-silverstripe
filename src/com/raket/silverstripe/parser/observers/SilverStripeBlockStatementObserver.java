package com.raket.silverstripe.parser.observers;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import com.raket.silverstripe.observers.SilverStripeObservable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-04-13
 * Time: 01:58
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeBlockStatementObserver extends SilverStripeStatementObserver {
	Stack<Pair<PsiBuilder.Marker, String>> blockStack;

	@Override
	public void updateBefore(SilverStripeObservable o, Object... arg) {
		PsiBuilder builder = (PsiBuilder) arg[0];
		String tokenText = (String) arg[1];
		int currentOffset = builder.getCurrentOffset();
		PsiBuilder.Marker levelMarker = builder.mark();
//		blockStack.push(Pair.create(levelMarker, tokenText));
	}

	@Override
	public void updateAfter(SilverStripeObservable o, Object... arg) {
		while (!blockStack.isEmpty()) {
			Pair blockLevel = blockStack.pop();
			PsiBuilder.Marker levelMarker = (PsiBuilder.Marker) blockLevel.getFirst();
			levelMarker.drop();
		}
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
