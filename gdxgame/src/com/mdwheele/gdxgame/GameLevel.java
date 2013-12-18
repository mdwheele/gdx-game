package com.mdwheele.gdxgame;

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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.mdwheele.gdxgame.support.GameBodyType;

public class GameLevel implements Disposable {

	private Logger logger;
	private TiledMap map;
	private World physicsWorld;
	private com.artemis.World entityWorld;
	
	private float unitsPerPixel;
	private Array<Body> bodies = new Array<Body>();
	
	private FixtureDef defaultFixture;
	
	public GameLevel(World physicsWorld, com.artemis.World entityWorld, float unitsPerPixel) {
		/* Set up logger interface */
		logger = new Logger("LevelLoader");
		logger.setLevel(Logger.DEBUG);
		logger.info("Initializing...");
		
		this.map = null;
		this.physicsWorld = physicsWorld;
		this.entityWorld = entityWorld;
		this.unitsPerPixel = unitsPerPixel;
		
		/* Set default fixture property definition */
		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.0f;
		defaultFixture.restitution = 0.0f;
	}
		
	public TiledMap loadFromFile(String fileName) {
		this.map = new TmxMapLoader().load(fileName);		
		this.loadPhysicsFromLayer("physics");
		this.loadEntitiesFromLayer("entities");
		
		return this.map;
	}
	
	/** 
	 * @param layerName name of the layer that contains physics shapes.
	 */
	private void loadPhysicsFromLayer(String layerName) {
		if (map == null) {
			logger.error("Map was not loaded properly.");
			return;
		}
		
		MapLayer layer = map.getLayers().get(layerName);
		
		if (layer == null) {
			logger.error("Layer " + layerName + " does not exist.");
			return;
		}
		
		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIterator = objects.iterator();
		
		while(objectIterator.hasNext()) {
			MapObject object = objectIterator.next();
			
			if (object instanceof TextureMapObject) {
				return;
			}
			
			Shape shape;
			
			if (object instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject)object);
			}
			else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject)object);
			}
			else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject)object);
			}
			else {
				logger.error("Non-supported shape " + object);
				continue;
			}
			
			FixtureDef fixtureDef = defaultFixture;
			fixtureDef.shape = shape;
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;
			
			Body body = physicsWorld.createBody(bodyDef);
			body.createFixture(fixtureDef).setUserData(GameBodyType.TERRAIN);
						
			bodies.add(body);
			
			fixtureDef.shape = null;
			shape.dispose();
		}
		
	}
	
	private void destroyPhysics() {
		for (Body body : bodies) {
			physicsWorld.destroyBody(body);
		}
		
		bodies.clear();
	}
	
	private Shape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		
		Vector2 size = new Vector2((rectangle.x + rectangle.width / 2) * unitsPerPixel, 
				(rectangle.y + rectangle.height / 2) * unitsPerPixel);
		
		polygon.setAsBox(rectangle.width * 0.5f * unitsPerPixel, rectangle.height * 0.5f * unitsPerPixel, size, 0.0f);
		
		return polygon;
	}
	
	private Shape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();
		float[] worldVertices = new float[vertices.length];
		
		for (int i = 0; i < vertices.length; ++i) { 
			worldVertices[i] = vertices[i] * unitsPerPixel;
		}
		
		polygon.set(worldVertices);
		return polygon;
	}
	
	private Shape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		
		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] * unitsPerPixel;
			worldVertices[i].y = vertices[i * 2 + 1] * unitsPerPixel;	
		}
		
		ChainShape chain = new ChainShape(); 
		chain.createChain(worldVertices);
		return chain;
	}
	
	private void loadEntitiesFromLayer(String layerName) {
		if (map == null) {
			logger.error("Map was not loaded properly.");
			return;
		}
		
		MapLayer layer = map.getLayers().get(layerName);
		
		if (layer == null) {
			logger.error("Layer " + layerName + " does not exist.");
			return;
		}
		
		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIterator = objects.iterator();
		
		while(objectIterator.hasNext()) {
			MapObject object = objectIterator.next();
			
			if (object instanceof RectangleMapObject) {
				RectangleMapObject entity = (RectangleMapObject) object;
				
				// Get Bounds
				Vector2 position = new Vector2();
				Vector2 size = new Vector2();

				position = entity.getRectangle().getCenter(position).scl(unitsPerPixel);
				size = entity.getRectangle().getSize(size).scl(0.5f * unitsPerPixel);
				
				Rectangle bounds = new Rectangle(position.x, position.y, size.x, size.y);
								
				// Get Properties
				MapProperties properties = entity.getProperties();
				
				// Create entity at position X,Y with properties P	
				Entity e = EntityFactory.create(entity.getName(), entityWorld, physicsWorld, bounds, properties);
				
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

	@Override
	public void dispose() {
		this.destroyPhysics();
	}		
}
