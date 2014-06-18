package com.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	EditText etQuery;
	Button btnSearch;
	GridView gvResults;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	boolean clearView;
	QueryOption queryOption;
	static final int REQUEST_CODE = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		setupOnItemClickListener();
		setupOnScrollListener();
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.advanced_search, menu);
        return true;
    }
	
	private void setupOnItemClickListener() {
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);				
			}
		});		
	}
	
	private void setupOnScrollListener() {
        gvResults.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
            // Triggered only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView
	    	Log.d("DEBUG", "Loading more... page = " + page + ", itemCount = " + totalItemsCount);
	        loadMoreDataFromApi(totalItemsCount); 
	        Log.d("DEBUG", "Loading more...DONE");
	    }
        });

	}
	
	public void onImageSearch(View v) {
		clearView = true;
		loadSearchResults(0);
	}
	
    // Append more data into the adapter
    public void loadMoreDataFromApi(int offset) {
    	clearView = false;
    	loadSearchResults(offset);
    }
    
    private void loadSearchResults(int offset) {
		String queryStr = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + queryStr, Toast.LENGTH_SHORT).show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(constructQueryStr(offset, queryStr), 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					// only clear adapter for a new search
					if (clearView) imageAdapter.clear();						 
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", "imageResults.size() = " + imageResults.size());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
    }
    
    private String constructQueryStr(int offset, String term) {
    	StringBuffer queryStr = new StringBuffer("https://ajax.googleapis.com/ajax/services/search/images?" + 
				"rsz=8&start=" + offset + "&v=1.0&q=" + Uri.encode(term));
    	if (queryOption != null) {
    		String color = queryOption.getImageColor();
    		String site = queryOption.getSiteRestriction();
    		if (color != null && !"".equals(color)) {
    			queryStr.append("&imgcolor=" + color);
    		}
    		if (site != null && !"".equals(site)) {
    			queryStr.append("&as_sitesearch=" + site);
    		}
    	}
    	Log.d("DEBUG", queryStr.toString());
    	return queryStr.toString();
    }
    
    public void onAdvanceSearch(MenuItem mi) {
        Toast.makeText(this, "Advanced searching...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getBaseContext(), AdvancedSearchActivity.class);
        if (queryOption != null) {
        	i.putExtra("options",  queryOption);
        }
        startActivityForResult(i, REQUEST_CODE);
     }
    
    // Handle the result once the activity returns a result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
    		
    		queryOption = (QueryOption) data.getSerializableExtra("options");
    		
    	}
    }
}
