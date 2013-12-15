package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mdwheele.gdxgame.components.SpatialComponent;
import com.mdwheele.gdxgame.events.CollisionEvent;
import com.mdwheele.gdxgame.services.EventManager;

public class CollisionSystem extends EntityProcessingSystem implements ContactListener {
	@Mapper ComponentMapper<SpatialComponent> spatial;
	
	EventManager eventManager;
	World physicsWorld;
	
	public CollisionSystem(EventManager eventManager, World physicsWorld) {
		super(Aspect.getAspectForAll(SpatialComponent.class));		
		this.eventManager = eventManager;
		this.physicsWorld = physicsWorld;
	}

	@Override
	protected void process(Entity arg0) {		
	}

	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if(a.getUserData() instanceof Entity && b.getUserData() instanceof Entity) {
			eventManager.post(new CollisionEvent((Entity)a.getUserData(), (Entity)b.getUserData()));
		}
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	@Override
	public void removed(Entity e) {
		physicsWorld.destroyBody(spatial.get(e).body());
	}
}
