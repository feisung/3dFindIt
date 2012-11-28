package com.threedindustries.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	Intent intent;
	private static final String TAG = "3DI Home Activity";
	static String ServerAddress = "http://www.3dfabsource.com:12002/search/index/format/json";
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate 3DI App");
        setContentView(R.layout.home_activity);
   }
    
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity, menu);
        return true;
    }
    
	public boolean onOptionsItemSelected(MenuItem item){
		
    	//Handling the different menu items
		switch (item.getItemId()){
		case R.id.menu_search:
			//go to Search Activity
            intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
	    	return true;
		case android.R.id.home:
			//go back to previous activity
			intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
			return true;
		//other case statements
	    default:
	    	return super.onOptionsItemSelected(item);    	

		}
    }
	
	//Handles On Click for 3DPartsource Logo
    public void n3dps(View view){
        intent = new Intent(this, IndustryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    
	//Handles On Click for 3DArchiSource Logo
    public void archisource(View view){
    	
    }

	//Handles On Click for 3DMechasource Logo
    public void mechasource(View view){
    	
    }
    
	//Handles On Click for 3DSciSource Logo
    public void scisource(View view){
    	
    }
    
	//Handles On Click for 3GameSource Logo
    public void Gamesource(View view){
    	
    }
    
	//Handles On Click for 3GameSource Logo
    public void robosource(View view){
    	
    }
    
}
