package com.mdwheele.gdxgame.components;

import com.artemis.Component;
import com.mdwheele.gdxgame.support.PlayerOrientation;
import com.mdwheele.gdxgame.support.PlayerState;

public class AspectComponent extends Component {
	public PlayerState state;
	public PlayerOrientation orientation;
	
	public AspectComponent(){
		state = PlayerState.IDLE;
		orientation = PlayerOrientation.RIGHT;
	}
	
	public void setPlayerState(PlayerState state) {
		this.state = state;		
	}
	
	public void setPlayerOrientation(PlayerOrientation orientation) {
		this.orientation = orientation;		
	}
	
	public PlayerState state() { return this.state; }
}
