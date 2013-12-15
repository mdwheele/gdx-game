package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mdwheele.gdxgame.components.LifetimeComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class LifetimeSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<LifetimeComponent> lc;
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	public LifetimeSystem() {
		super(Aspect.getAspectForAll(LifetimeComponent.class));		
	}

	@Override
	protected void process(Entity e) {	
		float counter = lc.get(e).counter;
		float lifetime = lc.get(e).lifetime;
		
		if(counter > lifetime) {			
			e.deleteFromWorld();
		}
		
		lc.get(e).counter++;
	}
}
