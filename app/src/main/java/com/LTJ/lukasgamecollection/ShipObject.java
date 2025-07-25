package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

import java.util.List;

public class ShipObject extends SimpleGameObject {
	
	
	private byte availableRockets;

	private byte maxRockets;
	
	private boolean hasLife;

	private boolean hasBomb;
	
	public BombObject bomb;
	


	/**
	 * determines if the Object is destroyed
	 */
	public boolean destroyed;

	/**
	 * Determines the speed in which the object moves
	 */
	public float moveSpeed;
	
	
	/**
	 * the Sprite of the Object
	 */
	public Bitmap sprite;

	public boolean shooting;
	
	public ShotObject[] shot;
	public RocketObject rocket;

	public byte shootFrameCounter, shotCounter , shotTime; //shotTime MUST BE HIGHER THAN 1
	public float shotSpeed, rocketSpeed;

	public ShipObject(){
		destroyed = true;
		rocket = new RocketObject();
		bomb = new BombObject();

	}
	

	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

		collisionBoxes = List.of(new Box(0.1f, 0.85f, 0.5f, 1f),new Box(0.35f, 0.65f, 0f, 1f));
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


	public void setMaxRockets(byte maxRockets) {
		//only apply logical values
		if (maxRockets > 0)
			this.maxRockets = maxRockets;
	}


	public byte getAvailableRockets() {
		return availableRockets;
	}


	public void setAvailableRockets(byte availableRockets) {
		//only apply logical values
		if (availableRockets <= maxRockets && availableRockets >= 0)
			this.availableRockets = availableRockets;
	}
	
	public void addRocket(){
		if (availableRockets < maxRockets){
			availableRockets++;
		}
	}
	
	public void subRocket(){
		if(availableRockets > 0){
			availableRockets--;
		}
	}


	public boolean hasBomb() {
		return hasBomb;
	}


	public void setHasBomb() {
		if (!hasBomb){
			this.hasBomb = true;
		}
	}
	
	public void subBomb() {
		hasBomb = false;
	}

	public boolean rocketsAvailable() {
		
		return availableRockets > 0;
	}


	public boolean hasExtraLife() {
		
		return this.hasLife;
	}
	
	public void setHasExtraLife() {
		
			this.hasLife = true;
		
	}
	
	public void subExtraLife() {
		hasLife = false;
	}

	
	
	
	
}
