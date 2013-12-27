package com.mdwheele.gdxgame.input.event;

import com.badlogic.gdx.controllers.PovDirection;


public class JoyButtonEvent extends InputEvent {
	private int buttonId;
	private boolean pressed;
	
	public JoyButtonEvent(int buttonId, boolean pressed) {
		this.buttonId = buttonId;
		this.pressed = pressed;
	}
	
	public JoyButtonEvent(PovDirection buttonId, boolean pressed) {
		this.buttonId = buttonId.hashCode();
		this.pressed = pressed;
	}
	
	public int getButtonId() {
		return buttonId;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isReleased() {
		return !pressed;
	}
}
