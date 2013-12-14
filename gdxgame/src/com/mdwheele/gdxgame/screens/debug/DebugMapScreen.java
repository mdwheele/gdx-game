package com.mdwheele.gdxgame.screens.debug;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.screens.AbstractScreen;

public class DebugMapScreen extends AbstractScreen {
	
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	public DebugMapScreen(final GdxGame game) {
		super(game);
				
		map = new TmxMapLoader().load("maps/testmap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
				
	
		
		mapRenderer.setView(this.camera);
		mapRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		camera.setToOrtho(false, 16, 8);
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
		map.dispose();
	}

}
