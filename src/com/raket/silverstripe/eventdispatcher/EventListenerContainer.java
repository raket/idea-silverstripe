package com.raket.silverstripe.eventdispatcher;

/**
 * Container for an event listener that provides
 * some additional configuration for the event listener
 * 
 * Currently it only stores the the autoRemove flag,
 * but this will probably extended in future versions
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class EventListenerContainer {

    /**
     * The actual event listener
     */
    private EventListener listener = null;
    
    /**
     * Whether to use autoRemove
     */
    private boolean autoRemove = false;
    
    /**
     * Create a new event listener container based
     * on an event listener
     * 
     * @param listener
     */
    public EventListenerContainer(EventListener listener) {
        this.listener = listener;
    }
    
    /**
     * Enable auto-remove for this listener
     * 
     * @param enable
     */
    public void enableAutoRemove(boolean enable) {
        this.autoRemove = enable;
    }
    
    /**
     * Check, whether auto-remove has been enabled.
     * 
     * @return
     */
    public boolean isAutoRemoveEnabled() {
        return this.autoRemove;
    }

    /**
     * Get the event listener
     * 
     * @return
     */
    public EventListener getListener() {
        return this.listener;
    }
}