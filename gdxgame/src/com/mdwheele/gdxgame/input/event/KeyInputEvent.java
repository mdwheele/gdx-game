package com.mdwheele.gdxgame.input.event;

public class KeyInputEvent extends InputEvent {
	private int keyCode;
	private boolean pressed;
	
	public KeyInputEvent(int keyCode, boolean pressed) {
		this.keyCode = keyCode;
		this.pressed = pressed;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isReleased() {
		return !pressed;
	}
}
