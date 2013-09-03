package com.alkliv.wheely;

/**
 * By George Mathew K
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {
	
	HashMap<String, String> mHashMap;
	HashMap<String, String> mHAccessibility;

	String[] mAccesssibilityIcon = new String[] {
		"" + R.drawable.blank_accessibility_icon,
		"" + R.drawable.circle_red_dark,
		"" + R.drawable.circle_green_dark,
		"" + R.drawable.circle_yellow_dark
	};
	
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject, HashMap<String, String> hAccessibility){		
		
		JSONArray jPlaces = new JSONArray();
		mHAccessibility = hAccessibility;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray(ConstantValues.FIELD_RESULTS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return getPlaces(jPlaces);
	}
	
	
	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> place = null;	

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject)jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}
	
	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace){

		HashMap<String, String> place = new HashMap<String, String>();
		String placeName = "-NA-";
		String latitude="";
		String longitude="";
		String reference="";
		String id="";
		String accessibility = "0";
				
		
		try {
			// Extracting Place name, if available
			if(!jPlace.isNull(ConstantValues.FIELD_NAME)){
				placeName = jPlace.getString(ConstantValues.FIELD_NAME);
			}
			
			
			// Extracting Place Reference
			if(!jPlace.isNull(ConstantValues.FIELD_REFERENCE)){
				reference = jPlace.getString(ConstantValues.FIELD_REFERENCE);
			}
			
			id = jPlace.getString(ConstantValues.FIELD_ID);
			
			
			latitude = jPlace.getJSONObject(ConstantValues.FIELD_GEOMETRY).getJSONObject(ConstantValues.FIELD_LOCATION).getString(ConstantValues.FIELD_LATITUDE);
			longitude = jPlace.getJSONObject(ConstantValues.FIELD_GEOMETRY).getJSONObject(ConstantValues.FIELD_LOCATION).getString(ConstantValues.FIELD_LONGITUDE);
			
						
			
			place.put(ConstantValues.EXTRA_NAME, placeName);
			place.put(ConstantValues.EXTRA_LATITUDE, latitude);
			place.put(ConstantValues.EXTRA_LONGITUDE, longitude);
			place.put(ConstantValues.EXTRA_REFERENCE, reference);	
			place.put(ConstantValues.EXTRA_PLACE_ID, id);
			if(mHAccessibility.containsKey(id)){
				accessibility = mHAccessibility.get(id);
			}
			
			place.put(ConstantValues.EXTRA_ACCESSIBILITY, accessibility);
			place.put(ConstantValues.EXTRA_ACCESSIBILITY_ICON, mAccesssibilityIcon[Integer.parseInt(accessibility) ]); // accessibility icon for listview
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return place;
	}
}
