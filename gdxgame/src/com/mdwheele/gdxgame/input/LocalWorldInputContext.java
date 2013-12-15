package com.mdwheele.gdxgame.input;

import com.badlogic.gdx.Input.Keys;

public class LocalWorldInputContext extends InputContext {   
	
	public LocalWorldInputContext() {
		super();

		setBinding(InputAction.MOVE_LEFT, Keys.A, Keys.LEFT);
		setBinding(InputAction.MOVE_UP, Keys.W, Keys.UP);
		setBinding(InputAction.MOVE_RIGHT, Keys.D, Keys.RIGHT);
		setBinding(InputAction.MOVE_DOWN, Keys.S, Keys.DOWN);
		setBinding(InputAction.JUMP, Keys.SPACE);
		setBinding(InputAction.ATTACK, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT);
		setBinding(InputAction.QUIT, Keys.ESCAPE);
	}
}
