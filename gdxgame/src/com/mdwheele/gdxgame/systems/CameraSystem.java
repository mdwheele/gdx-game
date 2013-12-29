package com.mdwheele.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mdwheele.gdxgame.components.CameraComponent;
import com.mdwheele.gdxgame.input.InputManager;
import com.mdwheele.gdxgame.input.controls.ActionListener;
import com.mdwheele.gdxgame.input.controls.AnalogListener;
import com.mdwheele.gdxgame.input.controls.JoyAxisTrigger;
import com.mdwheele.gdxgame.input.controls.JoyButtonTrigger;
import com.mdwheele.gdxgame.input.controls.Xbox360Pad;
import com.mdwheele.gdxgame.level.GameWorld;

public class CameraSystem extends EntityProcessingSystem implements ActionListener {
	@Mapper ComponentMapper<CameraComponent> cm;
	
	private OrthographicCamera camera;
	private InputManager input;
	
	private boolean zoomingIn;
	private boolean zoomingOut;
	
	public CameraSystem(GameWorld gameWorld) {
		super(Aspect.getAspectForAll(CameraComponent.class));
		
		this.camera = gameWorld.gameScreen.camera;
		this.input = gameWorld.gameScreen.input;

		input.addMapping("Zoom In", new JoyAxisTrigger(Xbox360Pad.AXIS_RIGHT_Y, true));
		input.addMapping("Zoom Out", new JoyAxisTrigger(Xbox360Pad.AXIS_RIGHT_Y, false));
		input.addMapping("Zero Zoom", new JoyButtonTrigger(Xbox360Pad.BUTTON_R3));
		
		input.addListener(this, new String[]{"Zoom In", "Zoom Out", "Zero Zoom"});
	}

	@Override
	protected void process(Entity e) {
		Body target = cm.get(e).getTargetBody();
		
		Vector2 targetPosition = target.getPosition().scl(GameWorld.toWorld(1f));
		Vector2 cameraPosition = new Vector2(camera.position.x, camera.position.y);
						
		camera.translate((targetPosition.x - cameraPosition.x) * 0.1f, (targetPosition.y - cameraPosition.y) * 0.1f);
		
		if(zoomingIn && this.camera.zoom - 0.05f > 0.1f) {
			this.camera.zoom -= 0.05f;
		}
		else if(zoomingOut) {
			this.camera.zoom += 0.05f;
		}
	}

	@Override
	public void onAction(String name, boolean isPressed, float delta) {
		if(name.equals("Zero Zoom")) {
			this.camera.zoom = 1f;
		}
		
		if(name.equals("Zoom In")) {
			zoomingIn = isPressed;
		}
		else if(name.equals("Zoom Out")) {
			zoomingOut = isPressed;
		}
	}
}
