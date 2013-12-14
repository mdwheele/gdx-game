package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mdwheele.gdxgame.components.AspectComponent;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<AspectComponent> rm;
	
	private OrthographicCamera camera;
	private ShapeRenderer debugRenderer;
	
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(AspectComponent.class));
		
		this.camera = camera;		
	}

	@Override
	protected void process(Entity e) {
	}
}
