package com.mdwheele.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mdwheele.gdxgame.GdxGame;

public abstract class AbstractScreen implements Screen {

	protected final GdxGame game;	
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	
	public AbstractScreen(final GdxGame game) {
		this.game = game;
		camera = new OrthographicCamera(game.width, game.height);
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
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
