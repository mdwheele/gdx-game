package com.mdwheele.gdxgame.components;

import java.util.ArrayList;
import java.util.Collections;

import com.artemis.Component;
import com.mdwheele.gdxgame.scripts.Script;

public class ScriptComponent extends Component {
	private ArrayList<Script> scripts;
		
	public ScriptComponent(Script...scripts){
		if(scripts == null)
			throw new RuntimeException("Cannot create a script component with no scripts.");
		
		this.scripts = new ArrayList<Script>(scripts.length);
		Collections.addAll(this.scripts, scripts);
	}
	
	public ArrayList<Script> getScripts() {
		return scripts;
	}
	
	public void setScripts(ArrayList<Script> scripts) {
		this.scripts = scripts;
	}
}
