package com.alkliv.wheely;

import java.util.HashMap;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

/** A class to parse the Accessibility Details in JSON format */
class TaskParseAccessibility extends AsyncTask<String, Integer, HashMap<String,String>>{

	JSONArray  jArray;
	IFaceTaskParseAccessibility placesActivity;
	
	public TaskParseAccessibility(IFaceTaskParseAccessibility placesActivity) {
		this.placesActivity = placesActivity;
	}		
	
	// Invoked by execute() method of this object
	@Override
	protected HashMap<String,String> doInBackground(String... jsonData) {
	
		HashMap<String, String> hAccessibility = null;
		AccessibilityJSONParser accessibilityJsonParser = new AccessibilityJSONParser();
    
        try{
	
        	jArray = new JSONArray( jsonData[0] );
        	
            // Start parsing Accessibility details in JSON format
        	hAccessibility = accessibilityJsonParser.parse(jArray);
            
        }catch(Exception e){
                Log.d("Exception",e.toString());
        }
        return hAccessibility;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(HashMap<String,String> hAccessibility){	
		placesActivity.onEndTaskParseAccessibility(hAccessibility);					
	}
	
	public void detach(){
		placesActivity = null;
	}
	
}   
