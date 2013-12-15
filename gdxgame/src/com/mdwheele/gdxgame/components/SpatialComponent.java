package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SpatialComponent extends Component {
	private Vector2 position = new Vector2();
	private Body physicsBody = null;
	
	private float maxVelocity;
	
	public SpatialComponent(Vector2 position, Body body, float maxVelocity){
		this.position = position;
		this.physicsBody = body;
		this.maxVelocity = maxVelocity;		
	}
	
	public Vector2 position() {
		return position;
	}
	
	public Body body() {
		return physicsBody;
	}
	
	public float maxVelocity() {
		return maxVelocity;
	}
}
