package com.LTJ.lukasgamecollection;

import java.util.ArrayList;
import java.util.List;
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
import android.view.View.OnTouchListener;
import android.view.View;

public class SpaceSurface extends GameSurface implements SensorEventListener,
		OnTouchListener {

	private Random random;
	private Timer timer;
	private boolean instantiated, gameStarted;
	private boolean[] difficulty;
	private Paint hudPaint;
	private Context context;
	private Intent intent;
	private SoundPool sounds;
	private int shotSfx, bossSpawnfx, upgradefx;
	private final Vibrator v1;

	// height and width of canvas
	private float height, width;

	private int frameCounter, score;

	static ShieldObject shield;
	private ShipObject ship;

	private List<EnemyShip> enemyShip;
	private ArrayList<MidShip> midShip;
	private BossShip[] bossShip;
	private ShotUpgrade[] shotUpgrade;
	private ShieldUpgrade[] shieldUpgrade;
	private RocketUpgrade[] rocketUpgrade;
	private BombUpgrade bombUpgrade;
	private LifeUpgrade lifeUpgrade;

	private Bitmap rocketHUD, bombHUD, lifeHUD;
	private Bitmap lifeHUDempty;

	private byte spawnTime, spawnImprobability, shotUpgradeImprobability,
			shieldUpgradeImprobability, rocketUpgradeImprobability;

	private float lastY;

	// everything identified with m is part of the Sensor act
	private float mLastx, mDeltaX;
	private boolean mInitialized;
	private final float MNOISE = 1.1f;
	private Sensor mAccelerometer;
	private SensorManager sm = null;

	public SpaceSurface(Context cont) {
		super(cont,50);
		context = cont;
		// intialize objects
		random = new Random();
		intent = new Intent();
		timer = new Timer();
		hudPaint = new Paint();
		v1 = (Vibrator) cont.getSystemService(Context.VIBRATOR_SERVICE);
		difficulty = new boolean[20];

		// stars should be white
		hudPaint.setARGB(255, 255, 255, 255);

		rocketHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.rockethud);
		bombHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.bombhud);
		lifeHUD = BitmapFactory.decodeResource(getResources(),
				R.drawable.lifehud);

		lifeHUDempty = BitmapFactory.decodeResource(getResources(),
				R.drawable.lifehudempty);

		// ambient = MediaPlayer.create(cont, resid);
		// ambient.setLooping(true);

		// set up sounds
		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		shotSfx = sounds.load(context, R.raw.shoot, 0);
		bossSpawnfx = sounds.load(context, R.raw.boss, 2);
		upgradefx = sounds.load(context, R.raw.upgrade, 1);

		// bossSpawnfx = MediaPlayer.create(cont, R.raw.boss);
		// upgradefx = MediaPlayer.create(cont, R.raw.upgrade);

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
		enemyShip = new ArrayList<EnemyShip>();
		EnemyShip.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.enemyship);
		// 12 enemies
		for (byte i = 0; i < 12; i++) {
			enemyShip.add(new EnemyShip());
			enemyShip.get(i).setDimensions();
		}
		// set up mid-tier enemies
		midShip = new ArrayList<MidShip>();
		MidShip.setSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.midship));
		for (byte i = 0; i < 4; i++) {
			midShip.add(new MidShip());

		}

		// set up bosses
		bossShip = new BossShip[2];
		BossShotObject.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.bosshot);
		BossShip.bossSprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.bossship);
		for (byte i = 0; i < bossShip.length; i++) {
			bossShip[i] = new BossShip();
			bossShip[i].setDimensions();
		}

		// max of 3 shotUpgrades on screen
		shotUpgrade = new ShotUpgrade[3];
		ShotUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradeshot);
		for (byte i = 0; i < shotUpgrade.length; i++) {
			shotUpgrade[i] = new ShotUpgrade();
			shotUpgrade[i].setDimensions();
		}
		// max of 1 shieldUpgrades on screen
		shieldUpgrade = new ShieldUpgrade[1];
		ShieldUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgradeshield);
		for (byte i = 0; i < shieldUpgrade.length; i++) {
			shieldUpgrade[i] = new ShieldUpgrade();
			shieldUpgrade[i].setDimensions();
		}

		// max of 3 rocketUpgrades on screen
		rocketUpgrade = new RocketUpgrade[3];
		RocketUpgrade.sprite = BitmapFactory.decodeResource(getResources(),
				R.drawable.upgraderocket);
		for (byte i = 0; i < rocketUpgrade.length; i++) {
			rocketUpgrade[i] = new RocketUpgrade();
			rocketUpgrade[i].setDimensions();
		}

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

			// enemy logic
			enemyShipLogic(c);

			// boss logic
			bossLogic(c);

			// midtier logic
			midShipLogic(c);

			// draw Upgrades
			upgradeLogic(c);

			// update difficulty
			difficultyChanges();

			if (ship.destroyed) {

				c.drawText("GAME OVER!", width / 3, height / 2, hudPaint);
				intent.putExtra("scoreKey", score);
				intent.setClass(getContext(), ScoreBoard.class);
				context.startActivity(intent);
			}

			frameCounter++;

		} else {
			c.drawText("Start shooting ", width / 3, height / 2, hudPaint);
			c.drawText("to begin game", width / 3, height / 2 + height / 29,
					hudPaint);
			if (ship.shooting) {
				gameStarted = true;
			}
		}

		// UI
		displayHUD(c);

		// debug
		// c.drawText("boss x" + bossSpawnHelp((byte)2), 0, 140, hudPaint);
		// c.drawText("boss spd" + bossShip[0].moveSpeed, 0, 180, starPaint);
		// c.drawText("spntm" + this.spawnTime, 0, 220, starPaint);
		// c.drawText("shtm" + this.shotTime, 0, 260, starPaint);
	}

	public boolean onTouch(View v, MotionEvent event) {

		// shoot whenever the screen is touched
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// saves the Y values for swiping
			lastY = event.getY();

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

	public void pause() {
		// to save battery
		sm.unregisterListener(this);
		super.pause();

	}

	@Override
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

			// spawnTime in frames for enemies
			spawnTime = 10;

			// spawnimprob is initially high
			spawnImprobability = 21;

			// upgradeimprobability
			shotUpgradeImprobability = 40;
			shieldUpgradeImprobability = 85;
			rocketUpgradeImprobability = 55;

			// Initial speed of enemies
			EnemyShip.moveSpeed = height / 320;

			// reset defeated enemies
			EnemyShip.destroyedObjects = 0;

			// set score factor for enemyShips
			EnemyShip.scoreMod = 10;

			// set scoreMod and MoveSpeed for mid-tier ships
			MidShip.initialize((short) 75, height / 200, (short) 30, (short) 90);

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

			for (BossShip bossShips : bossShip) {
				bossShips.moveSpeed = width / 50;
			}

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
			for (EnemyShip enemyShips : enemyShip) {
				if (!enemyShips.destroyed) {
					// check for collision
					if (ship.collidesWith(enemyShips)) {
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
						enemyShips.destroyed = true;
					}
				}
			}
			for (BossShip bossShips : bossShip) {
				if (!bossShips.destroyed) {
					// check for collision with shots
					for (BossShotObject bossShots : bossShips.shot) {
						ship.systemDestroyOnCollision(bossShots);
						if (ship.collidesWith(bossShots)) {
							// kill user ship
							if (ship.hasExtraLife()) {
								// subtract the extra life and vibrate
								ship.subExtraLife();
							} else {
								// destroy ship and vibrate
								ship.systemDestroy();
							}
							v1.vibrate(500);

							// kill shot
							bossShots.y = height;
						}
					}
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
				for (EnemyShip enemyShips : enemyShip) {
					if (!enemyShips.destroyed
							&& ship.rocket.collidesWith(enemyShips)) {
						enemyShips.destroy();
					}
				}

				for (MidShip midShips : midShip) {
					if (!midShips.isDestroyed()
							&& ship.rocket.collidesWith(midShips)) {
						midShips.destroy();
					}
				}

				for (BossShip bossShips : bossShip) {
					if (!bossShips.destroyed
							&& ship.rocket.collidesWith(bossShips)) {
						// apply damage to ship
						bossShips.hit();
						// might spawn superupgrade if destroyed after damage is
						// applied

						spawnSuperUpgrade(bossShips.destroyed, bossShips.x,
								bossShips.y);
					}
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
				for (EnemyShip enemyShips : enemyShip) {
					if (!enemyShips.destroyed
							&& ship.rocket.collidesWith(enemyShips)) {
						// apply damage to ship
						enemyShips.destroy();

						ship.rocket.explode();
					}
				}

				for (MidShip midShips : midShip) {
					if (!midShips.isDestroyed()
							&& ship.rocket.collidesWith(midShips)) {
						midShips.destroy();

						ship.rocket.explode();
					}
				}

				for (BossShip bossShips : bossShip) {
					if (!bossShips.destroyed
							&& ship.rocket.collidesWith(bossShips)) {
						// apply damage to ship
						bossShips.hit(10);

						ship.rocket.explode();
						// might spawn superupgrade if destroyed after damage is
						// applied
						spawnSuperUpgrade(bossShips.destroyed, bossShips.x,
								bossShips.y);

					}
				}
			}
		}
	}

	private void releaseBomb() {
		if (ship.hasBomb()) {
			ship.bomb.setNotDestroyed();
			ship.subBomb();
		}

	}

	private void drawBomb(Canvas c) {
		if (!ship.bomb.isDestroyed()) {
			c.drawLine(0, ship.bomb.getyExplosion(), width,
					ship.bomb.getyExplosion(), hudPaint);

			// move the line and destroy when the line goes out of bounds
			ship.bomb.explode();

			// check for collision
			for (EnemyShip enemyShips : enemyShip) {
				if (!enemyShips.destroyed
						&& ship.bomb.getyExplosion() <= enemyShips.y
								+ enemyShips.height) {
					enemyShips.destroy();
				}
			}

			for (MidShip midShips : midShip) {
				if (!midShips.isDestroyed()
						&& ship.bomb.getyExplosion() <= midShips.y
								+ midShips.height) {
					midShips.superDestroy();
				}
			}

			for (BossShip bossShips : bossShip) {
				if (!bossShips.destroyed
						&& ship.bomb.getyExplosion() <= bossShips.y
								+ bossShips.height) {
					bossShips.hit(10);
				}

				for (var shot : bossShips.shot) {
					if (ship.bomb.getyExplosion() <= shot.y + shot.height) {
						//reset shot
						shot.y = height;
					}
				}
			}
		}
	}

	/**
	 * will get an x value to help spawn new enemies up to a max of 12
	 * 
	 * @param i
	 *            which enemy should spawn
	 * @return a value that is between 1/6th of the screen width and the screen
	 *         width
	 */
	private float spawnHelp(byte i) {
		// returns number in one of 6 lines
		if (i < 6) {
			return randFloat((i * (width / 6)),
					i * (width / 6) + enemyShip.get(i).width / 2);
		}
		return randFloat(((i - 6) * (width / 6)), (i - 6) * (width / 6)
				+ enemyShip.get(i).width / 2);
	}

	private void enemyShipLogic(Canvas c) {

		spawnEnemyShip();

		// enemy logic
		for (EnemyShip enemyShips : enemyShip) {

			if (!enemyShips.destroyed) {

				// draw enemy ship
				c.drawBitmap(EnemyShip.sprite, enemyShips.x, enemyShips.y, null);

				// enemy is above the bottom
				if (enemyShips.y + EnemyShip.moveSpeed < height) {
					// enemy moves to the bottom
					enemyShips.y += EnemyShip.moveSpeed;
				} else {
					// destroy enemy when it goes out of the screen.
					enemyShips.systemDestroy();
				}

				// check for collision
				for (SimpleGameObject shots : ship.shot) {

					if (enemyShips.collidesWith(shots)) {

						// if upgradeImprobability is met
						if (random.nextInt(shotUpgradeImprobability) == 0) {
							spawnUpgrade(enemyShips.x, enemyShips.y,
									UpgradeObject.SHOT);
						} else if (random.nextInt(rocketUpgradeImprobability) == 0) {
							spawnUpgrade(enemyShips.x, enemyShips.y,
									UpgradeObject.ROCKET);
						} else if (random.nextInt(shieldUpgradeImprobability) == 0) {
							spawnUpgrade(enemyShips.x, enemyShips.y,
									UpgradeObject.SHIELD);
						}

						// destroys the ship
						enemyShips.destroy();

						// destroy the shot
						shots.y = -1;
					}
				}

			}

		}

	}

	private void spawnEnemyShip() {
		// every spawnTime frames
		if (frameCounter % spawnTime == 0) {
			// for every enemyShip
			for (byte i = 0; i < enemyShip.size(); i++) {
				// only spawns new ship if the random number between 0 and
				// spawnImprobability is 0.
				if (enemyShip.get(i).destroyed
						&& random.nextInt(spawnImprobability) == 0) {

					// spawns new enemyShip
					enemyShip.get(i).destroyed = false;
					enemyShip.get(i).x = spawnHelp(i);
					enemyShip.get(i).y = 0 - enemyShip.get(i).height;

				}
			}
		}
	}

	/**
	 * spawns a new upgrade at the coordinates where the enemyShip was destroyed
	 * 
	 * @param c
	 *            the Canvas
	 * @param x
	 *            the x value of the destroyed enemyShip
	 * @param y
	 *            the y value of the destroyed enemyShip
	 */
	private void spawnUpgrade(float x, float y, byte type) {

		switch (type) {

		case UpgradeObject.SHOT: // spawn a new upgrade.
			for (ShotUpgrade upgrades : shotUpgrade) {
				if (upgrades.destroyed) {
					upgrades.x = x;
					upgrades.y = y;
					upgrades.destroyed = false;

					// breaks the loop to only spawn one upgrade
					break;
				}
			}

			break;

		case UpgradeObject.SHIELD:
			for (ShieldUpgrade upgrades : shieldUpgrade) {
				if (upgrades.destroyed) {
					upgrades.x = x;
					upgrades.y = y;
					upgrades.destroyed = false;

					// breaks the loop to only spawn one upgrade
					break;
				}
			}

			break;
		case UpgradeObject.ROCKET:
			for (RocketUpgrade upgrades : rocketUpgrade) {
				if (upgrades.destroyed) {
					upgrades.x = x;
					upgrades.y = y;
					upgrades.destroyed = false;
					// breaks the loop to only spawn one upgrade
					break;
				}
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

	private void midShipLogic(Canvas c) {
		spawnMidShip();

		drawMidShip(c);
	}

	private void spawnMidShip() {

		if (frameCounter % MidShip.getSpawnTime() == 0) {
			for (MidShip midShips : midShip) {
				if (midShips.isDestroyed()
						&& random.nextInt(MidShip.getSpawnImprobability()) == 0) {
					// 50-50 chance
					if (random.nextBoolean()) {
						midShips.x = width + midShips.width;
					} else {
						midShips.x = -midShips.width;
					}
					midShips.y = -midShips.height;
					midShips.create();
				}
			}
		}

	}

	private void drawMidShip(Canvas c) {
		// go through array
		for (MidShip midShips : midShip) {
			// check if alive
			if (!midShips.isDestroyed()) {

				// draw shield
				if (!midShips.shield.destroyed) {
					// update shield coords
					midShips.shield.x = midShips.x - width / 40;
					midShips.shield.y = midShips.y - height / 42;
					// draw shield
					c.drawBitmap(ShieldObject.sprite, midShips.shield.x,
							midShips.shield.y, null);
				}

				// draw
				c.drawBitmap(MidShip.getSprite(), midShips.x, midShips.y, null);

				// move down
				midShips.y += MidShip.getMoveSpeed();

				// destroy when out of screen
				if (midShips.y >= height) {
					midShips.systemDestroy();
				}

				// move left&right
				if (midShips.getDirectionRight()) {
					midShips.x += MidShip.getMoveSpeed();
				} else {
					midShips.x -= MidShip.getMoveSpeed();
				}

				// change direction
				if (midShips.getDirectionRight()
						&& midShips.x + midShips.width >= width) {
					midShips.changeDirection();
				}
				if (midShips.getDirectionLeft() && midShips.x <= 0) {
					midShips.changeDirection();
				}

				// collision detection
				for (SimpleGameObject shots : ship.shot) {
					if (midShips.collidesWith(shots)) {
						// kill midShip
						midShips.destroy();

						if (midShips.isDestroyed()) {

							// if upgradeImprobability is met
							if (random.nextInt(shotUpgradeImprobability) == 0) {
								spawnUpgrade(midShips.x, midShips.y,
										UpgradeObject.SHOT);
							} else if (random
									.nextInt(rocketUpgradeImprobability) == 0) {
								spawnUpgrade(midShips.x, midShips.y,
										UpgradeObject.ROCKET);
							} else if (random
									.nextInt(shieldUpgradeImprobability) == 0) {
								spawnUpgrade(midShips.x, midShips.y,
										UpgradeObject.SHIELD);
							}
						}
						// kill shot
						shots.y = -1;
					}
				}
				if (midShips.collidesWith(ship)) {
					// kill user ship
					if (ship.hasExtraLife()) {
						// subtract the extra life and vibrate
						ship.subExtraLife();
					} else {
						// destroy ship and vibrate
						ship.systemDestroy();
					}
					v1.vibrate(500);

					// kill midSHip
					midShips.systemDestroy();
				}

			}
		}
	}

	private void bossLogic(Canvas c) {

		// spawn new boss
		bossSpawn();
		// boss Logic
		for (BossShip bossShips : bossShip) {
			// movement + draw + collision
			if (!bossShips.destroyed) {

				// Draw
				c.drawText("" + bossShips.getHealth(), bossShips.x,
						bossShips.y, hudPaint);
				c.drawBitmap(BossShip.bossSprite, bossShips.x, bossShips.y,
						null);

				if (bossShips.onScreen) {
					// ship about to go out of the right side of screen
					if (bossShips.x + bossShips.moveSpeed + bossShips.width >= width) {
						// reverse movement
						bossShips.moveSpeed *= -1;
					}
					// boss about to go out of the left side of screen.
					if (bossShips.x + bossShips.moveSpeed <= 0) {
						// reverse movement
						bossShips.moveSpeed *= -1;
					}
				} else {
					// boss is out of the left side of the screen
					if (bossShips.x < 0) {
						bossShips.moveSpeed = width / 50;
					} else if (bossShips.x > width) {
						bossShips.moveSpeed = -width / 50;
					}

					if (bossShips.x > 0
							&& bossShips.x + bossShips.width < width) {
						bossShips.onScreen = true;
					}

				}
				// move boss
				bossShips.x += bossShips.moveSpeed;
				bossShips.shootMechanics(c, height);

				// reduces health upon shooting
				for (ShotObject shots : ship.shot) {
					if (bossShips.collidesWith(shots)) {
						// apply damage to boss
						bossShips.hit();

						// destroy the shot
						shots.y = -1;

						// might spawn superupgrade if destroyed after damage is
						// applied
						spawnSuperUpgrade(bossShips.destroyed, bossShips.x,
								bossShips.y);

					}
				}
			}
		}
	}

	private void bossSpawn() {
		if (frameCounter % BossShip.spawnTime == 0) {
			for (byte i = 0; i < bossShip.length; i++) {
				// only spawns new ship if the random number between 0 and
				// spawnImprobability is 0.
				if (bossShip[i].destroyed
						&& random.nextInt(BossShip.spawnImprobability) == 0) {

					// spawns new boss
					sounds.play(bossSpawnfx, 1, 1, 1, 0, 1);

					bossShip[i].onScreen = false;
					bossShip[i].setHealth(20);
					bossShip[i].y = bossSpawnHelp(i);
					bossShip[i].x = random.nextInt(1) * width
							- bossShip[i].width;
					bossShip[i].destroyed = false;

					bossShip[i].resetShots(height);
				}
			}
		}
	}

	private float bossSpawnHelp(byte i) {
		return 25.0f + 1.05f * (bossShip[i].height * i);
	}

	/**
	 * Might spawn a super upgrade if the ship was destroyed
	 * 
	 * @param destroyed
	 * @param y
	 * @param x
	 */
	private void spawnSuperUpgrade(boolean destroyed, float x, float y) {

		if (destroyed && BossShip.spawnUpgrade(random)) {
			spawnUpgrade(x, y, UpgradeObject.BOMB);
		} else if (destroyed && BossShip.spawnUpgrade(random)) {
			spawnUpgrade(x, y, UpgradeObject.LIFE);
		}

	}

	private void upgradeLogic(Canvas c) {
		for (ShotUpgrade upgrades : shotUpgrade) {

			if (!upgrades.destroyed) {

				// draw upgrade
				c.drawBitmap(ShotUpgrade.sprite, upgrades.x, upgrades.y, null);

				// upgrade is above the bottom
				if (upgrades.y + UpgradeObject.moveSpeed < height) {
					// upgrade moves to the bottom
					upgrades.y += UpgradeObject.moveSpeed;
				} else {
					// destroy upgrade when it goes out of the screen.
					upgrades.systemDestroy();
				}

				// check for collision
				if (upgrades.collidesWith(ship)) {

					sounds.play(upgradefx, 1, 1, 1, 0, 1);
					upgradeShotTime();
					upgrades.systemDestroy();
				}

			}

		}
		if (shield.destroyed) {
			for (ShieldUpgrade upgrades : shieldUpgrade) {

				if (!upgrades.destroyed) {

					// draw upgrade
					c.drawBitmap(ShieldUpgrade.sprite, upgrades.x, upgrades.y,
							null);

					// upgrade is above the bottom
					if (upgrades.y + UpgradeObject.moveSpeed < height) {
						// upgrade moves to the bottom
						upgrades.y += UpgradeObject.moveSpeed;
					} else {
						// destroy upgrade when it goes out of the screen.
						upgrades.systemDestroy();
					}

					// check for collision
					if (upgrades.collidesWith(ship)) {

						sounds.play(upgradefx, 1, 1, 1, 0, 1);
						upgradeShield(6000);
						upgrades.systemDestroy();
					}

				}

			}
		}

		for (RocketUpgrade upgrades : rocketUpgrade) {

			if (!upgrades.destroyed) {

				// draw upgrade
				c.drawBitmap(RocketUpgrade.sprite, upgrades.x, upgrades.y, null);

				// upgrade is above the bottom
				if (upgrades.y + UpgradeObject.moveSpeed < height) {
					// upgrade moves to the bottom
					upgrades.y += UpgradeObject.moveSpeed;
				} else {
					// destroy upgrade when it goes out of the screen.
					upgrades.systemDestroy();
				}

				// check for collision
				if (upgrades.collidesWith(ship)) {

					sounds.play(upgradefx, 1, 1, 1, 0, 1);
					upgradeRockets();
					upgrades.systemDestroy();
				}

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

	/**
	 * helper method to get random floats in range.
	 * 
	 * @return returns a random float between min and max.
	 */
	private float randFloat(float min, float max) {
		// new random number between min and max
		float randomNum = random.nextFloat() * (max - min) + min;

		return randomNum;

	}

	/**
	 * Updates the difficulty based on score !!spawnTime can't be smaller than
	 * 1!!! !!spawnImprob can't be smaller than 1!!
	 */
	private void difficultyChanges() {

		// go through the difficulty array
		for (byte i = 0; i < difficulty.length; i++) {
			// updates difficulty every 250 points
			if (score > (250 + i * 250) && !difficulty[i]) {
				spawnImprobability--;
				if (spawnTime > 1)
					spawnTime--;
				EnemyShip.moveSpeed *= 1.116;
				MidShip.setMoveSpeed(MidShip.getMoveSpeed() * 1.1f);
				BossShip.spawnImprobability -= 3;
				BossShip.spawnTime -= 4;
				BossShip.shotTime -= 1;
				shotUpgradeImprobability += 2;
				shieldUpgradeImprobability += 2;
				rocketUpgradeImprobability += 2;
				difficulty[i] = true;
			}
		}
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

	private void displayRocketHud(Canvas c) {
		// display available Rockets
		for (byte i = 0; i < ship.getAvailableRockets(); i++) {
			c.drawBitmap(rocketHUD, c.getWidth() - (1 + i) * c.getWidth() / 17f, bombHUD.getHeight() + c.getWidth() / 16f, null);
		}
	}

	private void displayBombHud(Canvas c) {
		// display available Bomb
		if (ship.hasBomb()) {
			c.drawBitmap(bombHUD, c.getWidth() - bombHUD.getWidth() - c.getWidth() / 17f, c.getWidth() / 20f, null);
		}
	}

	private void displayLifeHud(Canvas c) {

		// display empty hud
		c.drawBitmap(lifeHUDempty, 135, 135, null);

		// display available extra life
		if (ship.hasExtraLife()) {
			c.drawBitmap(lifeHUD, 140, 140, null);
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
