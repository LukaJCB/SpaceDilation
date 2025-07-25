package com.LTJ.lukasgamecollection;


import java.util.List;

public class SimpleGameObject {




	/**
	 * Coordinates of the Object
	 */
	public float x, y;

	/**
	 * dimensions of the Object
	 */
	public float height,width;

	/**
	 * collision boxes are defined relative to the xy coordinates
	 */
	public List<Box> collisionBoxes;


	/**
	 * checks if the GameObject collides with another object.
	 *
	 * @param obj
	 *            the GameObject the current Object collides with.
	 * @return true if the Object collides with the other Object.
	 */
	public boolean collidesWith(SimpleGameObject obj) {
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

		return false;
	}
	
}


	
	
	
	
	
	
	

