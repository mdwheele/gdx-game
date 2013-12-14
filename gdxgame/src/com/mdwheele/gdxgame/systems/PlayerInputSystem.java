package com.mdwheele.gdxgame.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.events.InputActionEvent;
import com.mdwheele.gdxgame.input.InputAction;
import com.mdwheele.gdxgame.services.EventManager;
import com.mdwheele.gdxgame.services.SoundManager.GameSound;

public class PlayerInputSystem extends VoidEntitySystem {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	@Mapper ComponentMapper<AspectComponent> aspect;
	
	public PlayerInputSystem(EventManager eventManager) {
		eventManager.subscribe(InputActionEvent.class, this);
	}
	
	@Override
	protected void processSystem() {
	}
	
	public void handleEvent(InputActionEvent event) {
		Entity player = world.getManager(TagManager.class).getEntity("PLAYER");
		
		Body body = spatial.get(player).body();
		
		if(Math.abs(body.getLinearVelocity().x) < spatial.get(player).maxVelocity()) {
			if(event.action() == InputAction.MOVE_RIGHT)
				body.applyLinearImpulse(new Vector2(1f, 0), body.getPosition(), true);
			
			if(event.action() == InputAction.MOVE_LEFT)
				body.applyLinearImpulse(new Vector2(-1f, 0), body.getPosition(), true);	
			
			if(event.action() == InputAction.STOP)
				body.setLinearVelocity(0, 0);
		}
	}
}
