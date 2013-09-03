package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/** A class, to download Pavements Details */
class TaskParsePavements extends AsyncTask<String, Integer, List<HashMap<String, String>>>{	
	
	GooglePlacesMapActivity googlePlacesMapActivity;
	
	public TaskParsePavements(GooglePlacesMapActivity googlePlacesMapActivity) {
		this.googlePlacesMapActivity = googlePlacesMapActivity;
	}
	
	// Invoked by execute() method of this object
	@Override
	protected List<HashMap<String, String>> doInBackground(String... jsondata) {
		PavementsJSONParser pavementsJSONParser = new PavementsJSONParser();
		JSONObject json = null;
		try {
			json = new JSONObject(jsondata[0]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		List<HashMap<String, String>> pavementsList = pavementsJSONParser.parse(json);
		return pavementsList;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(List<HashMap<String, String>> pavementsList){					
		for(int i = 0;i < pavementsList.size();i++){
			googlePlacesMapActivity.showPavement(pavementsList.get(i));
		}			
	}
	
	public void detach(){
		googlePlacesMapActivity = null;
	}
	
}
