package com.mdwheele.gdxgame.events;

import com.artemis.Entity;


public class CollisionEvent extends Event {
	private Entity a;
	private Entity b;
	
	public CollisionEvent(Entity a, Entity b) {
		this.a = a;
		this.b = b;
	}
	
	public Entity getOtherEntity(Entity e) {
		if(e.equals(a)) {
			return b;
		}
		else if(e.equals(b)) {
			return a;
		}
		
		return null;
	}
}
