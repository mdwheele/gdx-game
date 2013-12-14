package com.mdwheele.gdxgame.screens.debug;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.LevelLoader;
import com.mdwheele.gdxgame.screens.AbstractScreen;

public class DebugMapPhysicsScreen extends AbstractScreen {

	/* Box2d world and renderer */
	World world;
	Box2DDebugRenderer physicsRenderer;
	
	/* Tiled map and renderer */
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	/* Level Loader */
	LevelLoader levelLoader;
	
	BitmapFont font;
	
	/* Maximum number of balls */
	static final int BALL_COUNT = 250;
	
	public DebugMapPhysicsScreen(final GdxGame game) {
		super(game);
		
		/* Instantiate Box2d physics world and debug renderer. */
		world = new World(new Vector2(0, -10), true);
		physicsRenderer = new Box2DDebugRenderer();
					
		/* Load map */
		map = new TmxMapLoader().load("maps/testmap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
		
		font = new BitmapFont();
		
		/* Populate physics world with data from map file. */
		levelLoader = new LevelLoader(map, world, 1 / 16f);		
		levelLoader.createPhysics();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		world.step(1/60f, 10, 2);
		
		/* Add ball to random x location in air */
		if(world.getBodyCount() < BALL_COUNT) 
			createBall((float)(new Random()).nextInt(320));
		
		mapRenderer.setView(this.camera);
		mapRenderer.render();
		
		physicsRenderer.render(world, camera.combined);
		
		batch.begin();
		font.draw(batch, "Body Count: " + world.getBodyCount(), 16, 700);
		batch.end();
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

	private void createBall(float x) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		
		// Set our body's starting position in the world
		bodyDef.position.set(x * 1 / 16f, 500 * 1 / 16f);

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(2 * 1 / 16f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.1f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
	}
	
}
