package com.LTJ.lukasgamecollection;

import java.util.Random;
import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;;

public class TutorialSurface extends GameSurface implements SensorEventListener, OnTouchListener {

	private Random random;
	private Timer timer;
	private boolean instantiated, gameStarted, shootingExplained,
			rocketsExplained, bombsExplained;
	private Paint hudPaint;
	private Context context;
	private Intent intent;
	private SoundPool sounds;
	private int shotSfx, bossSpawnfx, upgradefx;
	private final Vibrator v1;

	// height and width of canvas
	private float height, width;

	private int frameCounter, bombCounter, score;

	static ShieldObject shield;
	private ShipObject ship;

	private EnemyShip enemyShip;
	private BossShip bossShip;
	private ShotUpgrade shotUpgrade;
	private ShieldUpgrade shieldUpgrade;
	private RocketUpgrade rocketUpgrade;
	private BombUpgrade bombUpgrade;
	private LifeUpgrade lifeUpgrade;

	private Bitmap rocketHUD, bombHUD, lifeHUD;

	private float lastY;

	// everything identified with m is part of the Sensor act
	private float mLastx, mDeltaX;
	private boolean mInitialized;
	private final float MNOISE = 1.1f;
	private Sensor mAccelerometer;
	private SensorManager sm = null;
	private Bitmap lifeHUDempty;

