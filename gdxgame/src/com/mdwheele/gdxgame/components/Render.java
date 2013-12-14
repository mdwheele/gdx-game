package com.mdwheele.gdxgame.components;

import com.artemis.Component;

public class Render extends Component {
	public float x, y, width, height;
	
	public Render(float x, float y){
		this.x = x;
		this.y = y;
		this.width = this.height = 0.5f;
	}
}
