package com.LTJ.lukasgamecollection;

import android.os.Bundle;
import android.view.WindowManager;
import android.app.Activity;

public class Tutorial2 extends Activity {
	
	TutorialSurface tut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		tut = new TutorialSurface(this);
		tut.setOnTouchListener(tut);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(tut);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		tut.pause();
		

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		tut.resume();
	
	}



}
