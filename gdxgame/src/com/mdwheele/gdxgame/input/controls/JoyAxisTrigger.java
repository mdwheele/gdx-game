package com.mdwheele.gdxgame.input.controls;


public class JoyAxisTrigger implements Trigger {
	
	private int axisId;
	private boolean negative;

	public JoyAxisTrigger(int axisId, boolean negative) {
		this.axisId = axisId;
		this.negative = negative;
	}
	
	public String getName() {
		return "JoyAxis " + axisId;
	}
	
	public int getButtonIndex() {
		return this.axisId;
	}
	
	public static int joyAxisHash(int axisId, boolean negative) {
		return 2048 | axisId | (negative ? 1200 : 1024);
	}
	
    public int hashCode() {
        return joyAxisHash(axisId, negative);
    }
}
