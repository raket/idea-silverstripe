package com.raket.silverstripe.parser.events;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.eventdispatcher.Event;
import com.raket.silverstripe.eventdispatcher.EventDispatcher;
import com.raket.silverstripe.eventdispatcher.EventListener;
import com.raket.silverstripe.parser.SilverStripeBaseParser;

public abstract class SilverStripeParserEventListener implements EventListener {
	EventDispatcher dispatcher = EventDispatcher.getInstance();
	protected  boolean marking = false;
	protected PsiBuilder.Marker observerMarker;
	protected IElementType currentToken;

	@Override
	public void handleEvent(Event e) {
		SilverStripeBaseParser builder = (SilverStripeBaseParser)e.getContext();
		IElementType token = (IElementType)e.getUserInfo();
		if (e.getName().equals("before_consume"))
			updateBefore(builder, token);
		else
			updateAfter(builder, token);
	}

	public void updateBefore(SilverStripeBaseParser parser, IElementType token) {

	}

	public void updateAfter(SilverStripeBaseParser parser, IElementType token) {

	}
}
