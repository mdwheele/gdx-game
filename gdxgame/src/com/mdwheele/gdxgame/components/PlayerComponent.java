package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerComponent extends Component {	
	private float maxVelocity;
	private float speedBoostMultiplier;

	public PlayerComponent(float maxVelocity, float speedBoostMultiplier) {
		this.maxVelocity = maxVelocity;
		this.speedBoostMultiplier = speedBoostMultiplier;
	}
	
	public PlayerComponent(Body body, float maxVelocity) {
		this(maxVelocity, 1.0f);
	}
	
	public float getMaxVelocity(boolean boosted) {
		return boosted ? maxVelocity * speedBoostMultiplier : maxVelocity;
	}
	
	public float getSpeedBoostMultiplier() {
		return this.speedBoostMultiplier;
	}
}
