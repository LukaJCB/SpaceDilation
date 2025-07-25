package com.LTJ.lukasgamecollection;



public class UpgradeObject extends SimpleGameObject {
	
	
	/**
	 * determines if the Object is destroyed
	 */
	public boolean destroyed;



	/**
	 * Determines the speed in which the object moves
	 */
	public static float moveSpeed;
	
	
	/**
	 * Determines the shot type of Upgrade to be spawned
	 */
	public static final byte SHOT = 0;
	
	/**
	 * Determines the shield type of Upgrade to be spawned
	 */
	public static final byte SHIELD = 1;
	
	
	public static final byte ROCKET = 2;
	
	public static final byte BOMB = 3;



	public static final byte LIFE = 4;
	
	public UpgradeObject(){
		destroyed = true;
	}



	public boolean collidesWith(SimpleGameObject obj) {
		return !destroyed && super.collidesWith(obj);
	}
	
	
	/**
	 * sets destroyed to true //doesn't actually 'destroy' the object.
	 */
	public void systemDestroy(){
		this.destroyed = true;

	}
	

	
}
