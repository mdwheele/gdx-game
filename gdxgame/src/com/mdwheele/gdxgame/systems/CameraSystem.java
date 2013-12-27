package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.CameraComponent;
import com.mdwheele.gdxgame.level.GameWorld;

public class CameraSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<CameraComponent> cm;
	
	private OrthographicCamera camera;
	
	public CameraSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(CameraComponent.class));
		
		this.camera = camera;
	}

	@Override
	protected void process(Entity e) {
		Body target = cm.get(e).getTargetBody();
		
		Vector2 targetPosition = target.getPosition().scl(GameWorld.toWorld(1f));
		Vector2 cameraPosition = new Vector2(camera.position.x, camera.position.y);
						
		camera.translate((targetPosition.x - cameraPosition.x) * 0.1f, (targetPosition.y - cameraPosition.y) * 0.1f);
	}
}
