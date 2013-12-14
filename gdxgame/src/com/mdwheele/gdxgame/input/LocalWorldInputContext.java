package com.mdwheele.gdxgame.input;

import com.badlogic.gdx.Input.Keys;

public class LocalWorldInputContext extends InputContext {   
	
	public LocalWorldInputContext() {
		super();

		setBinding(InputAction.MOVE_LEFT, Keys.A, Keys.LEFT);
		setBinding(InputAction.MOVE_UP, Keys.W, Keys.UP);
		setBinding(InputAction.MOVE_RIGHT, Keys.D, Keys.RIGHT);
		setBinding(InputAction.MOVE_DOWN, Keys.S, Keys.DOWN);
	}
		
	public void setBinding(InputAction action, int... keys) {
		for(int key: keys) {
			keyBindings.put(key, action);
		}
	}
	
	public InputAction getBinding(Keys key) {		
		return keyBindings.get(key);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keyBindings.containsKey(keycode))
			mappedActions.add(keyBindings.get(keycode));		

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
