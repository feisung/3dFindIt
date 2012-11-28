package com.threedindustries.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends HomeActivity {
	private static final String TAG = "Search Activity";
	private TextView tv;
	public static final String url = "http://www.3dfabsource.com:12002/search/index/format/json";
	@TargetApi(11)
	@Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        //Enable navigating up
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv = (TextView) findViewById(R.id.http_response);
        
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
		
		//We first check that the network is available before getting new content
        if (GetContentIfNetworkAvailable() == true){
        	Log.i(TAG, "Search Function Initiated");
        	findViewById(R.id.Search).setVisibility(View.GONE);
        	findViewById(R.id.http_response).setVisibility(View.VISIBLE);

    		
    		//get user entered search term
    		EditText searchTxt = (EditText)findViewById(R.id.search_query);
    		String searchTerm = searchTxt.getText().toString();
    		if(searchTerm.length()>0){
    			try{
    				//encode in case user has included symbols such as spaces etc
    				String encodedSearch = URLEncoder.encode(searchTerm, "UTF-8");
    				//append encoded user search term to search URL
    				String searchURL = "http://www.3dfabsource.com:12002/search";	//+encodedSearch;
    				//instantiate and execute AsyncTask
    				new ExecuteGetAsync().execute(searchURL);

    			}
    			catch(Exception e){ 
    				tv.setText("Sorry, I was Unable to Get anything! Try Again please.");
    				e.printStackTrace(); 
    			}
    		}
    		else 
    			tv.setText("Enter a search query!");
    		}
        else{
        	Toast.makeText(this, "You don't seem to have Internet Access", Toast.LENGTH_SHORT).show();
        	Log.e(TAG, "No Network");
        }
	}
	
	/*-`	Async HTTP Class
	 * 
	 * 
	 * 
	 * *********************************************************************************/
	private class ExecuteGetAsync extends AsyncTask<String, Void, String> {
		/*
		 * Background Task to get the data
		 */
		@Override
		protected String doInBackground(String... params) {
			String output = null;
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(ServerAddress);
		    Log.v(TAG, "IP Address in use: " + ServerAddress);
		    
    	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
    	    // params comes from the execute() call: params[0] is the url.
    	    nameValuePairs.add (new BasicNameValuePair("keyword", "wheel"));		
    	    nameValuePairs.add (new BasicNameValuePair("refined_keyword", "any"));		
	       try {
	    	    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	    	    HttpResponse response = httpClient.execute(httpPost);
	            Log.v(TAG, "Post sent");

	    	    HttpEntity httpEntity = response.getEntity();
	    	    output = EntityUtils.toString(httpEntity);
	    	    Log.i(TAG, "The Output is" + output);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return output;
	}

		protected void onPostExecute(String output) {
			StringBuilder siteResultBuilder = new StringBuilder();
			try {
				//get JSONObject from result
				JSONObject resultObject = new JSONObject(output);
				Log.i(TAG, "Got Site Result: " + resultObject);
				JSONArray responseArray = resultObject.getJSONArray("results");
				Log.i(TAG, "Got Result array: " + responseArray);
				//loop through each result in the array
				for (int t=0; t<responseArray.length(); t++) {
					//each item is a JSONObject
					JSONObject responseObject = responseArray.getJSONObject(t);
					Log.i(TAG, "Loading Array Data: " + responseObject);
					//get the results
					siteResultBuilder.append(responseObject.getString("product_name")+": ");
					siteResultBuilder.append(responseObject.get("product_description")+"\n\n");
					Log.i(TAG, "Appending ResultBuilder");

				}
			}
			catch (Exception e) {
				tv.setText("Whoops - something went wrong!");
				e.printStackTrace();
			}
			//check result exists
			if(siteResultBuilder.length()>0){
				tv.setText(siteResultBuilder.toString());
				Log.i(TAG, "Showing Results");	
			}else
				tv.setText("Sorry - Unable to return any results from your search!");		
			}	
	}
}





	