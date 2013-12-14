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
import com.mdwheele.gdxgame.components.Render;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Render> rm;
	
	private OrthographicCamera camera;
	private ShapeRenderer debugRenderer;
	
	public RenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Render.class));
		
		this.camera = camera;
		debugRenderer = new ShapeRenderer();
	}

	@Override
	protected void process(Entity e) {
		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeType.Filled);

		float x = rm.get(e).x;
		float y = rm.get(e).y;
		float width = rm.get(e).width;
		float height = rm.get(e).height;
		
		debugRenderer.setColor(Color.GREEN);
		debugRenderer.rect(x, y, width, height);
		debugRenderer.end();
	}
}
