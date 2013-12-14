package com.mdwheele.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.AspectComponent;
import com.mdwheele.gdxgame.components.SpatialComponent;

public class EntityFactory {	
	
	public static Entity createPlayer(World world, com.badlogic.gdx.physics.box2d.World physicsWorld, float x, float y) {
		Entity e = world.createEntity();
		
		AspectComponent render = new AspectComponent();
		e.addComponent(render);		
				
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// Create Body
		Body body = physicsWorld.createBody(bodyDef);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.5f, 0.5f);
		
		body.createFixture(boxShape, 0.0f);
		
		
		SpatialComponent spatial = new SpatialComponent(x, y, body);
		e.addComponent(spatial);
		
		return e;
	}
	
}
