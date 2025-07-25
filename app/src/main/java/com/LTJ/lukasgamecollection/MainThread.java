package com.LTJ.lukasgamecollection;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	private final long FPS;
	private final long FRAME_PERIOD;
	private SurfaceHolder surfaceHolder;
	private GameSurface gamePanel;
	private boolean running;
	private long beginTime;
	private long timeDiff;
	private int sleepTime;
	
	public MainThread(SurfaceHolder surfaceHolder, GameSurface gamePanel, long fps) {
		super();
		this.FPS = fps;
		this.FRAME_PERIOD= 1000 / FPS;
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
	public void setRunning(boolean running) {
			this.running = running;
	}
	
	@Override
	public void run(){
		Canvas c;
		while (running){
			c= null;
			try {
			
				//get canvas
				c = surfaceHolder.lockCanvas();
				
				synchronized (surfaceHolder) {
					//measure time
					beginTime = System.currentTimeMillis();
					
					gamePanel.initializeCanvas(c);
					
					//draw on canvas
					gamePanel.render(c);
					//measure time difference
					timeDiff = System.currentTimeMillis() - beginTime;
					//Log.i("fPS", "" + 1000/timeDiff);
					
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					//Log.d("sleepTime","" +sleepTime);
					//sleep if rendered on time.
					if (sleepTime > 0){
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}
				}
			} finally{
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
