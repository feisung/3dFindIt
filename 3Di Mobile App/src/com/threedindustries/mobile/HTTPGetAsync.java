package com.threedindustries.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HTTPGetAsync extends AsyncTask<String, Void, Void>{

	private static final String TAG = "HTTPGET Task";
	private Activity activity;
	
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		Log.i(TAG, "HTTPGet Async On Start");
	
		try {
			  URL url = new URL("http://www.3dpartsource.com");
			  HttpURLConnection con = (HttpURLConnection) url
			    .openConnection();
			  readStream(con.getInputStream());
			  } catch (Exception e) {
			  e.printStackTrace();
			}
            Log.v(TAG, "Post sent");
    return null;
	}
	
	private void readStream(InputStream in) {
		  BufferedReader reader = null;
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		      System.out.println(line);
		    }
		  } catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		  }
		} 
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	public HTTPGetAsync(Activity activity) {
	    this.activity = activity;
	}
	protected void onPostExecute(Boolean result){
		Log.i(TAG, "Carrying out result of background task");
		    activity.startActivity(new Intent(activity, HomeActivity.class));
	}
	
}
