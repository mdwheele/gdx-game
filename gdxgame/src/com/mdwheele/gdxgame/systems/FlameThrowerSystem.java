package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.FlameComponent;
import com.mdwheele.gdxgame.components.LifetimeComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class FlameThrowerSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<FlameComponent> flame;
	@Mapper ComponentMapper<LifetimeComponent> lifetime;
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	public FlameThrowerSystem() {
		super(Aspect.getAspectForAll(FlameComponent.class, LifetimeComponent.class, SpatialComponent.class));		
	}

	@Override
	protected void process(Entity e) {	
		float counter = lifetime.get(e).counter;
		float strength = flame.get(e).strength;
		Body body = spatial.get(e).body();
		
		// Flames should follow a sine wave with increasing amplitude.
		body.applyLinearImpulse(new Vector2(1f * (float)Math.random() - 0.1f, (float)(Math.sin(counter) * Math.random() * 5)), body.getPosition(), true);
	}
}
