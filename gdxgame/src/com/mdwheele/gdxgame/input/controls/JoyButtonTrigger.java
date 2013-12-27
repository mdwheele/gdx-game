package com.mdwheele.gdxgame.input.controls;

import com.badlogic.gdx.controllers.PovDirection;

public class JoyButtonTrigger implements Trigger {
	
	private int buttonId;

	public JoyButtonTrigger(int buttonId) {
		this.buttonId = buttonId;
	}
	
	public JoyButtonTrigger(PovDirection buttonId) {
		this.buttonId = buttonId.hashCode();
	}
	
	public String getName() {
		return "JoyButton " + buttonId;
	}
	
	public int getButtonIndex() {
		return this.buttonId;
	}
	
	public static int keyHash(int buttonId) {
		return 1024 | buttonId;
	}
	
    public int hashCode() {
        return keyHash(buttonId);
    }
}
