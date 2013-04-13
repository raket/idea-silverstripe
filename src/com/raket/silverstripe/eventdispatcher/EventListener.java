package com.raket.silverstripe.eventdispatcher;

/**
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public interface EventListener {
    public void handleEvent(Event e);
}
