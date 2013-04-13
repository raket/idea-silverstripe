package com.raket.silverstripe.eventdispatcher;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Storage class for queued events
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
class EventQueue {
    
   /**
    * All queued events
    */
    ArrayList<Event> events = new ArrayList<Event>();
    
   /**
    * Add a new event to the queue.
    * 
    * The inQueue property of the event will automatically
    * be set to 'true'.
    * 
    * @param e      Event that will be added
    */
    public void addEvent(Event e) {
        e.queueEvent();
        this.events.add(e);
    }

   /**
    * Get all queued events
    * 
    * @return   ArrayList with all events
    */
    public ArrayList getQueuedEvents() {
        return this.events;
    }
    
   /**
    * Get queued events of a specific event name
    * 
    * @param    eventName       name of the event
    * @return   queued events
    */
    public ArrayList getQueuedEvents(String eventName) {
        ArrayList<Event> qEvents = new ArrayList<Event>();
        
        for (Iterator iter = this.events.iterator(); iter.hasNext();) {
            Event e = (Event)iter.next();
            if (e.getName().equals(eventName)) {
                qEvents.add(e);
            }
        }
        return qEvents;
    }

    /**
     * Clear the event queue
     */
    public void clearQueue() {
    	this.events.clear();
    }
}