package com.mdwheele.gdxgame.screens.debug;

import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.events.InputActionEvent;
import com.mdwheele.gdxgame.input.InputAction;
import com.mdwheele.gdxgame.input.LocalWorldInputContext;
import com.mdwheele.gdxgame.screens.AbstractScreen;
import com.mdwheele.gdxgame.services.EventManager;
import com.mdwheele.gdxgame.services.InputManager;
import com.mdwheele.gdxgame.services.SoundManager;
import com.mdwheele.gdxgame.services.SoundManager.GameSound;

public class DebugEventSoundScreen extends AbstractScreen {
	
	SoundManager soundManager;
	EventManager eventManager;
	InputManager inputManager;
	
	public DebugEventSoundScreen(final GdxGame game) {
		super(game);
		
		/**
		 * Instantiate sound and event services.
		 */
		soundManager = new SoundManager();
		eventManager = new EventManager();
		inputManager = new InputManager(eventManager);
		
		inputManager.setContext(new LocalWorldInputContext());
		
		/**
		 * Subscribe this class to the event manager.
		 */
		eventManager.subscribe(InputActionEvent.class, this);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		/**
		 * Tell event manager to process current event pool.
		 */
		inputManager.process();
		eventManager.process();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.setToOrtho(false, 20, 16);
	}
	
	public void handleEvent(InputActionEvent event) {
		/**
		 * Play the sound!
		 */
		if(event.action() == InputAction.MOVE_UP)
			soundManager.play(GameSound.ARROW_SHOT);
		
		if(event.action() == InputAction.MOVE_LEFT || event.action() == InputAction.MOVE_RIGHT)
			soundManager.play(GameSound.ARROW_SHOT, 0.5f);
		
		if(event.action() == InputAction.MOVE_DOWN)
			soundManager.play(GameSound.ARROW_SHOT, 0.1f);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {	
	}

	@Override
	public void dispose() {
	}
}
