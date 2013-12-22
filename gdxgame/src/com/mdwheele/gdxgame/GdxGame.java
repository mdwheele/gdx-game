package com.mdwheele.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.screens.GameScreen;

public class GdxGame extends Game {
	
	/* Level Path */
	public String levelPath;
	
	/* Window dimensions */
	public int width;
	public int height;
	
	/* SpriteBatch */
	public SpriteBatch batch;
	public BitmapFont font;

	public static int LogLevel = Logger.DEBUG;
	public Logger logger = new Logger("GdxGame", GdxGame.LogLevel);
	
	public GdxGame(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.levelPath = "maps/debug-map.tmx";
	}
	
	public void create() {		
		logger.info("Initializing Game Resources.");
		batch = new SpriteBatch();
		font = new BitmapFont();		
		
		logger.info("Setting up screen.");
		setScreen(new GameScreen(this, levelPath));
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
	}	
	
	public boolean isDebug() {
		return logger.getLevel() == Logger.DEBUG;
	}
}
