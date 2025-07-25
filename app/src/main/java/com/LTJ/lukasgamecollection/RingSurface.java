//package com.LTJ.lukasgamecollection;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.media.MediaPlayer;
//import android.view.MotionEvent;
//
//import android.view.View;
//
//
//public class RingSurface extends GameSurface  {
//
//
//	
//	private SimpleGameObject ball = new SimpleGameObject();
//	private SimpleGameObject leftGui = new SimpleGameObject();
//	private SimpleGameObject rightGui = new SimpleGameObject();
//	
//	private Bitmap ring;
//	private float xspeed, yspeed, yacc, xringl, yring, xringr,
//			xringspeed, height, width;
//	private boolean ringDirection, instantiate;
//	
//	final boolean DIRECTION_RIGHT = false;
//	final boolean DIRECTION_LEFT = true;
//	private MediaPlayer mediaPlayer;
//	private MediaPlayer sfx;
//	private MediaPlayer sfx2;
//
//	
//
//	public RingSurface(Context context) {
//		super(context);
//
//		//add bitmaps
//		ball.sprite = BitmapFactory.decodeResource(getResources(), R.drawable.jump);
//		ring = BitmapFactory.decodeResource(getResources(), R.drawable.ring);
//		rightGui.sprite = BitmapFactory.decodeResource(getResources(), R.drawable.right);
//		leftGui.sprite = BitmapFactory.decodeResource(getResources(), R.drawable.left);
//		
//		ball.setDimensions();
//		rightGui.setDimensions();
//		leftGui.setDimensions();
//
//	
//		
//		xspeed = xringl = 0;
//		yspeed = 10;
//		yacc = 1.5f;
//		xringspeed = 4;
//		ringDirection = false;
//		instantiate = false;
//		
//		mediaPlayer = MediaPlayer.create(context, R.raw.backgroundmusic);
//		mediaPlayer.setLooping(true);
//		mediaPlayer.start();
//		
//		sfx = MediaPlayer.create(context, R.raw.sfx);
//		sfx2 = MediaPlayer.create(context, R.raw.sfxjump);
//		sfx2.setVolume(0.5f, 0.5f);
//
//
//	}
//
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//		super.pause();
//		mediaPlayer.pause();
//	}
//
//
//	@Override
//	public void resume() {
//		// TODO Auto-generated method stub
//		super.resume();
//		mediaPlayer.start();
//	}
//
//
//	@Override
//	public void onDraw(Canvas cvn) {
//		try {
//			Thread.sleep(8);
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
//
//		
//		//draws the background
//		cvn.drawRGB( 140, 180, 95);
//		if (instantiate == false) {
//			
//			//instantiates the variables
//			height = cvn.getHeight();
//			width = cvn.getWidth();
//			yspeed = height / 105.4f;
//			yacc = height / 700;
//			xringspeed = width / 120;
//			
//			leftGui.y = height * 4 / 5;
//			leftGui.x = 0;
//			
//			rightGui.y = leftGui.y;
//			rightGui.x = width - rightGui.width;
//
//			yring = height / 2;
//			
//			// only run once
//			instantiate = true;
//
//		}
//		
//		//update the ring coordinates
//		xringr = xringl + ring.getWidth();
//		
//
//		
//		
//		//draws the Bitmaps
//		cvn.drawBitmap(ring, xringl, yring, null);
//		cvn.drawBitmap(ball.sprite, ball.x, ball.y, null);
//		cvn.drawBitmap(leftGui.sprite, leftGui.x, leftGui.y, null);
//		cvn.drawBitmap(rightGui.sprite, rightGui.x, rightGui.y, null);
//		
//		
//		//ball goes against left side of ring
//		if ((ball.x + ball.width) > xringl - xspeed) {
//			if (ball.x < xringl) {
//				if ((ball.y + ball.height) > (yring - yspeed)) {
//					if (ball.y < yring) {
//						yspeed = 0;
//						xspeed = -(width / 48);
//					}
//				}
//			}
//		}
//		
//		//ball goes against right side of ring
//		if ((ball.x + ball.width) > xringr - xspeed) {
//
//			if (ball.x < xringr) {
//				if ((ball.y + ball.height) > (yring - yspeed)) {
//					if (ball.y < yring) {
//						yspeed = 0;
//						xspeed = width / 48;
//					}
//				}
//			}
//		}
//		//ball goes through ring
//		if ((ball.x + ball.width) < xringr - xspeed) {
//
//			if (ball.x > xringl) {
//				if ((ball.y + ball.height) > (yring - yspeed)) {
//					if (ball.y < yring) {
//						
//						//blink
//						cvn.drawARGB(255, 255, 255, 255);
//						xringspeed += width / 1000;
//						sfx.start();
//					}
//				}
//			}
//		}
//		
//		//ball about to go outside right side of screen
//		if (ball.x > width - ball.width - xspeed) {
//			xspeed = 0;
//
//		}
//		//ball about to go outside of bottom side of screen
//		if (ball.y > height - ball.height - yspeed) {
//
//			yspeed = -(height / 21.35f);
//			sfx2.start();
//
//		}
//		
//		
//		//ball about to go outside of left side of screen
//		if (ball.x + xspeed < 0) {
//
//			xspeed = 0;
//
//		}
//		
//		
//		//yspeed to small
//		if (yspeed < height / 21.35f) {
//			yspeed += yacc;
//		}
//		
//		
//		if (ringDirection == DIRECTION_RIGHT) {
//			xringl += xringspeed;
//
//		}
//		
//		if (ringDirection == DIRECTION_LEFT) {
//			
//			xringl -= xringspeed;
//		}
//		
//		//ring touches right border of screen
//		if (xringl > width - ring.getWidth() - xringspeed) {
//			xringl -= xringspeed;
//			ringDirection = DIRECTION_LEFT;
//		}
//
//		//ring touches left border of screen
//		if (xringl + xringspeed < 0) {
//			
//			xringl += xringspeed;
//			ringDirection = DIRECTION_RIGHT;
//		}
//
//		ball.x += xspeed;
//		ball.y += yspeed;
//
//
//	}
//
//
//	@Override
//	public boolean onTouch(View view1, MotionEvent me) {
//
//	
//
//		switch (me.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			if (me.getX() < leftGui.width) {
//				if (me.getY() > leftGui.y) {
//					if (me.getY() < leftGui.y + leftGui.height) {
//						xspeed = -(width/ 72) ;
//					}
//
//				}
//			}
//			if (me.getX() > rightGui.x) {
//				if (me.getY() > rightGui.y) {
//					if (me.getY() < rightGui.y + leftGui.height) {
//						xspeed = width/72;
//					}
//
//				}
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//			xspeed = 0;
//			break;
//
//		}
//
//		return true;
//
//	}
//
//}