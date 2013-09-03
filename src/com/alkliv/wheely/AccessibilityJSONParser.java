package com.alkliv.wheely;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccessibilityJSONParser {	
	
	/** Receives a JSONObject and returns a list */
	public HashMap<String,String> parse(JSONArray jObject){		
		
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return getPlaces(jObject);
	}
	
	
	
	private HashMap<String, String> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		HashMap<String, String> placeMap = new HashMap<String,String>();
		JSONObject jPlace  = null;
		
		String placeid = "-NA-";
		String accessibility = "-NA-";

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */
				jPlace = (JSONObject)jPlaces.get(i);				
				
				try {
					// Extracting Place id, if available
					if(!jPlace.isNull(ConstantValues.FIELD_ACCESSIBILITY_PLACE_ID)){
						placeid = jPlace.getString(ConstantValues.FIELD_ACCESSIBILITY_PLACE_ID);
					}
					
					
					// Extracting accessibility
					if(!jPlace.isNull(ConstantValues.FIELD_ACCESSIBILITY )){
						accessibility = jPlace.getString(ConstantValues.FIELD_ACCESSIBILITY);
					}
					
					// key of each object is placeid
					// value of each object is accessibility
					placeMap.put(placeid, accessibility);				
						
					
				} catch (JSONException e) {
					e.printStackTrace();
				}			

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placeMap;
	}
}
