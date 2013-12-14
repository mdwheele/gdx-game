package com.mdwheele.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mdwheele.gdxgame.screens.GameScreen;

public class GdxGame extends Game {
	
	/* Level Path */
	public String levelPath;
	
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
		this.levelPath = "maps/debug-map.tmx";
	}
	
	public void create() {		
		batch = new SpriteBatch();
		
		setScreen(new GameScreen(this, levelPath));
	}

	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
	}	
}
