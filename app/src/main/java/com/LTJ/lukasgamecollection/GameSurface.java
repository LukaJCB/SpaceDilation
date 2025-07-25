package com.LTJ.lukasgamecollection;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * An abstract class to design different SurfaceViews for Games
 * 
 * @author Luka
 * 
 */
public abstract class GameSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	private MainThread t;
	private SurfaceHolder holder;
	private final long fps;

	/**
	 * Instantiates the Object and gets the SurfaceHolder
	 * 
	 * @param context
	 */
	public GameSurface(Context context, long fps) {
		super(context);
		this.fps = fps;
		holder = getHolder();
		holder.addCallback(this);
		

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		t = new MainThread(this.holder, this,fps);
		t.setRunning(true);
		t.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (t != null) {

			while (true) {
				try {
					t.setRunning(false);
					// wait for thread to finish
					t.join();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

	public void pause() {
		t.setRunning(false);
		while (true) {
			try {
				t.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Implement your Draw actions here
	 */
	public abstract void render(Canvas c);



	public abstract void initializeCanvas(Canvas c);

}
