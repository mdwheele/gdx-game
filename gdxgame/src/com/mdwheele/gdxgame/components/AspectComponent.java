package com.mdwheele.gdxgame.components;

import com.artemis.Component;

public class AspectComponent extends Component {
	enum State { RUNNING, JUMPING, SHOOTING, IDLE };
	enum Orientation { LEFT, RIGHT };
	
	State state;
	Orientation orientation;
	
	public AspectComponent(){
		state = State.IDLE;
		orientation = Orientation.RIGHT;
	}
}
