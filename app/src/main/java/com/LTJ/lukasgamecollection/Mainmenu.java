package com.LTJ.lukasgamecollection;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Mainmenu extends Activity implements OnClickListener {

	private Button scores, neu, tutorial;
	MediaPlayer sfx;
	private Intent mainIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainIntent = new Intent();

		setContentView(R.layout.mainmenu);

		sfx = MediaPlayer.create(this, R.raw.selection);

		scores = (Button) findViewById(R.id.scoresbutton);
		neu = (Button) findViewById(R.id.neubutton);
		tutorial = (Button) findViewById(R.id.tutbutton);
		
		neu.setOnClickListener(this);
		scores.setOnClickListener(this);
		tutorial.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		sfx.start();

		if (v == findViewById(R.id.scoresbutton)) {
			mainIntent.setClass(this, ScoreBoard.class);
			mainIntent.putExtra("checkScores", true);
		} else if (v == findViewById(R.id.neubutton)) {
			
			mainIntent.setClass(this, SpaceGame.class);
			
		} else if (v == findViewById(R.id.tutbutton)) {
			
			mainIntent.setClass(this, Tutorial2.class);

		}

		super.startActivity(mainIntent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			String result = data.getStringExtra("result");
			neu.setText(result);
		}
	}

}
