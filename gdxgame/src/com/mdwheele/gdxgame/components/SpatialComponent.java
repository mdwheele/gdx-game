package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.level.GameWorld;

public class SpatialComponent extends Component {
	private Body physicsBody = null;
	
	private float maxVelocity;
	
	public SpatialComponent(Body body, float maxVelocity) {
		this.physicsBody = body;
		
		this.maxVelocity = maxVelocity;
	}
	
	public Body getBody() { 
		return physicsBody;
	}
	
	public Vector2 getBox2dPosition() {
		return physicsBody.getPosition();
	}
	
	public Vector2 getWorldPosition() {
		return physicsBody.getPosition().scl(GameWorld.toWorld(1));
	}
	
	public float getMaxVelocity() {
		return maxVelocity;
	}
}
