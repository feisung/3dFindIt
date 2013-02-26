package com.threedindustries.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	Intent intent;
	private static final String TAG = "3DI Home Activity";
	static String ServerAddress = "http://www.3dfabsource.com:12002/search/index/format/json";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate 3DI App");
		setContentView(R.layout.home_activity);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_activity, menu);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default
		// TO-DO Add handler
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		// Handling the different menu items
		switch (item.getItemId()) {
		case R.id.menu_search:
			// intent = new Intent(this, IndustryActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			onSearchRequested();
			Log.i(TAG, "About to Search");
			return true;
		case R.id.menu_upload:
			// go to Search Activity
			intent = new Intent(this, IndustryActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.putExtra("shortcut", "true");
			startActivity(intent);
			return true;
		case android.R.id.home:
			// go back to previous activity
			intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_info:
			// go to 3dindustri.es site
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.3dindustri.es"));
			startActivity(browserIntent);
			return true;
			// other case statements
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// Handles On Click for 3DPartsource Logo
	public void n3dps(View view) {
		intent = new Intent(this, IndustryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	// Handles On Click for 3DArchiSource Logo
	public void archisource(View view) {
		Toast.makeText(this, "This Feature is Coming soon ...",
				Toast.LENGTH_SHORT).show();
	}

	// Handles On Click for 3DMechasource Logo
	public void mechasource(View view) {
		Toast.makeText(this, "This Feature is Coming soon ...",
				Toast.LENGTH_SHORT).show();
	}

	// Handles On Click for 3DSciSource Logo
	public void scisource(View view) {
		Toast.makeText(this, "This Feature is Coming soon ...",
				Toast.LENGTH_SHORT).show();
	}

	// Handles On Click for 3GameSource Logo
	public void Gamesource(View view) {
		Toast.makeText(this, "This Feature is Coming soon ...",
				Toast.LENGTH_SHORT).show();
	}

	// Handles On Click for 3GameSource Logo
	public void robosource(View view) {
		Toast.makeText(this, "This Feature is Coming soon ...",
				Toast.LENGTH_SHORT).show();
	}

}
