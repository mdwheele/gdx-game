package com.mdwheele.gdxgame.screens.debug;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.screens.AbstractScreen;

public class DebugBox2dScreen extends AbstractScreen {
		
	/* Box2d World */
	World world;
	
	/* Scaling factors */
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	/* Maximum number of balls */
	static final int BALL_COUNT = 100;
	
	/**
	 * Box2d Debug Renderer
	 * 
	 * Draws shapes and graphics for collisions and other things Box2d related.
	 */
	Box2DDebugRenderer debugRenderer;
	
	public DebugBox2dScreen(final GdxGame game) {
		super(game);
		
		/* Instantiate Box2d physics world and debug renderer. */
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
		
		/* Create the floor! */
		createFloor();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		world.step(1/60f, 6, 2);
		
		/* Add ball to random x location in air */
		if(world.getBodyCount() < BALL_COUNT) 
			createBall((float)(new Random()).nextInt(game.width));
		
		debugRenderer.render(world, camera.combined);
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
	}
	
	private void createBall(float x) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		
		// Set our body's starting position in the world
		bodyDef.position.set(x * WORLD_TO_BOX, 1000 * WORLD_TO_BOX);

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(16 * WORLD_TO_BOX);

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
	
	private void createFloor() {
		// Create our body definition
		BodyDef groundBodyDef =new BodyDef();  
		
		// Set its world position		
		groundBodyDef.position.set(new Vector2(0, 32 * WORLD_TO_BOX));  

		// Create a body from the defintion and add it to the world
		Body groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(camera.viewportWidth, 16 * WORLD_TO_BOX);
		
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		
		// Clean up after ourselves
		groundBox.dispose();
	}

}
