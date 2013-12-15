package com.mdwheele.gdxgame.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;


public abstract class InputContext {
	
	protected HashMap<Integer, InputAction> keyBindings;
	protected ArrayList<InputAction> mappedActions;
	
	public InputContext() {
		keyBindings = new HashMap<Integer, InputAction>();
		mappedActions = new ArrayList<InputAction>();
	}
		
	public void setBinding(Integer key, InputAction action) {
		keyBindings.put(key, action);
	}
		
	public void setBinding(InputAction action, int... keys) {
		for(int key: keys) {
			keyBindings.put(key, action);
		}
	}
	
	public InputAction getBinding(int key) {		
		return keyBindings.get(key);
	}
	
	public ArrayList<InputAction> getMappedActions() {
		return mappedActions;
	}
	
	public void clearMappedActions() {
		mappedActions.clear();
	}
	
	public void process() {
		boolean anyKeyPressed = false;
		
		for(Map.Entry<Integer, InputAction> binding : keyBindings.entrySet()) {
			if(Gdx.input.isKeyPressed(binding.getKey())) {
				mappedActions.add(binding.getValue());
				anyKeyPressed = true;
			}
		}
		
		if(!anyKeyPressed) {
			mappedActions.add(InputAction.STOP);
		}
	}
}
