package com.mdwheele.gdxgame.input;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.GdxGame;
import com.mdwheele.gdxgame.input.controls.ActionListener;
import com.mdwheele.gdxgame.input.controls.InputListener;
import com.mdwheele.gdxgame.input.controls.KeyTrigger;
import com.mdwheele.gdxgame.input.controls.Trigger;
import com.mdwheele.gdxgame.input.controls.Xbox360Pad;
import com.mdwheele.gdxgame.input.event.InputEvent;
import com.mdwheele.gdxgame.input.event.KeyInputEvent;


public class InputManager {

	private static final Logger logger = new Logger(InputManager.class.getSimpleName(), GdxGame.LogLevel);
	private float delta;
	private boolean safeMode;
	private float axisDeadZone = 0.15f;
	private Vector2 cursorPosition;
	
	private ArrayList<InputEvent> inputQueue;
	private IntMap<ArrayList<Mapping>> bindings;
	private HashMap<String, Mapping> mappings;
	
	private static class Mapping {
		private final String name;
		private final ArrayList<Integer> triggers = new ArrayList<Integer>();
		private final ArrayList<InputListener> listeners = new ArrayList<InputListener>();
		
		public Mapping(String name) {
			this.name = name;
		}
	}
	
	public InputManager() {
		Gdx.input.setInputProcessor(inputProcessor);
		Controllers.addListener(controllerListener);
	
		cursorPosition = new Vector2();
		
		inputQueue = new ArrayList<InputEvent>();
		bindings = new IntMap<ArrayList<Mapping>>();
		mappings = new HashMap<String, Mapping>();
	}
	
	public void update(float delta) {
		this.delta = delta;
		
		safeMode = delta < axisDeadZone;
		
		processInputQueue();
	}
	
	public void processInputQueue() {
		for(InputEvent event: inputQueue) {
			
			if(event.isConsumed()) {
				continue;
			}
			
			if(event instanceof KeyInputEvent) {
				KeyInputEvent e = (KeyInputEvent)event;
				int hash = KeyTrigger.keyHash(e.getKeyCode());
				invokeActions(hash, e.isPressed());
			}
		}
		
		inputQueue.clear();
	}
	
	private void invokeActions(int hash, boolean pressed) {
		ArrayList<Mapping> maps = bindings.get(hash);
		
		if(maps == null)
			return;
		
		for(Mapping mapping: maps) {
			for(InputListener listener: mapping.listeners) {
				if(listener instanceof ActionListener) {
					((ActionListener) listener).onAction(mapping.name, pressed, delta);
				}
			}
		}
	}
	
	public void addListener(InputListener listener, String... mappingNames) {
		for(String mappingName: mappingNames) {
			Mapping mapping = mappings.get(mappingName);
			
			if(mapping == null) {
				mapping = new Mapping(mappingName);
				mappings.put(mappingName, mapping);
			}
			
			if(!mapping.listeners.contains(listener)) {
				mapping.listeners.add(listener);
			}
		}
	}
	
	public void removeListener(InputListener listener) {
		for(Mapping mapping: mappings.values()) {
			mapping.listeners.remove(listener);
		}
	}
		
	public void addMapping(String mappingName, Trigger... triggers) {
		Mapping mapping = mappings.get(mappingName);
		
		if(mapping == null) {
			mapping = new Mapping(mappingName);
			mappings.put(mappingName, mapping);
		}
		
		for(Trigger trigger: triggers) {
			int hash = trigger.hashCode();
			ArrayList<Mapping> names = bindings.get(hash);
			
			if(names == null) {
				names = new ArrayList<Mapping>();
				bindings.put(hash, names);
			}
			
			if(!names.contains(mapping)) {
				names.add(mapping);
				mapping.triggers.add(hash);
			}
		}
	}
	
	public boolean hasMapping(String mappingName) {
		return mappings.containsKey(mappingName);
	}
	
	public void deleteMapping(String mappingName) {
		Mapping mapping = mappings.get(mappingName);
		
		if(mapping == null) {
			logger.error(String.format("Cannot find mapping: %s", mappingName));
		}
		
		ArrayList<Integer> triggers = mapping.triggers;
		for (int i = triggers.size() - 1; i >= 0; i--) {
            int hash = triggers.get(i);
            ArrayList<Mapping> maps = bindings.get(hash);
            maps.remove(mapping);
        }
	}
	
	public void deleteTrigger(String mappingName, Trigger trigger) {
        Mapping mapping = mappings.get(mappingName);
        
        if (mapping == null) {
			logger.error(String.format("Cannot find mapping: %s", mappingName));
        }

        ArrayList<Mapping> maps = bindings.get(trigger.hashCode());
        maps.remove(mapping);
    }
	
	public void clearMappings() {
        mappings.clear();
        bindings.clear();
    }
	
	/**
	 * Input adapter for capturing mouse and keyboard input events from LibGdx.
	 */
	private InputAdapter inputProcessor = new InputAdapter() {				
		@Override
		public boolean keyDown(int keycode) {
			inputQueue.add(new KeyInputEvent(keycode, true));
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			inputQueue.add(new KeyInputEvent(keycode, false));
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {			
			cursorPosition.set(screenX, screenY);
			return false;
		}
	};

	/**
	 * Input adapter for capturing input events from attached game controllers.
	 */
	private ControllerAdapter controllerListener = new ControllerAdapter() {
		@Override
		public void connected(Controller controller) {
			logger.info("Controller connected!");
		}

		@Override
		public void disconnected(Controller controller) {
		}
		
		@Override
		public boolean axisMoved(Controller controller, int axisCode, float value) {
			if(Math.abs(value) > axisDeadZone) {
				logger.info(String.format("Axis %d is set to %f", axisCode, value));
			}
			
			return false;
		}

		@Override
		public boolean buttonDown(Controller controller, int buttonCode) {
			logger.info(String.format("Controller pressed %s button.", buttonCode));
			
			if(buttonCode == Xbox360Pad.BUTTON_BACK) {
				Gdx.app.exit();
			}
			
			return false;
		}

		@Override
		public boolean buttonUp(Controller controller, int buttonCode) {
			logger.info(String.format("Controller released %s button.", buttonCode));
			return false;
		}

		@Override
		public boolean povMoved(Controller controller, int povCode, PovDirection value) {
			logger.info(String.format("Controller pressed %s button.", value));
			return false;
		}

		@Override
		public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
			return false;
		}

		@Override
		public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
			return false;
		}
	};
}
