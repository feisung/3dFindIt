package com.threedindustries.mobile;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;

public class SplashActivity extends Activity {

    @SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        
        Thread logoTimer = new Thread(){
        	public void run(){
        	try {
        		int logoTimer = 0;
        		while(logoTimer < 2000){
        			sleep(100);
        			logoTimer = logoTimer + 100;
        		}
        		startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        	}
        	catch (InterruptedException e){
        		e.printStackTrace();
        	}
        	finally{
        		finish();
        		}
        	}
        };
        logoTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash, menu);
        return true;
    }
}
