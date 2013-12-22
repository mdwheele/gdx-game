package com.mdwheele.gdxgame.input.event;

public abstract class InputEvent {
	protected long time;
	protected boolean consumed = false;
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public boolean isConsumed() {
		return consumed;
	}
	
	public void setConsumed() {
		this.consumed = true;
	}
}
