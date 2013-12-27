package com.mdwheele.gdxgame.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.input.controls.ActionListener;
import com.mdwheele.gdxgame.screens.GameScreen;
import com.mdwheele.gdxgame.systems.CameraSystem;
import com.mdwheele.gdxgame.systems.PlayerInputSystem;

public class GameWorld implements Disposable, ActionListener {
	
	// Tilemap Resources
	public TiledMap tiledMap;
	public TiledMapRenderer tiledMapRenderer;
	
	// Artemis Entity World
	public com.artemis.World artemisWorld;
	
	// Box2d Physics
	public World box2dWorld;
	
	// GameScreen reference
	public GameScreen gameScreen;
	
	// Debug Resources
	private static final Logger logger = new Logger(GameWorld.class.getSimpleName());
	public Box2DDebugRenderer box2dRenderer;
	
	public GameWorld(GameScreen gameScreen) {
		// Set up logger interface.
		logger.setLevel(GdxGame.LogLevel);
		logger.info("Initializing...");
		
		// Store reference to game screen.
		this.gameScreen = gameScreen;

        // Create Box2d World        
        box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -30.0f), true);
		
		// Create Artemis Entity World
		artemisWorld = new com.artemis.World();
		
		// Box2d Renderer
		box2dRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if(isPressed) {
			logger.info(name);
		}
	}
	
	public void start() {
		artemisWorld.setSystem(new PlayerInputSystem(this));
		artemisWorld.setSystem(new CameraSystem(gameScreen.camera));
		artemisWorld.initialize();	
	}
	
	public void update(float delta) {
		// Step physics simulation.
		box2dWorld.step(1/60f, 6, 2);
		
		// Update entity system.
		artemisWorld.setDelta(delta);
		artemisWorld.process();
		
		// Render Map!
		tiledMapRenderer.setView(this.gameScreen.camera);
		tiledMapRenderer.render();
		
		if(gameScreen.game.isDebug()) {
			box2dRenderer.render(box2dWorld, this.gameScreen.camera.combined.scl(GameWorld.toWorld(1f)));
		}
	}
	
	public void load(String mapFileName) {
		logger.info(String.format("Loading %s", mapFileName));
		
		// Create new level loader.
		LevelLoader levelLoader = new LevelLoader(this);	
		tiledMap = levelLoader.load(mapFileName);
		
		if(tiledMap == null) {
			logger.error("Error loading map.");
		}
		
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1, gameScreen.game.batch);			
	}
	
	@Override
	public void dispose() {
		tiledMap.dispose();
		box2dWorld.dispose();
	}
	
	public static final float toWorld(float box2d) {
		return box2d * 32.0f;
	}
	
	public static final float toBox2d(float world) {
		return world / 32.0f;
	}	
}
