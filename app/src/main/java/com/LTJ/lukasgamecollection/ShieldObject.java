package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

public class ShieldObject extends SimpleGameObject {
	
	private boolean blinking, invisible;

	private byte maxBlinks;

	/**
	 * determines if the Object is destroyed
	 */
	public boolean destroyed;
	
	/**
	 * the Sprite of the Object
	 */
	public static Bitmap sprite;

	public ShieldObject(){
		destroyed = true;
	}

	public boolean isBlinking() {
		return blinking;
	}
	
	public boolean isInvisible() {
		return invisible;
	}

	public void blink(int framecounter){
		
		// every 10 frames
		if (framecounter % 10 == 0){
			invisible = !invisible;
			maxBlinks++;
		}
		
		if (maxBlinks > 7){
			destroyed = true;
			maxBlinks = 0;
			blinking = false;
		}
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

	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

	}
	
	public void systemDestroy(){
		//blink before they die
		blinking = true;
	}
	
	
	
	/**
	 * calls destroy if the Objects collide on the bottom.
	 * 
	 * @param obj
	 */
	public void systemDestroyOnCollision(SimpleGameObject obj) {
		if (collidesWith(obj)) {
			systemDestroy();
		}

	}
}
