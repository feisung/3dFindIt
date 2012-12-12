package com.threedindustries.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends HomeActivity {
	private static final String TAG = "Search Activity";
	public TextView tv;
	public ImageView imageview;
	String query;
	//Server Address that replies to Search Queries
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
        imageview = (ImageView) findViewById(R.id.preview);
        
        //Handler for search field in UI
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          query = intent.getStringExtra(SearchManager.QUERY);
          Log.i(TAG, "The Search Term Entered: " + query);
        }
        
		//We first check that the network is available before getting new content
        if (GetContentIfNetworkAvailable() == true){   		
				//instantiate and execute AsyncTask
				new ExecuteGetAsync().execute(query);
        }

        else{
        	Toast.makeText(this, "You don't seem to have Internet Access", Toast.LENGTH_SHORT).show();
        	Log.e(TAG, "No Network");
        }
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
	
	
	/*To-Do: Clean up unused elements
	 * `	Async HTTP Class
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
    	    nameValuePairs.add (new BasicNameValuePair("keyword[keyword]", params[0]));	
    	    Log.i(TAG, "Searched for: " + params[0]);
    	    nameValuePairs.add (new BasicNameValuePair("keyword[refine_keyword]", "any"));		
	       try {
	    	    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	    	    HttpResponse response = httpClient.execute(httpPost);
	            Log.v(TAG, "Post sent");

	    	    HttpEntity httpEntity = response.getEntity();
	    	    output = EntityUtils.toString(httpEntity);
	    	    //Log.i(TAG, "The Output is" + output);			//For Debug
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
				JSONArray responseArray = resultObject.getJSONArray("jsonSearchResults");
				//Log.i(TAG, "Got Result array: " + responseArray);		//For Debug
				//loop through each result in the array
				for (int t=0; t<responseArray.length(); t++) {
					//each item is a JSONObject
					JSONObject responseObject = responseArray.getJSONObject(t);
					//Log.i(TAG, "Loading Array Data: " + responseObject);		//For Debug
					
					//For Image Array, we first select the Image Object and then the urls from it
					JSONObject imageObject = responseObject.getJSONObject("image");
					String image = imageObject.getString("urls");
					JSONArray imageArray = imageObject.getJSONArray("urls");
					//This returns an array of 0 - 3: For now we just select the first item 0
					String image1 = imageArray.getString(0);
					//Now Show the images
						imageview.setImageBitmap(getBitmap("http://www.3dpartsource.com/images/products/m2101_1/320_240.jpg"));
						Log.i(TAG, "The image to string: " + image1);
					
					siteResultBuilder.append(responseObject.getString("product_name")+": " + "\n");
					siteResultBuilder.append(responseObject.get("product_description")+" " + "\n\n");
					Log.i(TAG, "Appending ResultBuilder");
		        	//Update the View to show the results
		        	findViewById(R.id.Search).setVisibility(View.GONE);
		        	findViewById(R.id.queryResult).setVisibility(View.VISIBLE);
				}
			}
			catch (Exception e) {
				tv.setText("Was Unable to return any result!");
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
	
	public Bitmap getBitmap(String bitmapUrl) {
		  try {
		    URL url = new URL(bitmapUrl);
		    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		  }
		  catch(Exception ex) {return null;}
		}
}





	