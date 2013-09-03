package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class TaskParseGeocode extends AsyncTask<String, Void, List<HashMap<String,String>>>{
	
	SinglePavementActivity pavementActivity;
	JSONObject jObject = null;
	
	public TaskParseGeocode(SinglePavementActivity pavementActivity) {
		this.pavementActivity = pavementActivity;
	}

	@Override
	protected List<HashMap<String, String>> doInBackground(String... params) {
		
		try {
			jObject = new JSONObject(params[0]);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		GeocodeJSONParser parser = new GeocodeJSONParser();
		
		List list = parser.parse(jObject);
		return list;
	}	
	
	@Override
	protected void onPostExecute(List<HashMap<String, String>> result) {		
		super.onPostExecute(result);
		pavementActivity.onEndTaskParseGeocode(result);
	}
	
	protected void detach(){
		pavementActivity = null;
	}
	
	
}