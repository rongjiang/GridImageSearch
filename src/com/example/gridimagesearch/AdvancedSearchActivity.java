package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AdvancedSearchActivity extends Activity {
	Spinner colorChoice;
	EditText siteRestriction;
	QueryOption queryOption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_search);
		colorChoice = (Spinner) findViewById(R.id.spColor);
		siteRestriction = (EditText) findViewById(R.id.etSite);
		queryOption = (QueryOption)getIntent().getSerializableExtra("options");
		if (queryOption == null)
			queryOption = new QueryOption();
		else {
			// colorChoice.setSelection(queryOption.getImageColor());
			siteRestriction.setText(queryOption.getSiteRestriction());
		}
	}
	
	public void getQueryOptions() {
		String color = colorChoice.getSelectedItem().toString();
		queryOption.setImageColor(color);
		queryOption.setSiteRestriction(siteRestriction.getText().toString());
		Log.d("DEBUG", "Query options: " + color + siteRestriction.getText().toString());				
	}
	
	public void onSave(View v) {
		getQueryOptions();
		Intent i = new Intent();
		i.putExtra("options", queryOption);
		setResult(RESULT_OK, i);
		Log.d("DEBUG", "Saving advanced search options..." + queryOption.getSiteRestriction().toString() 
				+ queryOption.getImageColor());
		finish();
	}
}
