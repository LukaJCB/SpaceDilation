package com.LTJ.lukasgamecollection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreBoard extends Activity implements OnClickListener {
	
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	
	TextView scoreView;
	TextView[] topScore;
	TextView[] topName;
	Button submitScore, mainMenu, newGame;
	EditText scoreName;
	int yourScore;
	int[] iTopScore;
	String[] sTopName;
	MediaPlayer sfx;
	
	String yourName;
	boolean checkScores; 
	
	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoreboard);
		
		//get sharedPreferences
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		
		sfx = MediaPlayer.create(this, R.raw.selection);
		
		
		//set up arrays
		iTopScore = new int[5];
		sTopName = new String[5];
		
		topScore = new TextView[5];
		topName = new TextView[5];
		
		//get intent extras
		Bundle extras = getIntent().getExtras();

		//get score refresh
		checkScores = extras.getBoolean("checkScores",false);
		
		
		//set Textviews
		topName[0] = (TextView)findViewById(R.id.top1);
		topName[1] = (TextView)findViewById(R.id.top2);
		topName[2] = (TextView)findViewById(R.id.top3);
		topName[3] = (TextView)findViewById(R.id.top4);
		topName[4] = (TextView)findViewById(R.id.top5);
		
		topName[0].setText(sharedPref.getString("top1", "Top 1"));
		topName[1].setText(sharedPref.getString("top2", "Top 2"));
		topName[2].setText(sharedPref.getString("top3", "Top 3"));
		topName[3].setText(sharedPref.getString("top4", "Top 4"));
		topName[4].setText(sharedPref.getString("top5", "Top 5"));
		
		topScore[0] = (TextView)findViewById(R.id.top1score);
		topScore[1] = (TextView)findViewById(R.id.top2score);
		topScore[2] = (TextView)findViewById(R.id.top3score);
		topScore[3] = (TextView)findViewById(R.id.top4score);
		topScore[4] = (TextView)findViewById(R.id.top5score);
		
		topScore[0].setText(sharedPref.getInt("top1score", 0) + "");
		topScore[1].setText(sharedPref.getInt("top2score", 0) + "");
		topScore[2].setText(sharedPref.getInt("top3score", 0) + "");
		topScore[3].setText(sharedPref.getInt("top4score", 0) + "");
		topScore[4].setText(sharedPref.getInt("top5score", 0) + "");
		
		//set up name values
		for (byte i = 0;i < topName.length;i++){
			sTopName[i] = topName[i].getText().toString();
		}
		//set up score values
		for (byte i = 0;i < topScore.length;i++){
			iTopScore[i] = Integer.parseInt(topScore[i].getText().toString());
		}
		
		//set up score view
		scoreView = (TextView)findViewById(R.id.scoreView);
		
		//set buttons
		submitScore = (Button)findViewById(R.id.submitScore);
		submitScore.setOnClickListener(this);
		mainMenu = (Button)findViewById(R.id.menubutton);
		mainMenu.setOnClickListener(this);
		newGame = (Button)findViewById(R.id.newgamebutton);
		newGame.setOnClickListener(this);
		
		//set editText
		scoreName = (EditText)findViewById(R.id.scoreName);
		
		//get the score
		yourScore = extras.getInt("scoreKey");
		
		//set the score
		scoreView.setText("Your Score: " +yourScore);
		
		//if you only want to check scores
		if (checkScores){
			scoreView.setVisibility(View.INVISIBLE);
		}
		//upload name if the score is a highscore
		if (isHighScore(yourScore)){
			scoreName.setVisibility(View.VISIBLE);
			submitScore.setVisibility(View.VISIBLE);
			
		}
	}

	@Override
	public void onBackPressed() {
		
		//send user back to main menu instead of the game
		Intent intent = new Intent(this,Mainmenu.class);
		startActivity(intent);
	
	}

	@Override
	public void onClick(View v) {
		
		sfx.start();
		
		if (v == submitScore){
			// get the name  
			yourName = scoreName.getText().toString();
			//set the new highscore
			setNewScore(yourScore,yourName);
			editor.putString("top1", sTopName[0]);
			editor.putString("top2", sTopName[1]);
			editor.putString("top3", sTopName[2]);
			editor.putString("top4", sTopName[3]);
			editor.putString("top5", sTopName[4]);
			
			editor.putInt("top1score", iTopScore[0]);
			editor.putInt("top2score", iTopScore[1]);
			editor.putInt("top3score", iTopScore[2]);
			editor.putInt("top4score", iTopScore[3]);
			editor.putInt("top5score", iTopScore[4]);
			
			editor.commit();
			
			//refresh the scoreboard
			Intent i = new Intent(this,ScoreBoard.class);
			i.putExtra("checkScores", true);
			startActivity(i);
		} else if (v == mainMenu){
			Intent i = new Intent(this, Mainmenu.class);
			startActivity(i);
		} else if (v == newGame){
			Intent i = new Intent(this, SpaceGame.class);
			startActivity(i);
		} 
		
	}
	
	
	private void setNewScore(int newScore, String newName){
		for (byte i = 0;i < iTopScore.length; i++){
			if (newScore > iTopScore[i]){
				//create values to complete the recursion
				int oldScore = iTopScore[i];
				String oldName = sTopName[i];
				//enter the new values
				iTopScore[i] = newScore;
				sTopName[i] = newName;
				//call method for the old values
				setNewScore(oldScore, oldName);
				break;
			}
		}
	}
	
	private boolean isHighScore(int newScore){
		for (int topScores : iTopScore){
			if (topScores < newScore){
				return true;
			}
		}
		return false;
	}
	
	

}
