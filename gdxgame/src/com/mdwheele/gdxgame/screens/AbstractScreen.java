package com.mdwheele.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.input.InputManager;

public abstract class AbstractScreen implements Screen {

	public final GdxGame game;	
	public OrthographicCamera camera;
	public InputManager input;
	
	public AbstractScreen(final GdxGame game) {
		this.game = game;
		this.input = new InputManager();
		
		camera = new OrthographicCamera(game.width, game.height);
		camera.setToOrtho(false, game.width, game.height);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		camera.update();		
		input.update(delta);
		
		game.batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void resize(int width, int height) {
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
