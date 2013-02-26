package com.threedindustries.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class SearchActivity extends HomeActivity {
	private static final String TAG = "Search Activity";
	public TextView tv;
	public ImageView imageview;
	String query;
	// Server Address that replies to Search Queries
	public static final String url = "http://www.3dfabsource.com:12002/search/index/format/json";

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		// Enable navigating up
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);	//set true for home dashboard link

		/**
		 * TODO: Add Argument for when the correct Upload Search Result Array is
		 * returned, then update the UI
		 * */
		// When Receiving Intent extras data from file upload results
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		String UploadSimulation = extras.getString("doText");
		String UploadSearchResult = extras.getString("output");
		if (UploadSearchResult != null) {
			Log.i(TAG, "Performing UploadQuery");
			// Log.i(TAG, "Got Result Array: " + UploadSearchResult);
			// here we point to the processing of results to UI
			ExecutePostAsync executePost = new ExecutePostAsync();
			executePost.SearchResultBuilder(UploadSearchResult);
		}
		if (UploadSimulation != null) {
			Log.i(TAG, "Simulating Upload Query");
			doTextSearch(UploadSimulation);
		}
		// Handler for search field in UI
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			Log.i(TAG, "The Search Term Entered: " + query);
		}

		doTextSearch(query);
	}

	protected void doTextSearch(String query) {
		// We first check that the network is available before getting new
		// content
		if (GetContentIfNetworkAvailable() == true) {
			// instantiate and execute AsyncTask
			if (query != null) {
				new ExecutePostAsync().execute(query);
			} else
				Log.i(TAG, "## Query is null");
		}

		else {
			Toast.makeText(this, "You don't seem to have Internet Access",
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "No Network");
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this activity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		handleIntent(intent);
	}
	
	@Override
	public void onBackPressed() {
	   Log.d(TAG, "onBackPressed Called");
	   Intent setIntent = new Intent(this, IndustryActivity.class);
	   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   startActivity(setIntent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// handles a click on a search suggestion; launches activity to show
			// word
			Intent wordIntent = new Intent(this, SearchActivity.class);
			wordIntent.setData(intent.getData());
			startActivity(wordIntent);
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			doTextSearch(query);
		}
	}

	public boolean GetContentIfNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/*
	 * To-Do: Clean up unused elements ` Async HTTP Class
	 * 
	 * 
	 * 
	 * **************************************************************************
	 * ******
	 */
	private class ExecutePostAsync extends AsyncTask<String, Void, String> {
		/*
		 * Background Task to get the data
		 */

		private ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);

		protected void onPreExecute() {
			// Show progress screen
			setContentView(R.layout.loading_view);
			// Dialog.setMessage("Please wait...");
			// Dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String output = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ServerAddress);
			Log.v(TAG, "IP Address in use: " + ServerAddress);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			// params comes from the execute() call: params[0] is the url.
			nameValuePairs.add(new BasicNameValuePair("keyword[keyword]",
					params[0]));
			Log.i(TAG, "Searched for: " + params[0]);
			nameValuePairs.add(new BasicNameValuePair(
					"keyword[refine_keyword]", "any")); // Change me for refined
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				Log.v(TAG, "Post sent");

				HttpEntity httpEntity = response.getEntity();
				output = EntityUtils.toString(httpEntity);
				// Log.i(TAG, "The Output is" + output); //For Debug
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
			setContentView(R.layout.search_activity);

			SearchResultBuilder(output);
			// Update the View to show the results
			// findViewById(R.id.Search).setVisibility(View.GONE);
			// findViewById(R.id.queryResult).setVisibility(View.VISIBLE);
		}

		// Temporary public class to display results
		public void SearchResultBuilder(String output) {

			try {
				if (Dialog.isShowing()) {
					Dialog.dismiss();
				}
				// do your Display and data setting operation here
			} catch (Exception e) {

			}
			final ArrayList<SearchResults> results = Result(output);
			//Log.i(TAG, "#Loaded results Array: " + results);

			ListView listView = (ListView) findViewById(R.id.resultsListView);
			listView.setAdapter(new ResultsItemAdepter(SearchActivity.this,
					R.layout.listitem, results));
			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					View searchResultsView = (View) findViewById(R.id.SearchResults);
					View webView = (View) findViewById(R.id.webview);

					SearchResults searchResults = results.get(position);
					// Log.i(TAG, "Clicked for URL: " +
					// searchResults.product_url);

					searchResultsView.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);
					WebView myWebView = (WebView) findViewById(R.id.webview);
					myWebView.loadUrl(searchResults.product_url);
					// Temporarily linking user to 3dpartsource site uncomment
					// line above to enable direct link to product clicked
					// myWebView.loadUrl("http://www.3dpartsource.com");
				}

			});
		}

		protected ArrayList<SearchResults> Result(String output) {

			ArrayList<SearchResults> results = new ArrayList<SearchResults>();
			try {
				// get JSONObject from result
				JSONObject resultObject = new JSONObject(output);
				// Log.i(TAG, "Got Site Result: " + resultObject);
				JSONArray responseArray = resultObject
						.getJSONArray("jsonSearchResults");
				// Log.i(TAG, "Got Result array: " + responseArray); //For Debug
				// loop through each result in the array
				for (int t = 0; t < responseArray.length(); t++) {
					// each item is a JSONObject
					JSONObject responseObject = responseArray.getJSONObject(t);
					// Log.i(TAG, "Loading Array Data: " + responseObject);
					// //For Debug

					// For Image Array, we first select the Image Object and
					// then the urls from it
					JSONObject imageObject = responseObject
							.getJSONObject("image");
					// We use the JSONArray for the image urls
					JSONArray imageArray = imageObject.getJSONArray("urls");
					// This returns an array of 0 - 3: For now we just select
					// the first item, i.e. 0
					String image1 = imageArray.getString(0);
					// Now Show the images
					// Log.i(TAG, "The image to string: " + image1);

					SearchResults result = new SearchResults(responseObject
							.getString("product_name").toString(),
							responseObject.get("product_description")
									.toString(), image1, responseObject
									.getString("product_url").toString());
					results.add(result);
					Log.i(TAG,
							"\n Product Name: "
									+ responseObject.getString("product_name")
											.toString());
				}

			} catch (Exception e) {
				tv.setText("Was Unable to return any result!");
				e.printStackTrace();
			}
			return results;
		}

		public class ResultsItemAdepter extends ArrayAdapter<SearchResults> {
			private ArrayList<SearchResults> results;

			public ResultsItemAdepter(Context context, int textViewResourceId,
					ArrayList<SearchResults> results) {
				super(context, textViewResourceId, results);
				this.results = results;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.listitem, null);
				}

				SearchResults searchResults = results.get(position);
				Log.i(TAG, "Value for SR= " + searchResults + " and position: "
						+ position);
				if (searchResults != null) {
					Log.i(TAG, "++SearchResults");
					TextView product_name = (TextView) v
							.findViewById(R.id.product_name);
					TextView description = (TextView) v
							.findViewById(R.id.http_response);
					// ImageView image = (ImageView)
					// v.findViewById(R.id.preview_sec);
					SmartImageView image = (SmartImageView) v
							.findViewById(R.id.preview_sec);

					if (product_name != null) {
						product_name.setText(searchResults.product_name);
						Log.i(TAG, "*Name: " + searchResults.product_name);
					}

					if (description != null) {
						description.setText(searchResults.product_description);
						Log.i(TAG, "*Description: "
								+ searchResults.product_description);
					}

					if (image != null) {
						image.setImageUrl(searchResults.image_url);
						// image.setImageBitmap(getBitmap(searchResults.image_url));
						Log.i(TAG, "*Bitmap: " + searchResults.image_url);
					}
				} else {
					TextView product_name = (TextView) v
							.findViewById(R.id.product_name);
					product_name
							.setText("Unfortunately No Matched Results, Please Retry.");
				}
				return v;
			}
		}
	}
}
