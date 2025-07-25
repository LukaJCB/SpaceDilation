package com.LTJ.lukasgamecollection;

import com.LTJ.lukasgamecollection.R;
import com.LTJ.lukasgamecollection.R.drawable;

import com.LTJ.lukasgamecollection.R.raw;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Racinggame extends Activity {

	RacingSurface view1;

	MediaPlayer mediaPlayer, sfx, sfx2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view1 = new RacingSurface(this);
		setContentView(view1);
		view1.setOnTouchListener(view1);

		mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
		mediaPlayer.setLooping(true);
		// mediaPlayer.start();
		sfx = MediaPlayer.create(this, R.raw.sfx);
		sfx2 = MediaPlayer.create(this, R.raw.sfxjump);
		sfx2.setVolume(0.5f, 0.5f);

	}

	@Override
	protected void onPause() {

		super.onPause();
		view1.pause();
		// mediaPlayer.stop();

	}

	@Override
	protected void onResume() {

		super.onResume();
		

	}
}
