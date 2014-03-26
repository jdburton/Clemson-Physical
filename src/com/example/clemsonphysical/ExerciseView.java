package com.example.clemsonphysical;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ExerciseView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_view, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.xml.settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
 
        case R.layout.activity_info_view:
            Intent intent = new Intent(this, InfoView.class);
            startActivity(intent);
            break;
        }
 
        return true;
    }
	
	public void showPractitionerVideo(View view){
		Intent intent = new Intent(this, PractitionerVideoView.class);
		startActivity(intent);
	}
	
	public void showLog(View view){
		Intent intent = new Intent(this, LogView.class);
		startActivity(intent);
	}
	
	public void startRecording(View view){
		
		// ** to be filled in by James
		
	}

}
