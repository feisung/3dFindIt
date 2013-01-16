package com.threedindustries.mobile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
	
	HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(HomeActivity.ServerAddress);
    httpPost.addHeader("content-type:", "multipart/form-data");
    HttpURLConnection connection = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;
    
    
    String pathToOurFile = "/sdcard/test.step";
    String urlServer = "http://www.3dfabsource.com:12002/search/file-upload/format/json";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String serverResponseMessage;
    int serverResponseCode;
        
    
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;
    Log.v(TAG, "IP Address in use: " + HomeActivity.ServerAddress);
    
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
    // params comes from the execute() call: params[0] is the url.
    nameValuePairs.add (new BasicNameValuePair("model", "model"));	
    nameValuePairs.add (new BasicNameValuePair("keyword[refine_keyword]", "any"));	  	
       try {
    	   FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
	
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();						
            Log.v(TAG, "Post sent");
            
         // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            Log.i(TAG, "Uploading file from: " + params[0]);
            outputStream.writeBytes( params[0] +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
	            outputStream.write(buffer, 0, bufferSize);
	            bytesAvailable = fileInputStream.available();
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	            Log.i(TAG, "Uploading item. . .");
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
            serverResponseMessage = connection.getResponseMessage();
            
            Log.i(TAG, "The response code is: " + serverResponseCode + " and The Message: " + serverResponseMessage);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            
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
		Log.i(TAG, "On PostExecute Ready to handle");
//        Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_SHORT).show();		
	}
	
}
