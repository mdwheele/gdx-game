package com.mdwheele.gdxgame.level;

import java.util.Iterator;

import com.artemis.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.EntityFactory;
import com.mdwheele.gdxgame.GdxGame;

public class LevelLoader {

	private TiledMap tiledMap;
	private com.artemis.World artemisWorld;
	private World box2dWorld;	

	// Logger for the game level.
	private static final Logger logger = new Logger(LevelLoader.class.getSimpleName());
	
	// Default fixture for map terrain.
	FixtureDef defaultFixture;
	
	public LevelLoader(GameWorld level) {
		// Set up logger interface.
		logger.setLevel(GdxGame.LogLevel);
		logger.info("Initializing...");
		
		tiledMap = level.tiledMap;
		artemisWorld = level.artemisWorld;
		box2dWorld = level.box2dWorld;
		
		/* Set default fixture property definition */
        defaultFixture = new FixtureDef();
        defaultFixture.density = 1.0f;
        defaultFixture.friction = 0.0f;
        defaultFixture.restitution = 0.0f;
	}

	public TiledMap load(String mapFileName) {
		tiledMap = new TmxMapLoader().load(mapFileName);	
				
		this.loadPhysicsFromLayer("physics");
		this.loadEntitiesFromLayer("entities");
		
		return tiledMap;		
	}
	
	/** 
	 * @param layerName name of the layer that contains physics shapes.
	 */
	private void loadPhysicsFromLayer(String layerName) {
		if (tiledMap == null) {
			logger.error("Map was not loaded properly.");
			return;
		}
		
		MapLayer layer = tiledMap.getLayers().get(layerName);
		
		if (layer == null) {
			logger.error("Layer " + layerName + " does not exist.");
			return;
		}
		
		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIterator = objects.iterator();
		
		logger.info("Loading environment physics.");
		
		while(objectIterator.hasNext()) {
			MapObject object = objectIterator.next();
			
			if (object instanceof TextureMapObject) {
				return;
			}
			
			Shape shape;
			
			if (object instanceof RectangleMapObject) {
				shape = ShapeHelper.getRectangle((RectangleMapObject)object);
			}
			else if (object instanceof PolygonMapObject) {
				shape = ShapeHelper.getPolygon((PolygonMapObject)object);
			}
			else if (object instanceof PolylineMapObject) {
				shape = ShapeHelper.getPolyline((PolylineMapObject)object);
			}
			else {
				logger.error("Non-supported shape " + object);
				continue;
			}
			
			FixtureDef fixtureDef = defaultFixture;
			fixtureDef.shape = shape;
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;
			
			Body body = box2dWorld.createBody(bodyDef);
			body.createFixture(fixtureDef);
			
			fixtureDef.shape = null;
			shape.dispose();
		}
		
	}
	
	private void loadEntitiesFromLayer(String layerName) {
		if (tiledMap == null) {
			logger.error("Map was not loaded properly.");
			return;
		}
		
		MapLayer layer = tiledMap.getLayers().get(layerName);
		
		if (layer == null) {
			logger.error("Layer " + layerName + " does not exist.");
			return;
		}
		
		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIterator = objects.iterator();
		
		logger.info("Loading entities.");
		
		while(objectIterator.hasNext()) {
			MapObject object = objectIterator.next();
			
			if (object instanceof RectangleMapObject) {
				RectangleMapObject entity = (RectangleMapObject) object;
				
				// Get Bounds
				Vector2 position = new Vector2();
				Vector2 size = new Vector2();

				position = entity.getRectangle().getCenter(position).scl(GameWorld.toBox2d(1f));
				size = entity.getRectangle().getSize(size).scl(0.5f * GameWorld.toBox2d(1f));
				
				Rectangle bounds = new Rectangle(position.x, position.y, size.x, size.y);
								
				// Get Properties
				MapProperties properties = entity.getProperties();
				
				// Create entity at position X,Y with properties P	
				Entity e = EntityFactory.create(entity.getName(), artemisWorld, box2dWorld, bounds, properties);
				
				if(e != null) {
					e.addToWorld();
					logger.info( String.format("Spawned '%s' at position %s", entity.getName(), bounds) );
				}
			}
			else {
				logger.error("Non-supported shape " + object);
				continue;
			}
		}
	}
}
