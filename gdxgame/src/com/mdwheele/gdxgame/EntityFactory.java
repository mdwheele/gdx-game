package com.mdwheele.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mdwheele.gdxgame.components.Render;
import com.mdwheele.gdxgame.components.Spatial;

public class EntityFactory {	
	
	public static Entity createPlayer(World world, com.badlogic.gdx.physics.box2d.World physicsWorld, float x, float y) {
		Entity e = world.createEntity();
		
		Render render = new Render(x, y);
		e.addComponent(render);
		
				
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// Create Body
		Body body = physicsWorld.createBody(bodyDef);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.5f, 0.5f);
		
		body.createFixture(boxShape, 0.0f);
		
		
		Spatial spatial = new Spatial(x, y, body);
		e.addComponent(spatial);
		
		return e;
	}
	
}
