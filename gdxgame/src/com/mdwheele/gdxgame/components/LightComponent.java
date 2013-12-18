package com.mdwheele.gdxgame.components;

import box2dLight.PointLight;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class LightComponent extends Component {
	public float intensity;
	public Color color;
	public PointLight light;
	
	public LightComponent(){
		intensity = 10f;
		color = Color.WHITE;
		light = null;
	}
}
