package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
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
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.components.PlayerComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.input.InputManager;
import com.mdwheele.gdxgame.input.controls.ActionListener;
import com.mdwheele.gdxgame.input.controls.AnalogListener;
import com.mdwheele.gdxgame.input.controls.JoyAxisTrigger;
import com.mdwheele.gdxgame.input.controls.JoyButtonTrigger;
import com.mdwheele.gdxgame.input.controls.KeyTrigger;
import com.mdwheele.gdxgame.input.controls.Xbox360Pad;
import com.mdwheele.gdxgame.level.GameWorld;

public class PlayerInputSystem extends EntityProcessingSystem implements ActionListener {
	@Mapper ComponentMapper<PlayerComponent> pm;
	@Mapper ComponentMapper<SpatialComponent> sm;
	
	BitmapFont font;
	SpriteBatch batch;
	InputManager input;
	com.badlogic.gdx.physics.box2d.World box2dworld;
	
	// Actions
	private boolean movingLeft;
	private boolean movingRight;
	private boolean boosting;
	private boolean jumping;
	
	// "States"
	private boolean grounded;
	private boolean leftWallTouching;
	private boolean rightWallTouching;
	
	// Used for n-jumping.
	private int jumpCounter = 0;
	private int maxJumps = 1;
	private boolean canJumpAgain = true;
		
	public PlayerInputSystem(GameWorld gameWorld) {
		super(Aspect.getAspectForAll(PlayerComponent.class, SpatialComponent.class));
		
		this.font = gameWorld.gameScreen.getFont();
		this.batch = gameWorld.gameScreen.getSpriteBatch();
		this.box2dworld = gameWorld.box2dWorld;
		this.input = gameWorld.gameScreen.input;
		
		box2dworld.setContactListener(sensorListener);
		
		input.addMapping(
				"Left", 
				new KeyTrigger(Keys.A), 
				new KeyTrigger(Keys.LEFT), 
				new JoyButtonTrigger(Xbox360Pad.BUTTON_DPAD_LEFT), 
				new JoyAxisTrigger(Xbox360Pad.AXIS_LEFT_X, true)
		);
		
		input.addMapping(
				"Right", 
				new KeyTrigger(Keys.D), 
				new KeyTrigger(Keys.RIGHT), 
				new JoyButtonTrigger(Xbox360Pad.BUTTON_DPAD_RIGHT), 
				new JoyAxisTrigger(Xbox360Pad.AXIS_LEFT_X, false)
		);
		
		input.addMapping(
				"Boost", 
				new KeyTrigger(Keys.SHIFT_LEFT),
				new JoyButtonTrigger(Xbox360Pad.BUTTON_X)
		);
		
		input.addMapping(
				"Jump", 
				new KeyTrigger(Keys.SPACE), 
				new KeyTrigger(Keys.W), 
				new JoyButtonTrigger(Xbox360Pad.BUTTON_A)
		);
		
		input.addListener(this, new String[]{"Left", "Right", "Boost", "Jump"});
	}
	