	public TutorialSurface(Context cont) {
		super(cont, 50);
		context = cont;
		// intialize objects
		intent = new Intent();
		random = new Random();
		timer = new Timer();
		hudPaint = new Paint();
		v1 = (Vibrator) cont.getSystemService(Context.VIBRATOR_SERVICE);

		// stars should be white
		hudPaint.setARGB(255, 255, 255, 255);

		rocketHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.rockethud);
		bombHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.bombhud);
		lifeHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.lifehud);
		lifeHUDempty = BitmapFactory.decodeResource(getResources(), R.drawable.lifehudempty);

		// set up sounds
		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		shotSfx = sounds.load(context, R.raw.shoot, 0);
		bossSpawnfx = sounds.load(context, R.raw.boss, 2);
		upgradefx = sounds.load(context, R.raw.upgrade, 1);

		/* setting up gameObjects */

		// set up ship
		ship = new ShipObject();
		ship.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.ship);
		ship.rocket.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.rocket);
		ship.rocket.explosion = BitmapFactory.decodeResource(getResources(),
				R.drawable.explosion);
		ship.setDimensions();
		ship.rocket.setDimensions();
		ship.destroyed = false;

		// set up shield
		ShieldObject.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.shield);
		shield = new ShieldObject();

		// maximum of 17 shots
		ship.shot = new ShotObject[17];
		ShotObject.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.shot);
		for (byte i = 0; i < ship.shot.length; i++) {
			ship.shot[i] = new ShotObject();
			ship.shot[i].setDimensions();
		}

		// set up enemies
		enemyShip = new EnemyShip();
		EnemyShip.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.enemyship);
		enemyShip.setDimensions();

		// set up bosses

		BossShotObject.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.bosshot);
		BossShip.bossSprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.bossship);
		bossShip = new BossShip();
		bossShip.setDimensions();

		// max of 3 shotUpgrades on screen
		ShotUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradeshot);
		shotUpgrade = new ShotUpgrade();
		shotUpgrade.setDimensions();

		// max of 1 shieldUpgrades on screen

		ShieldUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradeshield);
		shieldUpgrade = new ShieldUpgrade();
		shieldUpgrade.setDimensions();

		// max of 3 rocketUpgrades on screen

		RocketUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgraderocket);
		rocketUpgrade = new RocketUpgrade();
		rocketUpgrade.setDimensions();

		// bombUpgrade
		BombUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradebomb);
		bombUpgrade = new BombUpgrade();
		bombUpgrade.setDimensions();

		// lifeUpgrade
		lifeUpgrade = new LifeUpgrade();
		lifeUpgrade.setSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradelife));
		lifeUpgrade.setDimensions();

		// set up the accelerometer
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	public void render(Canvas c) {

		
		

			
		
		

		// draw frame&update every 15 ms
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		// draw background
		drawBackground(c);

		// draw Ship
		shipLogic(c);

		// draw shots
		shootMechanics(c);

		if (gameStarted) {

			if (bombsExplained){
				//STEP 4
				c.drawText("Congratulations", 0, height / 2, hudPaint);
				c.drawText("Press anywhere to continue!", 0,height/2+ height/29,hudPaint);
			} else if (rocketsExplained){
				// boss logic
				bossLogic(c);
				
				// enemy logic
				enemyShipLogic(c);
				
				//STEP 3
				c.drawText("If a bomb is available", 0, height / 2, hudPaint);
				c.drawText("Swipe DOWN to release it!", 0,height/2+ height/29,hudPaint);
			} else if (shootingExplained){
				// boss logic
				bossLogic(c);
				
				//STEP 2
				c.drawText("If rockets are available", 0, height / 2, hudPaint);
				c.drawText("Swipe UP to fire a rocket!", 0,height/2+ height/29,hudPaint);
			} else {
				// enemy logic
				enemyShipLogic(c);
				
				//begining STEP 1
				c.drawText("Touching your ship will start shooting", 0, height / 2, hudPaint);
				c.drawText("Touch again to stop.", 0, height/2+ height/29,hudPaint);
			}
			
			
			
			
			// draw Upgrades
			upgradeLogic(c);
			
			
			if (ship.getAvailableRockets() == 2){
				shootingExplained = true;
			}
			
			if (bombCounter != 0 && bombCounter + 40 == frameCounter){
				bombsExplained = true;
			}
			


			frameCounter++;

		} else {
			c.drawText("Press anywhere to begin",0, height / 2, hudPaint);
			
		}

		// UI
		displayHUD(c);

		// debug
		// c.drawText("boss x" + bossSpawnHelp((byte)2), 0, 140, hudPaint);
		// c.drawText("boss spd" + bossShip[0].moveSpeed, 0, 180, starPaint);
		// c.drawText("spntm" + this.spawnTime, 0, 220, starPaint);
		// c.drawText("shtm" + this.shotTime, 0, 260, starPaint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// shoot whenever the screen is touched
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// saves the Y values for swiping
			lastY = event.getY();
			
			gameStarted = true;
			
			if (bombsExplained){
				intent.setClass(getContext(), Tutorial.class);
				context.startActivity(intent);
			}
			
			// starts shooting if ship is pressed
			if (event.getX() >= ship.x && event.getX() <= ship.x + ship.width
					&& event.getY() >= ship.y
					&& event.getY() <= ship.y + ship.height) {
				ship.shooting = !ship.shooting;
			}
			break;

		case MotionEvent.ACTION_UP:
			// if the y value is smaller than lastY, it's a swipe up
			if (event.getY() < lastY - 100) {
				// shoot rocket on swipe up
				shootRocket();
				// swipe down
			} else if (event.getY() > lastY + 100) {
				// release bomb
				releaseBomb();
			}

		}

		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// retrieve the values of the accelerometer
		float x = event.values[0];

		// intialize the base variables
		if (!mInitialized) {
			mLastx = x;

			mInitialized = true;
		}

		// the difference between each axis
		mDeltaX = mLastx - x;

		// if difference is smaller than noise, disregard
		if (Math.abs(mDeltaX) < MNOISE) {
			mDeltaX = 0;
		}

	}

	
	public void resume() {
		sm.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		
	}

	@Override
	public void pause() {
		// to save battery
		sm.unregisterListener(this);
		super.pause();

	}

	public void initializeCanvas(Canvas c) {
		if (!instantiated) {
		height = c.getHeight();
		width = c.getWidth();

		// set Textsize for Hud
		hudPaint.setTextSize(height / 29);

		// set starting position of our ship
		ship.y = height - ship.height * 1.5f;
		ship.x = width / 2 - ship.width / 2;

		// shotTime is initially every 20 frames
		ship.shotTime = 20;

		// shotSpeed is one 36th of the canvas height
		ship.shotSpeed = height / 36;

		// rocketSpeed should be slightly higher than the individual shots
		ship.rocketSpeed = height / 26;

		// set explosiontime in frames of rocket
		ship.rocket.setExplosionTime((byte) 15);

		// explosionspeed of the bomb
		ship.bomb.setExplosionSpeed(20);

		// Ycoordinate of the bomb explosion
		ship.bomb.setyExplosion(height);

		// uses height to spawn next bomb
		ship.bomb.setCanvasHeight(height);

		// speed of ship
		ship.moveSpeed = width / 144;

		// set the max amount of rockets
		ship.setMaxRockets((byte) 3);

		// Initial speed of enemies
		EnemyShip.moveSpeed = height / 320;

		// reset defeated enemies
		EnemyShip.destroyedObjects = 0;

		// set score factor for enemyShip
		EnemyShip.scoreMod = 10;

		// speed that the upgrades move toward you.
		UpgradeObject.moveSpeed = height / 110;

		// reset defeated bosses
		BossShip.destroyedObjects = 0;

		// set score factor for bosses
		BossShip.scoreMod = 250;

		// spawnImprobability is initially high
		BossShip.spawnImprobability = 90;

		// spawnTime in frames is initially high
		BossShip.spawnTime = 100;

		// shotTime in frames is initially only every 22 frames
		BossShip.shotTime = 22;

		// set shotspeed for boss shots
		BossShip.shotSpeed = height / 86;

		BossShip.setSuperUpgradeImprobability((byte) 2);

		bossShip.moveSpeed = width / 50;
		
		gameStarted = shootingExplained = rocketsExplained = bombsExplained = false;
		
		instantiated = true;
		}
	}

	private void drawBackground(Canvas c) {
		// draw black background
		c.drawRGB(0, 0, 0);
		// draw stars

		// if (frameCounter % 20 == 0){
		// c.drawCircle(randFloat(0, width), randFloat(0, height),
		// randFloat(1f, 5f), hudPaint);
		// c.drawCircle(randFloat(0, width), randFloat(0, height),
		// randFloat(1f, 5f), hudPaint);
		// }
	}

	private void shipLogic(Canvas c) {

		if (shield.destroyed) {

			if (!enemyShip.destroyed) {
				// check for collision
				if (ship.collidesWith(enemyShip)) {
					// kill user ship
					if (ship.hasExtraLife()) {
						// subtract the extra life and vibrate
						ship.subExtraLife();
					} else {
						// destroy ship and vibrate
						ship.systemDestroy();
					}
					v1.vibrate(500);

					// kill enemy
					enemyShip.destroyed = true;
				}

			}

		}
		// draw the ship on the canvas
		drawShip(c);

	}

	private void drawShip(Canvas c) {
		if (!ship.destroyed) {
			// draw ship
			c.drawBitmap(ship.sprite, ship.x, ship.y, null);

			// draw shield
			if (!shield.destroyed) {
				// upgrade shield coords
				shield.x = ship.x - width / 144;
				shield.y = ship.y - height / 85.333334f;

				// check for blink
				if (shield.isBlinking()) {
					shield.blink(frameCounter);
					// draw shield when it's visible
					if (!shield.isInvisible()) {
						c.drawBitmap(ShieldObject.sprite, shield.x, shield.y,
								null);
					}
				} else {
					c.drawBitmap(ShieldObject.sprite, shield.x, shield.y, null);
				}

			}

			// ship moves the rotation of the accelerometer relative to
			// moveSpeed.
			ship.x += mDeltaX * ship.moveSpeed;

			// ship goes out of the right side
			if (ship.x > width) {
				ship.x = -ship.width / 2;
			}
			// ship goes out of the left side
			if (ship.x < -ship.width) {
				ship.x = width - ship.width / 2;

			}

			drawRocket(c);
			drawBomb(c);

		}
	}

	/**
	 * A shot can only be made once the shootFrameCounter reaches the shotTime.
	 * Everytime a shot is made the shootFrameCounter is reset and the
	 * shotCounter increases. If the FIRST shot made (shot[0]) is out of the
	 * screen, the shotCounter is reset, so that the old shots (that are out of
	 * screen) can be assigned to new values. Every shot is positioned right
	 * inside of the ship and moves with shotSpeed pixels per frame. The shot is
	 * drawn right AFTER its position is updated.
	 */
	private void shootMechanics(Canvas c) {

		// check if the first shot is out of screen
		if (ship.shot[0].y < 0) {

			// reset shotCount
			ship.shotCounter = 0;
		}

		// shoot mechanics
		if (ship.shooting) {

			// create new shots every shotSpeed frames if shooting
			if (ship.shootFrameCounter >= ship.shotTime) {

				// new shot
				ship.shot[ship.shotCounter].x = ship.x + ship.width / 2.3f;
				ship.shot[ship.shotCounter].y = ship.y;

				// make shot noise
				sounds.play(shotSfx, 0.1f, 0.1f, 0, 0, 1);

				// next shot
				ship.shotCounter++;

				// reset frameCounter
				ship.shootFrameCounter = 0;

			} else {
				// wait for next frame
				ship.shootFrameCounter++;

			}
		}

		// move & draw shots
		for (SimpleGameObject shots : ship.shot) {

			// move
			shots.y -= ship.shotSpeed;
			// draw
			c.drawBitmap(ShotObject.sprite, shots.x, shots.y, null);
		}

	}

	private void shootRocket() {
		if (ship.rocketsAvailable() && ship.rocket.destroyed) {
			ship.subRocket();
			// create new rocket
			ship.rocket.destroyed = false;
			ship.rocket.x = ship.x + ship.width / 2.3f;
			ship.rocket.y = ship.y;
			ship.rocket.setDimensions();
		}

	}

	private void drawRocket(Canvas c) {

		if (!ship.rocket.destroyed) {

			// check if exploded
			if (ship.rocket.isExploded()) {

				// draw explosion in the middle of where the rocket was
				c.drawBitmap(ship.rocket.explosion, ship.rocket.x
						- ship.rocket.width / 3, ship.rocket.y
						- ship.rocket.height / 3, null);

				// check for collisions

				if (!enemyShip.destroyed && ship.rocket.collidesWith(enemyShip)) {
					enemyShip.destroy();
				}

				if (!bossShip.destroyed && ship.rocket.collidesWith(bossShip)) {
					// apply damage to ship
					bossShip.hit();
					// might spawn superupgrade if destroyed after damage is
					// applied
					spawnSuperUpgrade(bossShip.destroyed,bossShip.x,bossShip.y);

				}

				// checks if the explosion should still be drawed
				ship.rocket.checkExplosionCounter();
				// adds a frame to the explosioncounter
				ship.rocket.addExplosionCounter();

			} else {

				// draw rocket
				c.drawBitmap(ship.rocket.sprite, ship.rocket.x, ship.rocket.y,
						null);

				// move Rocket
				ship.rocket.y -= ship.rocketSpeed;

				// explode if it goes out of bounds
				if (ship.rocket.y <= 0)
					ship.rocket.explode();

				// check for collisions

				if (!enemyShip.destroyed && ship.rocket.collidesWith(enemyShip)) {
					// apply damage to ship
					enemyShip.destroy();

					ship.rocket.explode();
				}

				if (!bossShip.destroyed && ship.rocket.collidesWith(bossShip)) {
					// apply damage to ship
					bossShip.hit(10);

					ship.rocket.explode();
					// might spawn superupgrade if destroyed after damage is
					// applied

				}
			}

		}
	}

	private void releaseBomb() {
		if (ship.hasBomb()) {
			ship.bomb.setNotDestroyed();
			ship.subBomb();
			
			bombCounter = frameCounter;
		}

	}

	private void drawBomb(Canvas c) {
		if (!ship.bomb.isDestroyed()) {
			c.drawLine(0, ship.bomb.getyExplosion(), width,
					ship.bomb.getyExplosion(), hudPaint);

			// move the line and destroy when the line goes out of bounds
			ship.bomb.explode();

			// check for collision

			if (!enemyShip.destroyed
					&& ship.bomb.getyExplosion() <= enemyShip.y
							+ enemyShip.height) {
				enemyShip.destroy();
			}

			if (!bossShip.destroyed
					&& ship.bomb.getyExplosion() <= bossShip.y
							+ bossShip.height) {
				bossShip.hit(10);
			}

		}
	}

	private void enemyShipLogic(Canvas c) {

		spawnEnemyShip();

		// enemy logic

		if (!enemyShip.destroyed) {

			// draw enemy ship
			c.drawBitmap(EnemyShip.sprite, enemyShip.x, enemyShip.y, null);

			// enemy is above the bottom
			if (enemyShip.y + EnemyShip.moveSpeed < height) {
				// enemy moves to the bottom
				enemyShip.y += EnemyShip.moveSpeed;
			} else {
				// destroy enemy when it goes out of the screen.
				enemyShip.systemDestroy();
			}

			// check for collision
			for (SimpleGameObject shots : ship.shot) {

				if (enemyShip.collidesWith(shots)) {

					// destroys the ship
					enemyShip.destroy();
					
					this.spawnUpgrade(enemyShip.x, enemyShip.y, UpgradeObject.ROCKET);

					// destroy the shot
					shots.y = -1;
				}
			}

		}

	}

	private void spawnEnemyShip() {
		// every spawnTime frames
		
		
		if (enemyShip.destroyed && frameCounter % 10 == 0){
			enemyShip.destroyed = false;
			enemyShip.x = randFloat(0,width - enemyShip.width);
			enemyShip.y = 0 - enemyShip.height;
		}
		
		
	}

	/**
	 * spawns a new upgrade at the coordinates where the enemyShip was destroyed
	 * 
	 * @param x
	 *            the x value of the destroyed enemyShip
	 * @param y
	 *            the y value of the destroyed enemyShip
	 */
	private void spawnUpgrade(float x, float y, byte type) {

		switch (type) {

		case UpgradeObject.SHOT: // spawn a new upgrade.

			if (shotUpgrade.destroyed) {
				shotUpgrade.x = x;
				shotUpgrade.y = y;
				shotUpgrade.destroyed = false;

				// breaks the loop to only spawn one upgrade
				break;
			}

			break;

		case UpgradeObject.SHIELD:

			if (shieldUpgrade.destroyed) {
				shieldUpgrade.x = x;
				shieldUpgrade.y = y;
				shieldUpgrade.destroyed = false;

				// breaks the loop to only spawn one upgrade
				break;
			}

			break;
		case UpgradeObject.ROCKET:

			if (rocketUpgrade.destroyed) {
				rocketUpgrade.x = x;
				rocketUpgrade.y = y;
				rocketUpgrade.destroyed = false;
				// breaks the loop to only spawn one upgrade
				break;
			}

			break;

		case UpgradeObject.BOMB:

			if (bombUpgrade.destroyed) {
				bombUpgrade.x = x;
				bombUpgrade.y = y;
				bombUpgrade.destroyed = false;

			}

			break;
		case UpgradeObject.LIFE:
			if (lifeUpgrade.destroyed) {
				lifeUpgrade.x = x;
				lifeUpgrade.y = y;
				lifeUpgrade.destroyed = false;
			}
			break;

		}

	}

	private void bossLogic(Canvas c) {

		
		// boss Logic

		// movement + draw + collision
		if (!bossShip.destroyed) {
			
		

			// Draw
			c.drawText("" + bossShip.getHealth(), bossShip.x, bossShip.y,
					hudPaint);
			c.drawBitmap(BossShip.bossSprite, bossShip.x, bossShip.y, null);

			if (bossShip.onScreen) {
				// ship about to go out of the right side of screen
				if (bossShip.x + bossShip.moveSpeed + bossShip.width >= width) {
					// reverse movement
					bossShip.moveSpeed *= -1;
				}
				// boss about to go out of the left side of screen.
				if (bossShip.x + bossShip.moveSpeed <= 0) {
					// reverse movement
					bossShip.moveSpeed *= -1;
				}
			} else {
				// boss is out of the left side of the screen
				if (bossShip.x < 0) {
					bossShip.moveSpeed = width / 50;
				} else if (bossShip.x > width) {
					bossShip.moveSpeed = -width / 50;
				}

				if (bossShip.x > 0 && bossShip.x + bossShip.width < width) {
					bossShip.onScreen = true;
				}

			}
			// move boss
			bossShip.x += bossShip.moveSpeed;

			// reduces health upon shooting
			for (ShotObject shots : ship.shot) {
				if (bossShip.collidesWith(shots)) {
					// apply damage to boss
					bossShip.hit();

					// destroy the shot
					shots.y = -1;
					
					spawnSuperUpgrade(bossShip.destroyed,bossShip.x,bossShip.y);

				}
			}
		} else {
			// spawn new boss
			bossSpawn();
		}

	}

	private void bossSpawn() {
		if (frameCounter % BossShip.spawnTime == 0) {

			// spawns new boss
			sounds.play(bossSpawnfx, 1, 1, 1, 0, 1);

			bossShip.onScreen = false;
			bossShip.setHealth(20);
			bossShip.y = height / 30;
			bossShip.x = width - bossShip.width;
			bossShip.destroyed = false;

		}

	}

	private void upgradeLogic(Canvas c) {

		if (!shotUpgrade.destroyed) {

			// draw upgrade
			c.drawBitmap(ShotUpgrade.sprite, shotUpgrade.x, shotUpgrade.y, null);

			// upgrade is above the bottom
			if (shotUpgrade.y + UpgradeObject.moveSpeed < height) {
				// upgrade moves to the bottom
				shotUpgrade.y += UpgradeObject.moveSpeed;
			} else {
				// destroy upgrade when it goes out of the screen.
				shotUpgrade.systemDestroy();
			}

			// check for collision
			if (shotUpgrade.collidesWith(ship)) {

				sounds.play(upgradefx, 1, 1, 1, 0, 1);
				upgradeShotTime();
				shotUpgrade.systemDestroy();
			}

		}

		if (shield.destroyed) {

			if (!shieldUpgrade.destroyed) {

				// draw upgrade
				c.drawBitmap(ShieldUpgrade.sprite, shieldUpgrade.x,
						shieldUpgrade.y, null);

				// upgrade is above the bottom
				if (shieldUpgrade.y + UpgradeObject.moveSpeed < height) {
					// upgrade moves to the bottom
					shieldUpgrade.y += UpgradeObject.moveSpeed;
				} else {
					// destroy upgrade when it goes out of the screen.
					shieldUpgrade.systemDestroy();
				}

				// check for collision
				if (shieldUpgrade.collidesWith(ship)) {

					sounds.play(upgradefx, 1, 1, 1, 0, 1);
					upgradeShield(6000);
					shieldUpgrade.systemDestroy();
				}

			}

		}

		if (!rocketUpgrade.destroyed) {

			// draw upgrade
			c.drawBitmap(RocketUpgrade.sprite, rocketUpgrade.x,
					rocketUpgrade.y, null);

			// upgrade is above the bottom
			if (rocketUpgrade.y + UpgradeObject.moveSpeed < height) {
				// upgrade moves to the bottom
				rocketUpgrade.y += UpgradeObject.moveSpeed;
			} else {
				// destroy upgrade when it goes out of the screen.
				rocketUpgrade.systemDestroy();
			}

			// check for collision
			if (rocketUpgrade.collidesWith(ship)) {

				sounds.play(upgradefx, 1, 1, 1, 0, 1);
				upgradeRockets();
				rocketUpgrade.systemDestroy();
			}

		}

		if (!bombUpgrade.destroyed) {
			// draw upgrade
			c.drawBitmap(BombUpgrade.sprite, bombUpgrade.x, bombUpgrade.y, null);

			// upgrade is above the bottom
			if (bombUpgrade.y + UpgradeObject.moveSpeed < height) {
				// upgrade moves to the bottom
				bombUpgrade.y += UpgradeObject.moveSpeed;
			} else {
				// destroy upgrade when it goes out of the screen.
				bombUpgrade.systemDestroy();
			}

			// check for collision
			if (bombUpgrade.collidesWith(ship)) {

				sounds.play(upgradefx, 1, 1, 1, 0, 1);

				upgradeBombs();
				bombUpgrade.systemDestroy();
			}
		}

		if (!lifeUpgrade.destroyed) {
			// draw upgrade
			c.drawBitmap(lifeUpgrade.getSprite(), lifeUpgrade.x, lifeUpgrade.y,
					null);

			// upgrade is above the bottom
			if (lifeUpgrade.y + UpgradeObject.moveSpeed < height) {
				// upgrade moves to the bottom
				lifeUpgrade.y += UpgradeObject.moveSpeed;
			} else {
				// destroy upgrade when it goes out of the screen.
				lifeUpgrade.systemDestroy();
			}

			// check for collision
			if (lifeUpgrade.collidesWith(ship)) {

				sounds.play(upgradefx, 1, 1, 1, 0, 1);

				upgradeLife();
				lifeUpgrade.systemDestroy();
			}
		}

	}

	private void upgradeLife() {
		ship.setHasExtraLife();

	}

	private void upgradeBombs() {

		ship.setHasBomb();

	}

	/**
	 * Upgrades your shotTime
	 */
	private void upgradeShotTime() {
		// reduces shotTime
		if (ship.shotTime >= 6) {
			ship.shotTime -= 4;
		}

	}

	private void upgradeRockets() {

		// add a rocket to your rockets
		ship.addRocket();
	}

	/**
	 * upgrades your ship and gives you a shield for shieldTime seconds
	 */
	private void upgradeShield(long milliSeconds) {
		// set shield to your ship
		shield.x = ship.x - width / 144;
		shield.y = ship.y - height / 85.333334f;
		shield.destroyed = false;
		// shield should last 5000ms = 5s.
		ShieldDestroyer timerTask = new ShieldDestroyer();
		timer.schedule(timerTask, milliSeconds);
	}

	private void scoreLogic() {
		score = EnemyShip.destroyedObjects * EnemyShip.scoreMod
				+ BossShip.destroyedObjects * BossShip.scoreMod
				+ MidShip.getScore();

	}

	private void displayHUD(Canvas c) {

		displayScore(c);

		displayRocketHud(c);

		displayBombHud(c);

		displayLifeHud(c);
	}

	private void displayScore(Canvas c) {
		// update Score
		scoreLogic();
		// display score overlay
		c.drawText("Score: " + score, 80, 80, hudPaint);
	}
	
	private void displayRocketHud(Canvas c){
		// display available Rockets
				for (byte i = 0; i < ship.getAvailableRockets(); i++) {
					c.drawBitmap(rocketHUD, 360 + i * 50, 45, null);
				}
	}
	
	private void displayBombHud(Canvas c){
		// display available Bomb
				if (ship.hasBomb()) {
					c.drawBitmap(bombHUD, 80, 120, null);
				}
	}
	
	private void displayLifeHud(Canvas c){
		
		//display empty hud
		c.drawBitmap(lifeHUDempty,135,135,null);
		
		// display available extra life
				if (ship.hasExtraLife()) {
					c.drawBitmap(lifeHUD, 140, 140, null);
				}
	}

	/**
	 * helper method to get random floats in range.
	 * @return returns a random float between min and max.
	 */
	private float randFloat(float min, float max) {
		//new random number between min and max
		float randomNum = random.nextFloat() * (max - min) + min;
		
		return randomNum;

	}
	
	/**
	 * Might spawn a super upgrade if the ship was destroyed
	 * @param destroyed
	 * @param y 
	 * @param x 
	 */
	private void spawnSuperUpgrade(boolean destroyed, float x, float y) {
		
		if (destroyed){
			spawnUpgrade(x,y,UpgradeObject.BOMB);
			
			rocketsExplained = true;
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
