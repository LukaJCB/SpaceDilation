package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

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
	 * applies a factor to the score
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

		collisionBoxes = Arrays.asList(new Box(0f, 1f, 0f, 0.4f), new Box(0.3f, 0.7f, 0, 1));
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
		if (!destroyed) {
			for (Box box: obj.collisionBoxes) {
				float objX1 = obj.x + box.x1 * obj.width;
				float objX2 = obj.x + box.x2 * obj.width;
				float objY1 = obj.y + box.y1 * obj.height;
				float objY2 = obj.y + box.y2 * obj.height;
				for (Box box2 : this.collisionBoxes) {
					float thisX1 = this.x + box2.x1 * this.width;
					float thisX2 = this.x + box2.x2 * this.width;
					float thisY1 = this.y + box2.y1 * this.height;
					float thisY2 = this.y + box2.y2 * this.height;

					if (objY1 <= thisY2 && objY2 >= thisY1 && objX1 <= thisX2 && objX2 >= thisX1)
						return true;
				}
			}
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

	
	public void systemDestroy(){
		this.destroyed = true;
	}
	
	

	
	
}
