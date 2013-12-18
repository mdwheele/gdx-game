package com.mdwheele.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.AntiGravityComponent;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.CameraComponent;
import com.mdwheele.gdxgame.components.FlameComponent;
import com.mdwheele.gdxgame.components.LifetimeComponent;
import com.mdwheele.gdxgame.components.LightComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.support.GameBodyType;

public class EntityFactory {	
	
	public static Entity create(String type, World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Rectangle bounds, MapProperties properties) {
		if(type.equals("Player")) {
			return EntityFactory.createPlayer(entityWorld, physicsWorld, bounds, properties);
		}
		
		if(type.equals("Boogeyman")) {
			return EntityFactory.createBoogeyman(entityWorld, physicsWorld, bounds, properties);
		}
		
		if(type.equals("Trigger")) {
			return EntityFactory.createTrigger(entityWorld, physicsWorld, bounds, properties);
		}
		
		return null;
	}
	
	public static Entity createPlayer(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Rectangle bounds, MapProperties properties) {
		Entity e = entityWorld.createEntity();		
		entityWorld.getManager(TagManager.class).register("PLAYER", e);
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body body = physicsWorld.createBody(def);
				
		PolygonShape poly = new PolygonShape();		
		poly.setAsBox(16f, 16f);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = poly;
		fixture.filter.groupIndex = -1;
		
		body.createFixture(fixture).setUserData(GameBodyType.PLAYER);
		poly.dispose();			
 
		poly = new PolygonShape();		
		poly.setAsBox(0.4f, 0.1f, body.getPosition().add(0, -16f), 0);

		FixtureDef fixture2 = new FixtureDef();
		fixture2.shape = poly;
		fixture2.isSensor = true;
		
		body.createFixture(fixture2).setUserData(GameBodyType.PLAYER_SENSOR);		
		poly.dispose();
		
		body.setBullet(true);		
		body.setFixedRotation(true);
		
		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);

		/**
		 * Player state and orientation (for animation?)
		 */
		AspectComponent aspect = new AspectComponent();
		aspect.image = new Texture(Gdx.files.internal("img/frank.png"));
		e.addComponent(aspect);
		
		/**
		 * Add spatial component (physics)
		 */
		SpatialComponent spatial = new SpatialComponent(position, body, 8f);
		e.addComponent(spatial);

		/**
		 * Camera follow!
		 */
		CameraComponent camera = new CameraComponent();
		e.addComponent(camera);
		
		body.setUserData(e);
		
		return e;
	}
	
	public static Entity createBoogeyman(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Rectangle bounds, MapProperties properties) {
		Entity e = entityWorld.createEntity();		
		entityWorld.getManager(GroupManager.class).add(e, "ENEMY");
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body body = physicsWorld.createBody(def);
				
		PolygonShape poly = new PolygonShape();		
		poly.setAsBox(0.5f, 0.5f);		
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = poly;
		fixture.isSensor = true;
		
		body.createFixture(fixture).setUserData(GameBodyType.ENEMY_SENSOR);		
		poly.dispose();
		
		body.setBullet(true);		
		body.setFixedRotation(true);
		
		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);
		
		/**
		 * Add spatial component (physics)
		 */
		SpatialComponent spatial = new SpatialComponent(position, body, 8f);
		e.addComponent(spatial);
		
		/**
		 * Floats around like a ghost so it gets this!
		 */
		AntiGravityComponent antigrav = new AntiGravityComponent();
		e.addComponent(antigrav);
		
		body.setUserData(e);
		
		return e;
	}
	
	public static Entity createTrigger(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Rectangle bounds, MapProperties properties) {
		Entity e = entityWorld.createEntity();		
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		Body body = physicsWorld.createBody(def); 
						
		PolygonShape poly = new PolygonShape();		
		poly.setAsBox(bounds.width, bounds.height);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = poly;
		fixture.isSensor = true;
		
		body.createFixture(fixture).setUserData(GameBodyType.TRIGGER);
		poly.dispose();			

		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);
		
		SpatialComponent spatial = new SpatialComponent(position, body, 0f);
		e.addComponent(spatial);

		body.setUserData(e);
		
		return e;
	}
	
	public static Entity createFlame(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Vector2 position, Vector2 velocity) {
		Entity e = entityWorld.createEntity();		
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body body = physicsWorld.createBody(def); 
		
		CircleShape circle = new CircleShape();		
		circle.setRadius(2f);
		circle.setPosition(new Vector2(0, -0.5f));
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = circle;
		fixture.filter.groupIndex = -1;
		
		body.createFixture(fixture).setUserData(GameBodyType.FLAME);
		circle.dispose();	
		
		body.setTransform(position, 0);
		
		SpatialComponent spatial = new SpatialComponent(position, body, 5f);
		e.addComponent(spatial);
		
		LifetimeComponent lifetime = new LifetimeComponent();
		lifetime.counter = 0;
		lifetime.lifetime = 280 + (int)(Math.random() * 10);
		e.addComponent(lifetime);
		
		FlameComponent flame = new FlameComponent();
		e.addComponent(flame);
		
		body.setLinearVelocity(velocity);
		body.setUserData(e);

		/**
		 * Light!
		 */
		LightComponent light = new LightComponent();
		light.color = new Color(1,1,1,0.5f);
		light.intensity = 50f;
		e.addComponent(light);
		
		return e;
	}
	
}
