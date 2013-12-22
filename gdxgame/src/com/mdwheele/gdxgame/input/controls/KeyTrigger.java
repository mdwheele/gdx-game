package com.mdwheele.gdxgame.input.controls;

public class KeyTrigger implements Trigger {
	
	private int keyCode;
	
	public KeyTrigger(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public String getName() {
		return "KeyCode " + keyCode;
	}
	
	public static int keyHash(int keyCode) {
		return keyCode;
	}
	
    public int hashCode() {
        return keyHash(keyCode);
    }
}
