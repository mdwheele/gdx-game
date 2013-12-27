package com.mdwheele.gdxgame.input.event;

import com.badlogic.gdx.controllers.PovDirection;

public class JoyAxisEvent extends InputEvent {
	private int axisId;
	private float value;
	private boolean negative;
	
	public JoyAxisEvent(int axisId, float value, boolean negative) {
		this.axisId = axisId;
		this.value = value;
		this.negative = negative;
	}
	
	public int getAxisId() {
		return axisId;
	}
	
	public float getValue() {
		return value;
	}
	
	public boolean isNegative() {
		return negative;
	}
	
}
