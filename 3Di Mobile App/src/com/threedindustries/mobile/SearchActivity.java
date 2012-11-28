package com.threedindustries.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends HomeActivity {
	private static final String TAG = "Search Activity";
	@TargetApi(11)
	@Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        //Enable navigating up
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //We first check that the network is available before getting new content
        if (GetContentIfNetworkAvailable() == true){
//        	new HTTPGetAsync(SearchActivity.this).execute();
        }else{
        	Toast.makeText(this, "You don't seem to have Internet Access", Toast.LENGTH_SHORT).show();
        	Log.e(TAG, "No Network");
        }
                
//        connect("http://3dpartsource.com");
      }
	
	public boolean GetContentIfNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	      getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 
	
	public void Search (View view){
		
    	new HTTPGetAsync(SearchActivity.this).execute();
		//Do Nothing for now
		Log.i(TAG, "Search Function Initiated");
	}
} 
	