package com.raket.silverstripe.eventdispatcher;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Stores all event listeners for one event
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class EventListenerCollection {
 
   /**
    * All event listeners
    */
    private ArrayList<EventListenerContainer> listeners = new ArrayList<EventListenerContainer>();
    
   /**
    * Add a new event listener to the collection
    * 
    * @param listener       Listener to add
    * @return               amount of event listeners in this collection
    */
    public int addListener(EventListener listener, boolean autoRemove) {

        EventListenerContainer container = new EventListenerContainer(listener);
        container.enableAutoRemove(autoRemove);
        
        this.listeners.add(container);
        return this.listeners.size();
    }

   /**
    * Propagate an event to all listeners in this collection
    * 
    * @param e      event to propagate
    * @return       event
    */
    public Event propagate(Event e) {
        ArrayList<EventListener> remove = new ArrayList<EventListener>();
        for (int i = 0; i < this.listeners.size(); i++) {
            EventListenerContainer container = (EventListenerContainer)this.listeners.get(i);
            container.getListener().handleEvent(e);
            
            // remove the listener
            if (container.isAutoRemoveEnabled()) {
                remove.add(container.getListener());
            }
            if (e.isCancelled()) {
                break;
            }
        }
        // remove the listeners that have been set to autoRemove
        for (Iterator iter = remove.iterator(); iter.hasNext();) {
            EventListener listener = (EventListener) iter.next();
            this.removeListener(listener);
        }
        return e;
    }

   /**
    * Remove a listener from the list
    * 
    * @param index  index of the event listener   
    * @return
    */
    public EventListenerContainer removeListener(int index) {
        return (EventListenerContainer)this.listeners.remove(index);
    }

    /**
     * Remove a listener of a sepciefied class from the list
     * 
     * @param className
     * @return
     */
    public EventListenerContainer removeListener(String className) {
        for (Iterator iter = this.listeners.iterator(); iter.hasNext();) {
            EventListenerContainer container = (EventListenerContainer) iter.next();
            if (container.getListener().getClass().getName().equals(className)) {
                this.listeners.remove(container);
                return container;
            }
        }
        return null;
    }
    
    /**
     * Remove a listener from the list
     * 
     * If a listener has been added more the once, only the
     * first listener is removed
     * 
     * @param listener      listener to remove   
     * @return
     */
     public EventListenerContainer removeListener(EventListener listener) {
         for (Iterator iter = this.listeners.iterator(); iter.hasNext();) {
            EventListenerContainer container = (EventListenerContainer) iter.next();
            if (container.getListener().equals(listener)) {
                this.listeners.remove(container);
                return container;
            }
         }
         return null;
     }

     /**
      * Remove all listeners from this collection
      */
     public void removeAllListeners() {
    	 this.listeners.clear();
     }
     
     /**
      * Get an iterator to iterate over the event listeners in this
      * Collection
      * 
      * @return
      */
     public Iterator iterator() {
         return this.listeners.iterator();
     }

	public int size() {
		return listeners.size();
	}
}