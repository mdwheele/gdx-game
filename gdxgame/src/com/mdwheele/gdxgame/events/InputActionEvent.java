package com.mdwheele.gdxgame.events;

import com.mdwheele.gdxgame.input.InputAction;

public class InputActionEvent extends Event {
	private InputAction action;
	
	public InputActionEvent(InputAction action) {
		this.action = action;
	}
	
	public InputAction action() {
		return this.action;
	}
}
