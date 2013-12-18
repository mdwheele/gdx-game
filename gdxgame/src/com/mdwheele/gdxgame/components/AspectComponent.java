package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.mdwheele.gdxgame.support.PlayerOrientation;
import com.mdwheele.gdxgame.support.PlayerState;

public class AspectComponent extends Component {
	public PlayerState state;
	public PlayerOrientation orientation;
	public Texture image;
	
	public AspectComponent(){
		state = PlayerState.IDLE;
		orientation = PlayerOrientation.RIGHT;
		image = null;
	}
	
	public void setPlayerState(PlayerState state) {
		this.state = state;		
	}
	
	public void setPlayerOrientation(PlayerOrientation orientation) {
		this.orientation = orientation;		
	}
	
	public PlayerState state() { return this.state; }
}
