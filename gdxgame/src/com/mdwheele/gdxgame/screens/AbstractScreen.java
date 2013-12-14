package com.mdwheele.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.input.LocalWorldInputContext;
import com.mdwheele.gdxgame.services.EventManager;
import com.mdwheele.gdxgame.services.InputManager;
import com.mdwheele.gdxgame.services.SoundManager;

public abstract class AbstractScreen implements Screen {

	protected final GdxGame game;	
	protected OrthographicCamera camera;
	protected SpriteBatch batch;

	protected SoundManager soundManager;
	protected EventManager eventManager;
	protected InputManager inputManager;
	
	public AbstractScreen(final GdxGame game) {
		this.game = game;
		camera = new OrthographicCamera(game.width, game.height);
		batch = new SpriteBatch();
		
		/**
		 * Instantiate sound and event services.
		 */
		soundManager = new SoundManager();
		eventManager = new EventManager();
		inputManager = new InputManager(eventManager);
		
		inputManager.setContext(new LocalWorldInputContext());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		/**
		 * Tell event manager to process current event pool.
		 */
		inputManager.process();
		eventManager.process();
	}

	@Override
	public void resize(int width, int height) {
		game.width = width;
		game.height = height;
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
