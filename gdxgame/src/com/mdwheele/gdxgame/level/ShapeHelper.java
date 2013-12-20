package com.mdwheele.gdxgame.level;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class ShapeHelper {

	public static Shape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		
		Vector2 size = new Vector2(GameWorld.toBox2d((rectangle.x + rectangle.width / 2)), GameWorld.toBox2d(rectangle.y + rectangle.height / 2));		
		polygon.setAsBox(GameWorld.toBox2d(rectangle.width / 2), GameWorld.toBox2d(rectangle.height / 2), size, 0.0f);
		
		return polygon;
	}
	
	public static Shape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();
		float[] worldVertices = new float[vertices.length];
		
		for (int i = 0; i < vertices.length; ++i) { 
			worldVertices[i] = GameWorld.toBox2d(vertices[i]);
		}
		
		polygon.set(worldVertices);
		return polygon;
	}
	
	public static Shape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		
		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = GameWorld.toBox2d(vertices[i * 2]);
			worldVertices[i].y = GameWorld.toBox2d(vertices[i * 2 + 1]);	
		}
		
		ChainShape chain = new ChainShape(); 
		chain.createChain(worldVertices);
		return chain;
	}
	
}
