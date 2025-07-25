package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

public class EnemyShip extends SimpleGameObject {
	
	
	/**
	 * determines if the Object is destroyed
	 */
	public boolean destroyed;


	/**
	 * counts the number of destroyed Objects
	 */
	public static short destroyedObjects = 0;
	
	/**
	 * Determines the speed in which the object moves
	 */
	public static float moveSpeed;
	
	/**
	 * applies a faktor to the score
	 */
	public static byte scoreMod;
	
	/**
	 * the Sprite of the Object
	 */
	public static Bitmap sprite;
	
	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

	}
	
	
	public EnemyShip(){
		destroyed = true;
	}
	

	/**
	 * checks if the GameObject collides with another object.
	 * 
	 * @param obj
	 *            the GameObject the current Object collides with.
	 * @return true if the Object collides with the other Object.
	 */
	public boolean collidesWith(SimpleGameObject obj) {
		if (!destroyed && obj.y <= this.y + this.height && obj.y + obj.height >= this.y
				&& obj.x + obj.width >= this.x && obj.x <= this.x + this.width){

			return true;
		}
		return false;
	}

	/**
	 * sets destroyed to true //doesn't actually 'destroy' the object.
	 */
	public void destroy() {
		
		systemDestroy();
		destroyedObjects++;
		
	}

	/**
	 * calls destroy if the Objects collide on the bottom.
	 * 
	 * @param obj
	 */
	public void destroyOnCollision(SimpleGameObject obj) {
		if (collidesWith(obj)) {
			destroy();
		}

	}
	
	public void systemDestroy(){
		this.destroyed = true;
	}
	
	
	
	/**
	 * calls destroy if the Objects collide.
	 * 
	 * @param obj
	 */
	public void systemDestroyOnCollision(SimpleGameObject obj) {
		if (collidesWith(obj)) {
			systemDestroy();
		}

	}
	
	
}
