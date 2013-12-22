package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

	private static final Logger logger = new Logger(PlayerInputSystem.class.getSimpleName());
	
	private boolean left;
	private boolean right;
	private boolean jump;
		
	public PlayerInputSystem(InputManager input) {
		super(Aspect.getAspectForAll(PlayerComponent.class, SpatialComponent.class));
		
		input.addMapping("Left", new KeyTrigger(Keys.A), new KeyTrigger(Keys.LEFT));
		input.addMapping("Right", new KeyTrigger(Keys.D), new KeyTrigger(Keys.RIGHT));
		input.addMapping("Jump", new KeyTrigger(Keys.SPACE), new KeyTrigger(Keys.W));
		input.addListener(this, new String[]{"Left", "Right", "Jump"});
	}

	@Override
	protected void process(Entity e) {		
		SpatialComponent spatial = sm.get(e);
		
		Body body = spatial.getBody();
		Vector2 currentVelocity = body.getLinearVelocity();		
		float maxVelocity = spatial.getMaxVelocity();
		
		Vector2 movement = new Vector2(0, currentVelocity.y);
		
		// Determine if player is grounded.
		
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
			movement.sub(currentVelocity);		

			body.applyLinearImpulse(movement, body.getWorldCenter(), true);
		}
		else {
			//body.setLinearVelocity(currentVelocity);
		}
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
}
