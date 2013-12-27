package com.mdwheele.gdxgame.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.CameraComponent;
import com.mdwheele.gdxgame.components.PlayerComponent;
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
				
		// Create player body.
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body body = physicsWorld.createBody(def);
				
		PolygonShape poly = new PolygonShape();		
		poly.setAsBox(bounds.width * 0.95f, bounds.height * 0.95f);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = poly;
		fixture.friction = 100f;
		
		body.createFixture(fixture);
		poly.dispose();			
 
		poly = new PolygonShape();		
		poly.setAsBox(bounds.width * 0.9f, bounds.height / 6, body.getPosition().add(0, -(bounds.height)), 0);

		FixtureDef groundSensorFixture = new FixtureDef();
		groundSensorFixture.shape = poly;
		groundSensorFixture.isSensor = true;	
		
		body.createFixture(groundSensorFixture).setUserData(3);		
		poly.dispose();
		
		body.setFixedRotation(true);
		
		Vector2 position = new Vector2(bounds.x, bounds.y);		
		body.setTransform(position, 0);		
		body.setUserData(e);
		
		// SpatialComponent
		SpatialComponent spatial = new SpatialComponent(body, 256.0f);
		e.addComponent(spatial);
		
		// PlayerComponent
		e.addComponent(new PlayerComponent());
		
		// CameraComponent
		e.addComponent(new CameraComponent(body));
		
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
		
		body.createFixture(fixture);
		poly.dispose();			

		Vector2 position = new Vector2(bounds.x, bounds.y);
		
		body.setTransform(position, 0);
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
		
		body.createFixture(fixture);
		circle.dispose();	
		
		body.setTransform(position, 0);
		
		return e;
	}
	
}
