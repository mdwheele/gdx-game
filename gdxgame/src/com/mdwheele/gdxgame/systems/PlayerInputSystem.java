package com.mdwheele.gdxgame.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.EntityFactory;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.events.InputActionEvent;
import com.mdwheele.gdxgame.input.InputAction;
import com.mdwheele.gdxgame.services.EventManager;

public class PlayerInputSystem extends VoidEntitySystem {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	@Mapper ComponentMapper<AspectComponent> aspect;
	
	private World physicsWorld;
	
	public PlayerInputSystem(EventManager eventManager, World physicsWorld) {
		eventManager.subscribe(InputActionEvent.class, this);
		this.physicsWorld = physicsWorld;
	}
	
	@Override
	protected void processSystem() {
	}
	
	public void handleEvent(InputActionEvent event) {
		Entity player = world.getManager(TagManager.class).getEntity("PLAYER");
		
		Body body = spatial.get(player).body();
		float currentVelocity = body.getLinearVelocity().x;
		float maxVelocity = spatial.get(player).maxVelocity();
				
		if(event.action() == InputAction.MOVE_RIGHT)
			body.applyLinearImpulse(new Vector2(maxVelocity - currentVelocity, 0), body.getPosition(), true);
		
		if(event.action() == InputAction.MOVE_LEFT)
			body.applyLinearImpulse(new Vector2(-(maxVelocity + currentVelocity), 0), body.getPosition(), true);	
		
		if(event.action() == InputAction.STOP)
			body.applyLinearImpulse(new Vector2(-body.getLinearVelocity().x, 0), body.getPosition(), true);

		if(event.action() == InputAction.ATTACK) {
			Vector2 position = body.getPosition();
			position.add(0.75f, 0.5f);
			
			EntityFactory.createFlame(world, physicsWorld, position).addToWorld();
		}
		
		if(event.action() == InputAction.JUMP) {
			body.applyLinearImpulse(new Vector2(0, 9f), body.getPosition(), true);
		}
	}
}
