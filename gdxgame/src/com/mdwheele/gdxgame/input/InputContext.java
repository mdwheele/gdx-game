package com.mdwheele.gdxgame.input;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.InputProcessor;


public abstract class InputContext implements InputProcessor {
	
	protected HashMap<Integer, InputAction> keyBindings;
	protected ArrayList<InputAction> mappedActions;
	
	public InputContext() {
		keyBindings = new HashMap<Integer, InputAction>();
		mappedActions = new ArrayList<InputAction>();
	}
		
	public void setBinding(Integer key, InputAction action) {
		keyBindings.put(key, action);
	}
	
	public InputAction getBinding(Integer key) {		
		return keyBindings.get(key);
	}
	
	public ArrayList<InputAction> getMappedActions() {
		return mappedActions;
	}
	
	public void clearMappedActions() {
		mappedActions.clear();
	}
}
