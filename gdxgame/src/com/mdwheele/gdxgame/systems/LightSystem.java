package com.mdwheele.gdxgame.systems;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.components.AntiGravityComponent;
import com.mdwheele.gdxgame.components.LightComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class LightSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<LightComponent> light;
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	private World physicsWorld;
	private RayHandler rayHandler;
	
	public LightSystem(World physicsWorld, RayHandler rayHandler) {
		super(Aspect.getAspectForAll(LightComponent.class, SpatialComponent.class));
		
		this.physicsWorld = physicsWorld;
		this.rayHandler = rayHandler;
	}

	@Override 
	protected void inserted(Entity e) {
		Body body = spatial.get(e).body();
		Color color = light.get(e).color;
		float intensity = light.get(e).intensity;
		
		light.get(e).light = new PointLight(this.rayHandler, 32, color, intensity, 0, 0);
		light.get(e).light.attachToBody(body, 0, 0);
		light.get(e).light.setXray(true);
		light.get(e).light.setSoft(true);
	}
	
	@Override
	protected void removed(Entity e) {
		light.get(e).light.remove();
	}
	
	@Override
	protected void process(Entity e) {	
		light.get(e).intensity -= 0.05f;
		light.get(e).light.setDistance(light.get(e).intensity);
	}
}
