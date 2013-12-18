package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	@Mapper ComponentMapper<AspectComponent> aspect;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
		super(Aspect.getAspectForAll(AspectComponent.class, SpatialComponent.class));
		this.batch = batch;
		this.camera = camera;
	}

	@Override
	protected void process(Entity e) {
		Body body = spatial.get(e).body();
		Texture image = aspect.get(e).image;
		
		batch.setProjectionMatrix(camera.combined);
		batch.draw(image, body.getPosition().x - image.getWidth() / 2, body.getPosition().y - image.getHeight() / 2);
	}
}
