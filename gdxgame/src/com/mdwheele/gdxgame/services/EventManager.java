package com.mdwheele.gdxgame.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.mdwheele.gdxgame.events.Event;

public class EventManager {
	private HashMap<Class<? extends Event>, ArrayList<Object>> eventListeners;
	private LinkedList<Event> eventPool;
	
	public EventManager() {
		this.eventListeners = new HashMap<Class<? extends Event>, ArrayList<Object>>();
		this.eventPool = new LinkedList<Event>();
	}
	
	/**
	 * Subscribe a listener to a particular event.
	 * 
	 * @param event
	 * @param eventListener
	 */
	public void subscribe(Class<? extends Event> event, Object listener) {
		ArrayList<Object> listeners = this.eventListenersForEvent(event);
		listeners.add(listener);
	}
	
	/**
	 * Unsubscribe specified listener from listening events with the specified event.
	 * 
	 * @param event
	 * @param eventListener
	 */
	public void unsubscribe(Class<? extends Event> event, Object listener) {
		ArrayList<Object> listeners = this.eventListenersForEvent(event);
		listeners.remove(listener);
	}
	
	/**
	 * Unregister specified listener from all events it was registered for.
	 * @param eventListener
	 */
	public void unsubscribe(Object listener) {
		Set<Class<? extends Event>> keySet = eventListeners.keySet();
		
		for (Class<? extends Event> key: keySet)
			eventListeners.get(key).remove(listener);
	}
	
	/**
	 * Get all event listeners for an event class.
	 * 
	 * @param event Event class.
	 * @return ArrayList of listeners.
	 */
	private ArrayList<Object> eventListenersForEvent(Class<? extends Event> event) {
		if (!eventListeners.containsKey(event)) {
			eventListeners.put(event,  new ArrayList<Object>());
		}
		
		return eventListeners.get(event);
	}
	
	/**
	 * Submits a new event specifying event id and the object sending that event.
	 * 
	 * @param eventId the id of the event
	 * @param eventSender the object which generated the event or any other data
	 */
	public void post(Event event) {
		if(event != null)
			this.eventPool.offer(event);
	}
	
	/**
	 * Process all events in pool.
	 */
	public void process() {
		while(eventPool.isEmpty() == false) {
			Event event = eventPool.poll();
			
			for (Object listener: eventListeners.get(event.getClass())) {
				try {
					Method method = listener.getClass().getMethod("handleEvent", event.getClass());
					
					try {
						method.invoke(listener, event);
					}
					catch (InvocationTargetException e) {}
					catch (IllegalAccessException e) {}
				}
				catch (NoSuchMethodException e) {}
			}
		}
	}
}
