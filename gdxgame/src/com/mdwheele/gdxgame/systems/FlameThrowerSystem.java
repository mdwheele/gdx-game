package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.FlameComponent;
import com.mdwheele.gdxgame.components.LifetimeComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.support.PlayerOrientation;

public class FlameThrowerSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<FlameComponent> flame;
	@Mapper ComponentMapper<LifetimeComponent> lifetime;
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	SpriteBatch batch;
	OrthographicCamera camera;
	ParticleEffect effect;
	
	public FlameThrowerSystem() {
		super(Aspect.getAspectForAll(FlameComponent.class, LifetimeComponent.class, SpatialComponent.class));
	}

	@Override
	protected void process(Entity e) {	
		float counter = lifetime.get(e).counter;
		Body body = spatial.get(e).body();
		
		PlayerOrientation orientation = world.getManager(TagManager.class).getEntity("PLAYER").getComponent(AspectComponent.class).orientation;
		
		// Get player orientation.
		int direction = (orientation == PlayerOrientation.RIGHT) ? 1: -1;
		
		// Flames should follow a sine wave with increasing amplitude.
		body.applyLinearImpulse(new Vector2(direction * body.getMass() * (float)Math.random() - 0.1f, (float)(Math.sin(counter) * body.getMass() * Math.random() * 2)), body.getPosition(), true);
	}
}
