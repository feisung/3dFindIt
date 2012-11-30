package com.threedindustries.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.SearchView;
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
    
   
    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
          Log.i(TAG, "The query is: " );
        //TO-DO Add handler
        return true;
    }	
    
	public boolean onOptionsItemSelected(MenuItem item){
		
    	//Handling the different menu items
		switch (item.getItemId()){
		case R.id.menu_search:
			onSearchRequested();
			Log.i(TAG, "About to Search");
//			//go to Search Activity
//            intent = new Intent(this, SearchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
	    	return true;
		case R.id.menu_upload:
			//go to Search Activity
            intent = new Intent(this, IndustryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
	    	return true;		case android.R.id.home:
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
