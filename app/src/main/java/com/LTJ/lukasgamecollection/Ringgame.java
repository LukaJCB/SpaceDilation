//package com.LTJ.lukasgamecollection;
//
//import android.app.Activity;
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//
//public class Ringgame extends Activity {
//
//	RingSurface view1;
//
//	MediaPlayer mediaPlayer, sfx, sfx2;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		view1 = new RingSurface(this);
//		
//		setContentView(view1);
//		view1.setOnTouchListener(view1);
//		
//		mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
//		mediaPlayer.setLooping(true);
//		// mediaPlayer.start();
//		sfx = MediaPlayer.create(this, R.raw.sfx);
//		sfx2 = MediaPlayer.create(this, R.raw.sfxjump);
//		sfx2.setVolume(0.5f, 0.5f);
//
//	}
//
//	@Override
//	protected void onPause() {
//
//		super.onPause();
//		view1.pause();
//		// mediaPlayer.pause();
//	}
//
//	@Override
//	protected void onResume() {
//
//		super.onResume();
//		view1.resume();
//		// mediaPlayer.start();
//	}
//
//}
