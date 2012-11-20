package com.threedindustries.mobile;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class SearchActivity extends HomeActivity {
    @TargetApi(11)
	@Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        //Enable navigating up
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
}
