package com.mdwheele.gdxgame.services;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements Disposable {	
	public enum GameSound {
		ARROW_SHOT("sfx/arrow.wav");
		
		private final String fileName;
		
		private GameSound(String fileName) {
			this.fileName = fileName;
		}
		
		public String fileName() {
			return fileName;
		}
	}
	
	/**
	 * Whether or not sound is enabled.
	 */
	private boolean enabled = true;
	
	/**
	 * Volume to be used for sound playback.
	 */
	private float volume = 1f;
	
	/**
	 * Sound cache.
	 */
	private HashMap<GameSound, Sound> soundCache;
	
	public SoundManager() {
		this.soundCache = new HashMap<GameSound, Sound>();
	}
	
	/**
	 * Sets the volume
	 * @param volume float between 0 and 1.
	 */
	public void setVolume(float volume) {
		if (volume < 0 || volume > 1f) {
			throw new IllegalArgumentException("The volume must be above zero and below one.");
		}
		
		this.volume = volume;
	}
	
	/**
	 * Enables sound playback.
	 */
	public void enable() { this.enabled = true; }
	
	/**
	 * Disabled sound playback.
	 */
	public void disable() { this.enabled = false; }
	
	public void play(GameSound sound) {
		this.play(sound, volume);
	}
	
	public void play(GameSound sound, float volume) {
		if(!enabled) return;
		
		Sound sfx = soundCache.get(sound);
		if(sfx == null) {
			FileHandle soundFile = Gdx.files.internal(sound.fileName());
			sfx = Gdx.audio.newSound(soundFile);
			soundCache.put(sound, sfx);
		}
		
		sfx.play(volume);
	}
	
	@Override
	public void dispose() {
		/**
		 * Clean up sounds in the cache.
		 */
		for(Sound sound: this.soundCache.values()) {
			sound.dispose();
		}
	}
}
