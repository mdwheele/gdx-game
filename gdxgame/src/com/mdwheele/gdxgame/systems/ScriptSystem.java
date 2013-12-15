package com.mdwheele.gdxgame.systems;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.mdwheele.gdxgame.components.ScriptComponent;
import com.mdwheele.gdxgame.scripts.Script;

public class ScriptSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<ScriptComponent> script;
	
	public ScriptSystem() {
		super(Aspect.getAspectForAll(ScriptComponent.class));
	}

	@Override
	protected void process(Entity e) {
		ArrayList<Script> scripts = script.get(e).getScripts();
		
		for(Script script: scripts) {
			script.update(world, e);
		}
	}
	
	@Override
	public void inserted(Entity e) {
		ArrayList<Script> scripts = script.get(e).getScripts();
		
		for(Script script: scripts) {
			script.inserted(world, e);
		}
	}
	
	@Override
	public void removed(Entity e) {
		ArrayList<Script> scripts = script.get(e).getScripts();
		
		for(Script script: scripts) {
			script.removed(world, e);
		}
	}
}
