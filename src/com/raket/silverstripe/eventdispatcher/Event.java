package com.raket.silverstripe.eventdispatcher;

/**
 * Event class, that contains information about a generic event.
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class Event {
    private String name       =  null;
    private boolean cancelled = false;
    private boolean inQueue   = false;
    private Object context    = null;
    private Object userInfo   = null;

   /**
    * Create a new event
    * 
    * @param name       name of the event
    */
    public Event(String name) {
        this.name = name;
    }

    /**
     * Create a new event
     * 
     * @param name       name of the event
     * @param context    context of the event
     */
    public Event(String name, Object context) {
        this.name    = name;
        this.context = context;
    }

    /**
     * Create a new event
     * 
     * @param name       name of the event
     * @param context    context of the event
     * @param userInfo   user info for the event
     */
    public Event(String name, Object context, Object userInfo) {
        this.name     = name;
        this.context  = context;
        this.userInfo = userInfo;
    }

   /**
    * Cancel the event
    */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Flag the event as queued
     */
     public void queueEvent() {
         this.inQueue = true;;
     }
    
   /**
    * Check, whether the event has been cancelled
    * 
    * @return       true, if the event has been cancelled, false otherwise
    */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Check, whether the event already is in a queue
     * 
     * @return       true, if the event is queued, false otherwise
     */
     public boolean isQueued() {
         return this.inQueue;
     }
    
   /**
    * Get the name of the event
    * 
    * @return   name of the event
    */
    public String getName() {
        return this.name;
    }

   /**
    * Get the event context
    * 
    * @return   Context of the ecent
    */
    public Object getContext() {
        return this.context;
    }

   /**
    * Get the (optional) user info
    * 
    * @return   any user info that has been passed to the constructor
    */
    public Object getUserInfo() {
        return this.userInfo;
    }
}