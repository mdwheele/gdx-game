package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mdwheele.gdxgame.components.CameraComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class CameraSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	private OrthographicCamera camera;
	
	public CameraSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(SpatialComponent.class, CameraComponent.class));
		
		this.camera = camera;
	}

	@Override
	protected void process(Entity e) {
		camera.position.set(spatial.get(e).body().getPosition().x, 5f, 0);
		camera.update();
	}
}
