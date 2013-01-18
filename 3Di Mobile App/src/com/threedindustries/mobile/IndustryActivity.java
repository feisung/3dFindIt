package com.threedindustries.mobile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class IndustryActivity extends HomeActivity {

	private static final String TAG = "Industry Activity";
	static String UploadServerAddress = "http://www.3dfabsource.com:12002/search/file-upload/format/json";

	final int UploadFile = 1;
	private ImageAdapter adapter;
	public TextView tv;

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "OnCreate");

		setContentView(R.layout.industry_activity);
		// Enable navigating up
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		GridView gridview = (GridView) findViewById(R.id.gridview);
		adapter = new ImageAdapter(this, data);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				openPage((String) adapter.getItem(position));
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

	public void Search(View view) {

		Log.i(TAG, "Opening file Browser to select what to upload");
		// Open File Browser Chooser
		Intent intentBrowseFiles = new Intent(Intent.ACTION_GET_CONTENT);
		intentBrowseFiles.setType("*/*");

		intentBrowseFiles.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intentBrowseFiles, UploadFile);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case UploadFile: {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String filePath = uri.getPath();
				Log.i(TAG, "File Path: " + filePath);

				// Upon Collection of file path, we execute the post with the
				// path to the file
				// To Debug
				new ExecuteUploadSearchAsync().execute(filePath);

			}
		}
		}
	}

	/*
	 * To-Do: Clean up unused elements ` Async HTTP Class
	 * 
	 * 
	 * 
	 * **************************************************************************
	 * ******
	 */
	private class ExecuteUploadSearchAsync extends
			AsyncTask<String, Void, String> {
		/*
		 * Background Task to upload file and get the data
		 */
		
		private ProgressDialog Dialog = new ProgressDialog(IndustryActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please wait...");
			Dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			String output = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(UploadServerAddress);
			Log.v(TAG, "IP Address in use: " + UploadServerAddress);
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			// params comes from the execute() call: params[0] is the url.

			File file = new File(params[0]);
			FileBody fileBody = new FileBody(file);

			try {
				reqEntity.addPart("model", fileBody);
				// Log.i(TAG, "Searched for: " + params[0] +
				// " and the reqEntity has: " + reqEntity);
				httpPost.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(httpPost);
				Log.v(TAG, "Post sent");

				HttpEntity httpEntity = response.getEntity();
				output = EntityUtils.toString(httpEntity);
				// Log.i(TAG, "The Output is" + output); //For Debug

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return output;
		}

		protected void onPostExecute(String output) {
			Intent search = new Intent(IndustryActivity.this,
					SearchActivity.class);
			search.putExtra("output", output);
			startActivity(search);
			// Log.i(TAG, "Passed Intent Extra");
		}
	}
}
