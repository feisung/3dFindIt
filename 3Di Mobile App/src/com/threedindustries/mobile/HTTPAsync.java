package com.threedindustries.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPAsync extends AsyncTask<String, Void, Void>{

	private static final String TAG = "HTTPPost Task";
	// Server URL that will handle HTTP Posts on the server; Python listens on /arduino
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
	
	HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(HomeActivity.ServerAddress);
    httpPost.addHeader("content-type:", "multipart/form-data");
    Log.v(TAG, "IP Address in use: " + HomeActivity.ServerAddress);
    	
       try {
    	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    	    // params comes from the execute() call: params[0] is the url.
    	    nameValuePairs.add (new BasicNameValuePair("keyword=", params[0]));		
    	    nameValuePairs.add (new BasicNameValuePair("[keyword: ", params[0]));		
    	    nameValuePairs.add (new BasicNameValuePair(" [keyword_refine: ", params[0]));	
    	    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            @SuppressWarnings("unused")
			HttpResponse response = httpClient.execute(httpPost);
            Log.v(TAG, "Post sent");
            
       } catch (ClientProtocolException e) {
        e.printStackTrace();
       } catch (IOException e) {
        e.printStackTrace();
       }
    return null;
	}
	
	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(Long result){
//        Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_SHORT).show();		
	}
	
}
