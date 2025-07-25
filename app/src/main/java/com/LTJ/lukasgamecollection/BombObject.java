package com.LTJ.lukasgamecollection;

public class BombObject extends SimpleGameObject {
	private boolean destroyed;
	
	private float yExplosion;
	
	private float explosionSpeed;
	private float canvasHeight;
	
	public void explode(){
		if (yExplosion >= 0){
			yExplosion -= explosionSpeed;
		} else {
			systemDestroy();
		}
	}
	
	public BombObject(){
		destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setNotDestroyed() {
		this.destroyed = false;
	}
	
	public void systemDestroy(){
		destroyed = true;
		setyExplosion(canvasHeight);
	}

	public void setyExplosion(float yExplosion) {
		this.yExplosion = yExplosion;
	}

	public float getyExplosion() {
		return yExplosion;
	}

	public void setCanvasHeight(float canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

	public void setExplosionSpeed(float explosionSpeed) {
		this.explosionSpeed = explosionSpeed;
	}
	
	
}
