package com.mdwheele.gdxgame.services;

import com.badlogic.gdx.Gdx;
import com.mdwheele.gdxgame.events.InputActionEvent;
import com.mdwheele.gdxgame.input.InputAction;
import com.mdwheele.gdxgame.input.InputContext;

public class InputManager {
	private EventManager eventManager;	
	private InputContext activeContext = null;
	
	public InputManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}
	
	public void setContext(InputContext context) {
		activeContext = context;		
		Gdx.input.setInputProcessor(activeContext);	
	}
	
	public void process() {
		for(InputAction action: activeContext.getMappedActions()) {
			eventManager.post(new InputActionEvent(action));
		}
		
		activeContext.clearMappedActions();
	}
}
