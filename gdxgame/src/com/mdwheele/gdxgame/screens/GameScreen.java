package com.mdwheele.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.level.GameWorld;

public class GameScreen extends AbstractScreen {
	
	// The current level being played.
	private GameWorld currentLevel;
	
	public GameScreen(final GdxGame game, String levelPath) {
		super(game);
		
		currentLevel = new GameWorld(this);
		currentLevel.load(levelPath);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);	
		
		currentLevel.update(delta);
				
		// Exit game hotkey... fix.
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		// Show FPS if game is in debug mode.
		if(game.isDebug()) {
			showFps();
		}
	}
	
	public void showFps() {
		game.batch.begin();
		this.game.font.draw(game.batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 10, 20);
		game.batch.end();	
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);		
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
