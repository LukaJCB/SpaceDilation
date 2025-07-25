package com.LTJ.lukasgamecollection;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class Tutorial extends Activity implements OnClickListener {
	
	private Button forward;
	private Button back;
	private int currentPage,lastPage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//current tutorial page
		currentPage = R.layout.activity_tutorial;
		
		//last tutorial page
		lastPage = R.layout.activity_tutorial2;
		
		displayNewPage();
		
	}

	@Override
	public void onClick(View v) {
		
		if (v == findViewById(R.id.backButton)){
			
			this.onBackPressed(); 
			
		} else if (v == findViewById(R.id.forwardButton)){
			
			this.onForwardPressed();
			
	
		}
	}
	@Override
	public void onBackPressed() {

		//check if it's still in the tutorial range
		if (currentPage > R.layout.activity_tutorial){
			
			//go back
			currentPage--;
			//display page
			displayNewPage();
			
		} else {
			//use the default method for pressing back
			super.onBackPressed();
		}
		

		
	}

	
	private void onForwardPressed(){
		

		//check if it's still in the tutorial range
		if (currentPage < lastPage){
			
			//go forward
			currentPage++;
			
			//display
			displayNewPage();
		} else {
			//revert user back to Main menu
			Intent i = new Intent(this,Mainmenu.class);
			startActivity(i);
		}
		

	}
	
	
	/**
	 * displays the content of the currentpage
	 * Only works for activity_tutorial-pages
	 */
	private void displayNewPage(){
		//show new content
		setContentView(currentPage);
		
		//set up new buttons
		forward = (Button)findViewById(R.id.forwardButton);
		back = (Button)findViewById(R.id.backButton);
		
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
	}



}
