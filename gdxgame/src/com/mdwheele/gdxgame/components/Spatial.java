package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Spatial extends Component {
	private Vector2 position = new Vector2();
	private Body physicsBody = null;
	
	public Spatial(Vector2 pos) { this(pos.x, pos.y, null); }
	public Spatial(float x, float y, Body body){
		this.position.x = x;
		this.position.y = y;
		this.physicsBody = body;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Body getBody() {
		return physicsBody;
	}
}
