package com.LTJ.lukasgamecollection;

import android.os.Bundle;
import android.view.WindowManager;
import android.app.Activity;


public class SpaceGame extends Activity {

	private SpaceSurface mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//get current highscore
		
		mainView = new SpaceSurface(this);
		mainView.setOnTouchListener(mainView);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(mainView);
		
		

	}

	@Override
	protected void onPause() {
		
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mainView.pause();
		

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mainView.resume();
	
	}

}
