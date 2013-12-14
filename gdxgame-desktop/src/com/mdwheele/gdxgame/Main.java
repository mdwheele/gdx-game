package com.mdwheele.gdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Ludum Dare 28: You Only Have One";
		cfg.useGL20 = false;
		cfg.width = 640;
		cfg.height = 480;
		
		new LwjglApplication(new GdxGame(cfg.width, cfg.height), cfg);
	}
}
