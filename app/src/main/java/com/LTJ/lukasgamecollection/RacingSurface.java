package com.LTJ.lukasgamecollection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.*;
import android.view.View;

public class RacingSurface extends GameSurface implements OnTouchListener {

	Bitmap street, car, right, left, breakb, acc;
	Bitmap scacc, scbrk;
	MediaPlayer mediaPlayer, sfx, sfx2;
	float xspeed, yacc, ycarspeed, yspeed;
	float xstr, ystr, height, width, xcar, ycar;
	int account;
	boolean instantiate;

	public int xguir;
	public int ygui, ygui2;

	public RacingSurface(Context context) {
		super(context,50);

		street = BitmapFactory
				.decodeResource(getResources(), R.drawable.street);
		car = BitmapFactory.decodeResource(getResources(), R.drawable.car);
		right = BitmapFactory.decodeResource(getResources(), R.drawable.right);
		left = BitmapFactory.decodeResource(getResources(), R.drawable.left);
		breakb = BitmapFactory.decodeResource(getResources(),
				R.drawable.breakbutton);
		acc = BitmapFactory.decodeResource(getResources(), R.drawable.acc);
		scacc = Bitmap.createScaledBitmap(acc, 100, 100, false);
		scbrk = Bitmap.createScaledBitmap(breakb, 100, 100, false);

		height = street.getHeight();
		width = street.getWidth();
		xspeed = 0;
		yspeed = 10;
		yacc = 1f;
		ycarspeed = 0;
		instantiate = true;

	}

	public void render(Canvas cvn) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		cvn.drawRGB(190, 58, 209);

		if (instantiate == true) {

			ystr = -height + cvn.getHeight();
			xcar = cvn.getWidth() / 2;
			yspeed = cvn.getHeight() / 800;
			ycar = cvn.getHeight() / 2;
			yacc = 1f;
			instantiate = false;
		}
		ygui = cvn.getHeight() * 4 / 5;
		ygui2 = ygui - scacc.getHeight();
		xguir = cvn.getWidth() - right.getWidth();
		xstr = cvn.getWidth() / 2 - street.getWidth() / 2;
		cvn.drawBitmap(street, xstr, ystr, null);
		cvn.drawBitmap(car, xcar, ycar, null);
		cvn.drawBitmap(left, 0, ygui, null);
		cvn.drawBitmap(right, xguir, ygui, null);
		cvn.drawBitmap(scacc, xguir, ygui2, null);
		cvn.drawBitmap(scbrk, 0, ygui2, null);

		if (ystr > 0) {
			ystr = -height + cvn.getHeight();
		}

		if (xcar + xspeed < 0) {

			xspeed = 0;

		}
		if (ycar + ycarspeed < cvn.getHeight() / 300) {

			ycarspeed = 0;

		}
		if (xcar > cvn.getWidth() - car.getWidth() - xspeed) {
			xspeed = 0;

		}
		if (ycar > cvn.getHeight() - car.getHeight() - ycarspeed) {
			ycarspeed = 0;

		}
		if (yspeed < cvn.getHeight() / 30) {
			if (account < 1) {
				yspeed = yspeed * yacc;
			}
		}
		if (yspeed > cvn.getHeight() / 30) {
			yspeed = cvn.getHeight() / 31;
		}
		if (yspeed < 1.1f) {
			yspeed = 1;
		}

		ystr += yspeed;
		xcar += xspeed;
		ycar += ycarspeed;
		account++;
		if (account > 6) {
			account = 0;
		}

	}

	public boolean onTouch(View view1, MotionEvent me) {
	


		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (me.getX() < left.getWidth()) {
				if (me.getY() > ygui) {
					if (me.getY() < ygui + left.getHeight()) {
						xspeed = -10;
					}

				}
			}
			if (me.getX() > xguir) {
				if (me.getY() > ygui) {
					if (me.getY() < ygui + left.getHeight()) {
						xspeed = 10;
					}

				}
			}
			if (me.getX() < scacc.getWidth()) {
				if (me.getY() > ygui2) {
					if (me.getY() < ygui2 + scacc.getHeight()) {

						yacc = 0.7f;
						ycarspeed = 5;

					}

				}
			}
			if (me.getX() > xguir) {
				if (me.getY() > ygui2) {
					if (me.getY() < ygui2 + scacc.getHeight()) {
						yacc = 1.6f;
						ycarspeed = -5;
					}

				}
			}

			break;
		case MotionEvent.ACTION_UP:
			xspeed = 0;
			ycarspeed = 0;
			yacc = 1f;
			break;

		}

		return true;

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeCanvas(Canvas c) {
		// TODO Auto-generated method stub
		
	}

}
