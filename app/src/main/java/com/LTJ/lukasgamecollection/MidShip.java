package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

public class MidShip extends SimpleGameObject {
	/**
	 * determines if the Object is destroyed
	 */
	private boolean destroyed;

	/**
	 * is true when the ship is going to the right
	 */
	private boolean directionRight;


	/**
	 * counts the number of destroyed Objects
	 */
	private static short destroyedObjects = 0;
	
	/**
	 * applies a factor to the score
	 */
	private static short scoreMod;

	/**
	 * the Sprite of the Object
	 */
	private static Bitmap sprite;

	/**
	 * Determines the speed in which the object moves
	 */
	private static float moveSpeed;

	private static short spawnImprobability;

	private static short spawnTime;
	
	public ShieldObject shield;

	public MidShip(){
		setDimensions();
		destroyed = true;
		destroyedObjects = 0;
		shield = new ShieldObject();
		shield.setDimensions();
		shield.destroyed = false;
	}
	
	public static void initialize(short sM, float mS, short sI, short sT){
		setScoreMod(sM);
		setMoveSpeed(mS);
		MidShip.setSpawnImprobability(sI);
		MidShip.setSpawnTime(sT);
	}
	
	public static int getScore() {
		return (destroyedObjects * scoreMod);
	}

	public static void setMoveSpeed(float moveSpeed) {
		MidShip.moveSpeed = moveSpeed;
	}

	public static void setScoreMod(short scoreMod) {
		MidShip.scoreMod = scoreMod;
	}

	public static float getMoveSpeed() {
		return moveSpeed;
	}

	public static Bitmap getSprite() {
		return sprite;
	}

	public static void setSprite(Bitmap sp) {
		MidShip.sprite = sp;

	}

	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void create() {
		this.destroyed = false;
		shield.destroyed = false;
	}
	
	public boolean getDirectionRight() {
		return directionRight;
	}

	public boolean getDirectionLeft() {
		return !directionRight;
	}

	public void changeDirection() {
		directionRight = !directionRight;
	}

	public void systemDestroy(){
		this.destroyed = true;
	}

	public void destroy(){
		if (!shield.destroyed){
			shield.destroyed = true;
		} else {
			superDestroy();
		}
	}
	
	public boolean collidesWith(SimpleGameObject obj) {
		if (!destroyed && obj.y <= this.y + this.height && obj.y + obj.height >= this.y
				&& obj.x + obj.width >= this.x && obj.x <= this.x + this.width){

			return true;
		}
		return false;
	}

	public static short getSpawnImprobability() {
		
		return spawnImprobability;
	}
	public static void setSpawnImprobability(short s){
		spawnImprobability = s;
	}

	public static short getSpawnTime() {
		
		return spawnTime;
	}
	public static void setSpawnTime(short s){
		spawnTime = s;
	}

	public void superDestroy() {
		
		systemDestroy();
		destroyedObjects++;
	}
	
}
