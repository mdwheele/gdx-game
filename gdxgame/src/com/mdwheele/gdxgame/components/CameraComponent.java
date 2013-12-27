package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class CameraComponent extends Component {
	Body targetBody;
	
	public CameraComponent(Body body) {
		this.targetBody = body;
	}
	
	public void attachToBody(Body body) {
		this.targetBody = body;
	}
	
	public Body getTargetBody() {
		return this.targetBody;
	}
}
