package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

import java.util.List;

public class RocketObject extends SimpleGameObject {
	/**
	 * determines if the Object is destroyed
	 */
	public boolean destroyed;
	
	private boolean exploded;
	
	private byte explosionCounter;

	private byte explosionTime;

	/**
	 * the Sprite of the Object
	 */
	public Bitmap sprite,explosion;

	public void setExplosionTime(byte time) {
		this.explosionTime = time;
	}


	public RocketObject(){
		destroyed = true;
	}
	
	
	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

		collisionBoxes = List.of(new Box(0f, 1f, 0f, 1f));
	}
	
	/**
	 * checks if the GameObject collides with another object.
	 * 
	 * @param obj
	 *            the GameObject the current Object collides with.
	 * @return true if the Object collides with the other Object.
	 */
	public boolean collidesWith(SimpleGameObject obj) {
		return !destroyed && super.collidesWith(obj);
	}
	
	public void systemDestroy(){
		this.destroyed = true;

	}
	
	public void turnInto(Bitmap newSprite){
		this.sprite = newSprite;
		setDimensions();
	}


	public void explode() {
		setExploded(true);
		this.height = explosion.getHeight();
		this.width = explosion.getWidth();
	}


	public boolean isExploded() {
		return exploded;
	}


	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}


	public void checkExplosionCounter() {
		if (explosionCounter >= explosionTime){
			
			//stop drawing and destroy the object
			this.exploded = false;
			systemDestroy();
			//reset the explosionCounter for next Time
			explosionCounter = 0;
		}
	}


	public void addExplosionCounter() {
		this.explosionCounter++;
	}


}
