package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.components.AntiGravityComponent;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.support.PlayerOrientation;

public class AntiGravitySystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<AntiGravityComponent> antigrav;
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	private World physicsWorld;
	
	public AntiGravitySystem(World physicsWorld) {
		super(Aspect.getAspectForAll(AntiGravityComponent.class, SpatialComponent.class));
		
		this.physicsWorld = physicsWorld;
	}

	@Override
	protected void process(Entity e) {	
		Body body = spatial.get(e).body();
				
		Vector2 antiGravityForce = new Vector2(0, -physicsWorld.getGravity().scl((Gdx.graphics.getDeltaTime() * 60f) * body.getMass()).y);
		
		body.setLinearVelocity(Vector2.Zero);
		body.applyForceToCenter(antiGravityForce, true);
	}
}
