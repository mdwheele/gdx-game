package com.mdwheele.gdxgame.screens;

import com.artemis.World;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.systems.RenderSystem;

public class GameScreen extends AbstractScreen {
	
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	World world;
	
	public GameScreen(final GdxGame game) {
		super(game);
				
		map = new TmxMapLoader().load("maps/testmap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f, this.batch);
		
		world = new World();		
		world.setSystem(new RenderSystem(this.camera));
		world.initialize();	
		
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
				
		mapRenderer.setView(this.camera);
		mapRenderer.render();
		
		world.setDelta(delta);
		world.process();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		camera.setToOrtho(false, 20, 15);
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
