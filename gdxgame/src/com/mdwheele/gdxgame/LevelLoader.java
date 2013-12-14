package com.mdwheele.gdxgame;

import java.util.Iterator;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
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
import com.badlogic.gdx.utils.Logger;

public class LevelLoader {

	private Logger logger;
	private Map map;
	private World physicsWorld;
	private float unitsPerPixel;
	private Array<Body> bodies = new Array<Body>();
	
	private FixtureDef defaultFixture;
	
	/** 
	 * @param world Box2d physics world to work in.
	 * @param unitsPerPixel conversion from pixel units to Box2d.
	 */
	public LevelLoader(Map map, World world, float unitsPerPixel) {
		/* Set up logger interface */
		logger = new Logger("LevelLoader");
		logger.info("Initializing...");
		
		this.map = map;
		this.physicsWorld = world;
		this.unitsPerPixel = unitsPerPixel;
		
		/* Set default fixture property definition */
		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;
	}
		
	public void createPhysics() {
		createPhysics("physics");
	}
	
	/** 
	 * @param layerName name of the layer that contains physics shapes.
	 */
	public void createPhysics(String layerName) {
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
			body.createFixture(fixtureDef);
			
			bodies.add(body);
			
			fixtureDef.shape = null;
			shape.dispose();
		}
		
	}
	
	public void destroyPhysics() {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
