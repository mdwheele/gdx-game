package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<SpatialComponent> sm;
	
	private OrthographicCamera camera;
	private ShapeRenderer debugRenderer;
	
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(SpatialComponent.class));
		
		this.camera = camera;
		debugRenderer = new ShapeRenderer();
	}

	@Override
	protected void process(Entity e) {
	}
}
