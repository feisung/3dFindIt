package com.threedindustries.mobile;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class IndustryActivity extends HomeActivity{
	
	private static final String TAG = "Industry Activity";
	private ImageAdapter adapter;
    @TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.industry_activity);
        //Enable navigating up
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		
        GridView gridview = (GridView) findViewById(R.id.gridview);
        adapter = new ImageAdapter(this, data);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				openPage((String) adapter.getItem(position));
				//Toast.makeText(HomeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
				
			}
        });
	}
    
    public void openPage(String url) {
		// create an intent
		Intent data = new Intent();
		
		// specify the intent's action and url
		data.setAction(Intent.ACTION_VIEW);
		data.setData(Uri.parse(url));
		
    	try {
    		startActivity(data); 
    	} catch (ActivityNotFoundException e) {
    		Log.e("Threads03", "Cannot find an activity to start URL " + url);
    	}

	}
    
    public void Search (View view){
    	//Comment
		startActivity(new Intent(IndustryActivity.this, SearchActivity.class));
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
