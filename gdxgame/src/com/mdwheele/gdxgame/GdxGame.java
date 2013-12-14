package com.mdwheele.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mdwheele.gdxgame.screens.debug.DebugEventSoundScreen;

public class GdxGame extends Game {
	
	/* Window dimensions */
	public int width;
	public int height;
	
	/* Shared SpriteBatch */
	private SpriteBatch batch;
	
	/* FPS Debug Logger */
	FPSLogger fpsLogger;
	
	public GdxGame(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void create() {		
		batch = new SpriteBatch();
		
		setScreen(new DebugEventSoundScreen(this));
	}

	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
	}	
}
