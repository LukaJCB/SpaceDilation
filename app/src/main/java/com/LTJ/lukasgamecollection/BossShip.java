package com.LTJ.lukasgamecollection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BossShip extends ShipObject {
	
	private int shotCounter;


	private int shootFrameCounter;


	private int health;
	
	public BossShotObject[] shot;

	public boolean onScreen;
	
	static private byte superUpgradeImprobability;

	/**
	 * counts the number of destroyed Objects
	 */
	public static short destroyedObjects = 0;


	public static int shotTime;


	public static float shotSpeed;
	
	/**
	 * applies a factor to the score
	 */
	public static short scoreMod;

	/**
	 * determines when and how often the boss should be spawned
	 */
	public static byte spawnTime, spawnImprobability;
	
	/**
	 * the Sprite of the Object
	 */
	public static Bitmap bossSprite;
	
	public BossShip(){
		super();
		
		// maximum of 15 shots
		shot = new BossShotObject[15];
		for (byte i = 0; i < shot.length; i++) {
			shot[i] = new BossShotObject();
			//for this to work a sprite has to be set before this method is called
			shot[i].setDimensions();
		}
		
	}
	
	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = bossSprite.getHeight();
		width = bossSprite.getWidth();

		collisionBoxes = List.of(new Box(0f, 1f, 0f, 1f));
	}
	
	public void setHealth(int i) {
		this.health = i;
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
	public void hitOnCollision(SimpleGameObject obj) {
		if (collidesWith(obj)) {
			hit();
		}

	}
	
	
	public void hit() {
		if (health > 1){
			health-= 1;
		} else{
			destroy();
		}
		
	}
	
	public void hit(int dmg){
		if (health > dmg){
			health-= dmg;
		} else {
			destroy();
		}
	}
	
	/**
	 * Method derived from the Method in SpaceSurface.
	 * Will shoot upwards instead of down.
	 * A shot can only be made once the shootFrameCounter reaches the shotTime.
	 * Every time a shot is made the shootFrameCounter is reset and the
	 * shotCounter increases. If the FIRST shot made (shot[0]) is out of the
	 * screen, the shotCounter is reset, so that the old shots (that are out of
	 * screen) can be assigned to new values. Every shot is positioned right
	 * inside of the ship and moves with shotSpeed pixels per frame. The shot is
	 * drawn right AFTER its position is updated.
	 */
	public void shootMechanics(Canvas c,float height) {
	
		// check if the first shot is out of screen
		if (shot[0].y >= height) {
	
			// reset shotCount
			shotCounter = 0;
		}
	
		// shoot mechanics
		
	
			// create new shots every shotTime frames if shooting
			if (shootFrameCounter >= shotTime) {
	
				// new shot
				shot[shotCounter].x = x + width / 2;
				shot[shotCounter].y = y + this.height;
	
				// next shot
				shotCounter++;
	
				// reset frameCounter
				shootFrameCounter = 0;
	
			} else {
			// wait for next frame
				shootFrameCounter++;
			}
		
	
		// move & draw shots
		for (BossShotObject shots : shot) {
	
			// move
			shots.y += shotSpeed;
			// draw
			c.drawBitmap(BossShotObject.sprite, shots.x, shots.y, null);
		}
	
	}
	public int getHealth() {
		return health;
	}
	
	/**
	 * will return true if the boss should spawn an upgrade
	 * @param rand
	 * @return
	 */
	public static boolean spawnUpgrade(Random rand){
		//will generate a random int between 0 and the improbability
		return rand.nextInt(superUpgradeImprobability) == 0;
		
	}
	public static void setSuperUpgradeImprobability(byte superUpgradeImprobability) {
		BossShip.superUpgradeImprobability = superUpgradeImprobability;
	}
}