	@Override
	protected void process(Entity e) {		
		PlayerComponent player = pm.get(e);
		SpatialComponent spatial = sm.get(e);
		Body body = spatial.getBody();
		
		// Handle Movement
		Vector2 currentVelocity = body.getLinearVelocity();		
		float maxVelocity = player.getMaxVelocity(boosting);
		
		Vector2 movement = new Vector2(0, 0);
				
		if(movingLeft && !leftWallTouching) {			
			movement.x -= 1;
		}
		
		if(movingRight && !rightWallTouching) {
			movement.x += 1;
		}
		
		if(movement.len() > 0) {
			// We have movement direction, but we need to calculate magnitude.
			movement.nor();
			
			// Our desired magnitude every step is the current velocity magnitude plus an acceleration factor.
			float desiredMagnitude = 0;
			
			// OH GOD THE HACKS, DEAR GOD NOOOOO!!!  Basically... if we're on the ground... turning should be sharp.
			// If in the air, we want to "step" towards the direction.
			if(grounded) {
				desiredMagnitude = Math.min(Math.abs(currentVelocity.x) + 0.8f, GameWorld.toBox2d(maxVelocity));
			}
			else {
				if(movingLeft) {
					desiredMagnitude = -1.0f * Math.max(currentVelocity.x - 0.8f, GameWorld.toBox2d(maxVelocity) * -0.8f);
				}
				else if(movingRight) {
					desiredMagnitude = Math.min(currentVelocity.x + 0.8f, GameWorld.toBox2d(maxVelocity) * 0.8f);
				}
			}
			
			// Scale our movement vector (which already has direction) with the desired magnitude calculated above. 
			movement.scl(desiredMagnitude);
			
			// Subtract our current velocity from the vector (which caps us at maximum velocity rather than maxVelocity + currentVelocity)
			movement.sub(new Vector2(currentVelocity.x, 0));
			
			// Apply movement impulse to body.
			body.applyLinearImpulse(movement, body.getWorldCenter(), true);
		}
		
		// Handle Jumping		
		if(jumping && canJumpAgain && ((jumpCounter < maxJumps) || leftWallTouching || rightWallTouching)) {
			Vector2 force = new Vector2(0, 0);
			force.x = body.getLinearVelocity().x;
			force.y = (float)Math.sqrt(-2 * box2dworld.getGravity().y * GameWorld.toBox2d(300f));
			
			if(!grounded) {
				// Add horizontal velocity if wall jumping.  Character should jump away from wall.
				Vector2 wallJumpForce = new Vector2(0, 0);
				
				if(leftWallTouching) {
					wallJumpForce.x = 1;
				}
				else if(rightWallTouching) {
					wallJumpForce.x = -1;
				}
				
				wallJumpForce.scl(maxVelocity * 0.025f);
				
				force.add(wallJumpForce);
			}
			
			body.setLinearVelocity(force);
			canJumpAgain = false;
			jumpCounter++;
		} 
		
		if(!jumping) {
			canJumpAgain = true;

			// If they are in the air and not pressing the jump button, we will cut their Y-velocity 
			// by a multiplier if they are going up.
			if(!grounded && body.getLinearVelocity().y > 0) {				
				body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 0.75f);
			}
		}
		
		if(!grounded) {
			// Set friction to zero if in the air to prevent getting stuck on sides of things.
			body.getFixtureList().get(0).setFriction(0.0f);
			
			if(leftWallTouching) {
				body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
				body.getFixtureList().get(0).setFriction(1.5f);
			}
			else if(rightWallTouching) {
				body.applyLinearImpulse(new Vector2(0.5f, 0), body.getWorldCenter(), true);
				body.getFixtureList().get(0).setFriction(1.5f);
			}
						
			// Apply a little extra gravity.
			body.applyLinearImpulse(new Vector2(0, -0.3f), body.getWorldCenter(), true);
			
			// If no jumps have been made, take away a jump.  This means they ran off a ledge.
			if(jumpCounter == 0) {
				jumpCounter = 1;
			}
		}
		else {
			// If on the ground...
			
			if(!movingLeft && !movingRight) {
				// ... and we're not moving, set friction super high.
				body.getFixtureList().get(0).setFriction(500.0f);
			}
			else {
				// ... if we're moving, add a little friction.
				body.getFixtureList().get(0).setFriction(0.2f);
			}
			
			if(!jumping) {
				// Reset jump counter if jump button is not held and is on ground.
				jumpCounter = 0;
			}
		}		
		
		// Debug Drawing
		if(GdxGame.LogLevel == Logger.DEBUG) {
			batch.begin();						
			String text = String.format("");
			font.draw(batch, text, GameWorld.toWorld(body.getPosition().x) - font.getBounds(text).width / 2, GameWorld.toWorld(body.getPosition().y) + 48);
			batch.end();
		}
	}

	@Override
	public void onAction(String name, boolean isPressed, float delta) {
		if(name.equals("Left")) {
			movingLeft = isPressed;
		}
		
		if(name.equals("Right")) {
			movingRight = isPressed;
		}
		
		if(name.equals("Boost")) {
			boosting = isPressed;
		}
		
		if(name.equals("Jump")) {
			jumping = isPressed;
		}
	}
	
	ContactListener sensorListener = new ContactListener() {
		@Override
		public void beginContact(Contact contact) {
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(1)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(1))
			) {
				leftWallTouching = true;
			}
			
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(2)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(2))
			) {
				grounded = true;
			}
			
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(3)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(3))
			) {
				rightWallTouching = true;
			}
		}

		@Override
		public void endContact(Contact contact) {
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(1)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(1))
			) {
				leftWallTouching = false;
			}
			
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(2)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(2))
			) {
				grounded = false;
			}
			
			if(		(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals(3)) ||
					(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals(3))
			) {
				rightWallTouching = false;
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
