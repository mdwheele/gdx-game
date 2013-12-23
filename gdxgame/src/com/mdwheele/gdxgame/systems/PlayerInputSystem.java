package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.components.PlayerComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.input.InputManager;
import com.mdwheele.gdxgame.input.controls.ActionListener;
import com.mdwheele.gdxgame.input.controls.KeyTrigger;
import com.mdwheele.gdxgame.level.GameWorld;

public class PlayerInputSystem extends EntityProcessingSystem implements ActionListener {
	@Mapper ComponentMapper<PlayerComponent> player;
	@Mapper ComponentMapper<SpatialComponent> sm;
	
	BitmapFont font;
	SpriteBatch batch;
	InputManager input;
	com.badlogic.gdx.physics.box2d.World box2dworld;

	private static final Logger logger = new Logger(PlayerInputSystem.class.getSimpleName());
	
	private boolean left;
	private boolean right;
	private boolean jump;
	private boolean grounded;
		
	public PlayerInputSystem(GameWorld gameWorld) {
		super(Aspect.getAspectForAll(PlayerComponent.class, SpatialComponent.class));
		
		this.font = gameWorld.gameScreen.getFont();
		this.batch = gameWorld.gameScreen.getSpriteBatch();
		this.box2dworld = gameWorld.box2dWorld;
		this.input = gameWorld.gameScreen.input;
		
		box2dworld.setContactListener(sensorListener);
		
		input.addMapping("Left", new KeyTrigger(Keys.A), new KeyTrigger(Keys.LEFT));
		input.addMapping("Right", new KeyTrigger(Keys.D), new KeyTrigger(Keys.RIGHT));
		input.addMapping("Jump", new KeyTrigger(Keys.SPACE), new KeyTrigger(Keys.W));
		input.addListener(this, new String[]{"Left", "Right", "Jump"});
	}

	@Override
	protected void process(Entity e) {		
		SpatialComponent spatial = sm.get(e);
		Body body = spatial.getBody();
		
		// Handle Movement
		Vector2 currentVelocity = body.getLinearVelocity();		
		float maxVelocity = spatial.getMaxVelocity();
		
		Vector2 movement = new Vector2(0, 0);
				
		if(left) {
			movement.x -= 1;
		}
		
		if(right) {
			movement.x += 1;
		}
		
		if(movement.len() > 0) {
			movement.nor();
			movement.scl(maxVelocity);	
			movement.scl(GameWorld.toBox2d(1));
			movement.sub(new Vector2(currentVelocity.x, 0));		

			body.applyLinearImpulse(movement, body.getWorldCenter(), true);
		}
		
		// Handle Jumping
		if(jump && grounded) {
			Vector2 force = new Vector2(0, 0);
			force.y = (float)Math.sqrt(-2 * box2dworld.getGravity().y * GameWorld.toBox2d(6.1f));
			
			body.applyLinearImpulse(force.scl(body.getMass()), body.getWorldCenter(), true);
		} 
		
		// Set friction to zero if in the air.
		if(!grounded) {
			body.getFixtureList().get(0).setFriction(0.0f);
		}
		else {
			if(!left && !right) {
				body.getFixtureList().get(0).setFriction(100.0f);
			}
			else {
				body.getFixtureList().get(0).setFriction(0.2f);
			}
		}
		
		
		// Debug Drawing
		batch.begin();
		String text = String.format("Grounded: %s Jump: %s Left: %s Right: %s Friction: %s", grounded, jump, left, right, ((Fixture)body.getFixtureList().get(0)).getFriction());
		font.draw(batch, text, GameWorld.toWorld(body.getPosition().x) - font.getBounds(text).width / 2, GameWorld.toWorld(body.getPosition().y) + 48);
		batch.end();
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if(name.equals("Left")) {
			left = isPressed;
		}
		
		if(name.equals("Right")) {
			right = isPressed;
		}
		
		if(name.equals("Jump")) {
			jump = isPressed;
		}
	}
	
	ContactListener sensorListener = new ContactListener() {
		@Override
		public void beginContact(Contact contact) {
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(3)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(3))
			) {
				grounded = true;
			}
		}

		@Override
		public void endContact(Contact contact) {
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(3)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(3))
			) {
				grounded = false;
			}
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			contact.resetFriction();
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
		}
	};
}
