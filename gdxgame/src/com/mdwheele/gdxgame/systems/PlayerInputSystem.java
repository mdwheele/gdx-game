package com.mdwheele.gdxgame.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.EntityFactory;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.events.InputActionEvent;
import com.mdwheele.gdxgame.input.InputAction;
import com.mdwheele.gdxgame.services.EventManager;
import com.mdwheele.gdxgame.support.GameBodyType;
import com.mdwheele.gdxgame.support.PlayerOrientation;
import com.mdwheele.gdxgame.support.PlayerState;

public class PlayerInputSystem extends VoidEntitySystem implements ContactListener {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	@Mapper ComponentMapper<AspectComponent> aspect;
	
	private World physicsWorld;
	
	private Entity player;
	private boolean playerGrounded;
	private Body playerBody;
	
	private int attackRateCounter = 0;
	private int attackRate = 1;
	
	public PlayerInputSystem(EventManager eventManager, World physicsWorld) {
		eventManager.subscribe(InputActionEvent.class, this);
		this.physicsWorld = physicsWorld;
	}
	
	@Override
	protected void processSystem() {
		player = world.getManager(TagManager.class).getEntity("PLAYER");
		playerBody = spatial.get(player).body();
		
		if((!playerGrounded && aspect.get(player).state != PlayerState.JUMPING)) {
			playerBody.applyLinearImpulse(new Vector2(0, -1f), playerBody.getPosition(), true);
		}
		
		//System.out.println(String.format("The player is currently %s.  Grounded? %s", aspect.get(player).state, playerGrounded));
	}
	
	public void handleEvent(InputActionEvent event) {
		float currentVelocity = playerBody.getLinearVelocity().x;
		float maxVelocity = spatial.get(player).maxVelocity();
				
		if(event.action() == InputAction.MOVE_RIGHT) {
			playerBody.applyLinearImpulse(new Vector2((maxVelocity - currentVelocity)  * playerBody.getMass(), 0), playerBody.getPosition(), true);
			aspect.get(player).setPlayerState(PlayerState.RUNNING);
			aspect.get(player).setPlayerOrientation(PlayerOrientation.RIGHT);
		}
		
		if(event.action() == InputAction.MOVE_LEFT) {
			playerBody.applyLinearImpulse(new Vector2(-(maxVelocity + currentVelocity) * playerBody.getMass(), 0), playerBody.getPosition(), true);	
			aspect.get(player).setPlayerState(PlayerState.RUNNING);
			aspect.get(player).setPlayerOrientation(PlayerOrientation.LEFT);
		}
		
		if(event.action() == InputAction.STOP) {
			playerBody.applyLinearImpulse(new Vector2(-playerBody.getLinearVelocity().x * playerBody.getMass(), 0), playerBody.getPosition(), true);	
			aspect.get(player).setPlayerState(PlayerState.IDLE);
		}

		if(event.action() == InputAction.ATTACK) {
			Vector2 position = playerBody.getPosition();
			
			int direction = (aspect.get(player).orientation == PlayerOrientation.RIGHT) ? 1: -1;
			
			position.add(direction * 0.75f, 0.5f);		
			
			if(attackRateCounter++ > attackRate) {
				EntityFactory.createFlame(world, physicsWorld, position, playerBody.getLinearVelocity().add(new Vector2(direction * 40f, 1f))).addToWorld();
				attackRateCounter = 0;
			}

			aspect.get(player).setPlayerState(PlayerState.SHOOTING);
		}
		
		if(event.action() == InputAction.JUMP) {
			if(playerGrounded) {
				playerBody.applyLinearImpulse(new Vector2(0, 2f), playerBody.getPosition(), true);
				aspect.get(player).setPlayerState(PlayerState.IDLE);
			}
			else {
				aspect.get(player).setPlayerState(PlayerState.JUMPING);
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
		if(contact.getFixtureA().getUserData() == GameBodyType.PLAYER_SENSOR || contact.getFixtureB().getUserData() == GameBodyType.PLAYER_SENSOR) {
			playerGrounded = true;
		}
	}

	@Override
	public void endContact(Contact contact) {
		if(contact.getFixtureA().getUserData() == GameBodyType.PLAYER_SENSOR || contact.getFixtureB().getUserData() == GameBodyType.PLAYER_SENSOR) {
			playerGrounded = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {	
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
