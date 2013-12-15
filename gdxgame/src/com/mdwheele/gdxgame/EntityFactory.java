package com.mdwheele.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.FlameComponent;
import com.mdwheele.gdxgame.components.LifetimeComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class EntityFactory {	
	
	public static Entity create(String type, World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Rectangle bounds, MapProperties properties) {
		if(type.equals("Player")) {
			return EntityFactory.createPlayer(entityWorld, physicsWorld, bounds, properties);
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
		poly.setAsBox(0.5f, 0.5f);
		body.createFixture(poly, 1);
		poly.dispose();			
 
		poly = new PolygonShape();		
		poly.setAsBox(0.4f, 0.1f, body.getPosition().add(0, -0.5f), 0);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = poly;
		fixture.density = 1.0f;
		fixture.isSensor = true;
		
		body.createFixture(fixture);		
		poly.dispose();
		
		body.setBullet(true);		
		body.setFixedRotation(true);
		
		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);
		
		SpatialComponent spatial = new SpatialComponent(position, body, 8f);
		e.addComponent(spatial);

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
		fixture.density = 1.0f;
		fixture.isSensor = true;
		
		body.createFixture(fixture);
		poly.dispose();			

		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);
		
		SpatialComponent spatial = new SpatialComponent(position, body, 0f);
		e.addComponent(spatial);

		body.setUserData(e);
		
		return e;
	}
	
	public static Entity createFlame(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Vector2 position) {
		Entity e = entityWorld.createEntity();		
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body body = physicsWorld.createBody(def); 

		CircleShape circle = new CircleShape();		
		circle.setRadius(0.1f);
		circle.setPosition(new Vector2(0, -0.5f));
		body.createFixture(circle, 0);		
		circle.dispose();	
		
		body.setTransform(position, 0);
		
		SpatialComponent spatial = new SpatialComponent(position, body, 5f);
		e.addComponent(spatial);
		
		LifetimeComponent lifetime = new LifetimeComponent();
		lifetime.counter = 0;
		lifetime.lifetime = 20;
		e.addComponent(lifetime);
		
		FlameComponent flame = new FlameComponent();
		e.addComponent(flame);
		
		body.setLinearVelocity(5f, 1f);

		body.setUserData(e);
		
		return e;
	}
	
}
