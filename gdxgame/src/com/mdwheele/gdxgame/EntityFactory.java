package com.mdwheele.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class EntityFactory {	
	
	public static Entity create(String type, World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Vector2 position, MapProperties properties) {
		if(type.equals("Player")) {
			return EntityFactory.createPlayer(entityWorld, physicsWorld, position, properties);
		}
		
		return null;
	}
	
	public static Entity createPlayer(World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, Vector2 position, MapProperties properties) {
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
 
		CircleShape circle = new CircleShape();		
		circle.setRadius(0.5f);
		circle.setPosition(new Vector2(0, -0.5f));
		body.createFixture(circle, 0);		
		circle.dispose();		
 
		body.setBullet(true);		
		body.setFixedRotation(true);
		
		body.setTransform(position.scl(1/32f), 0);
		
		SpatialComponent spatial = new SpatialComponent(position.scl(1/32f), body);
		e.addComponent(spatial);
		
		return e;
	}
	
}
