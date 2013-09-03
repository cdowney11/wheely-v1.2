package com.alkliv.wheely;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PlaceDetailsJSONParser {
	
	/** Receives a JSONObject and returns a list */
	public HashMap<String,String> parse(JSONObject jObject){		
		
		JSONObject jPlaceDetails = null;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaceDetails = jObject.getJSONObject(ConstantValues.FIELD_RESULT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return getPlaceDetails(jPlaceDetails);
	}
	
	
	/** Parsing the Place Details Object object */
	private HashMap<String, String> getPlaceDetails(JSONObject jPlaceDetails){		
		
		HashMap<String, String> hPlaceDetails = new HashMap<String, String>();
		
		String name = "-NA-";		
		String latitude="";
		String longitude="";
		String formatted_address="-NA-";
		String formatted_phone="-NA-";		
		String place_id = "-NA-";
		String rating = "-NA-";
		String photoReference = "";
		
		try {
			// Extracting Place name, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_NAME)){
				name = jPlaceDetails.getString(ConstantValues.FIELD_NAME);
			}			
			
			// Extracting Place formatted_address, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_FORMATTED_ADDRESS)){
				formatted_address = jPlaceDetails.getString(ConstantValues.FIELD_FORMATTED_ADDRESS);
			}
			
			// Extracting Place formatted_phone, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_FORMATTED_PHONE_NUMBER)){
				formatted_phone = jPlaceDetails.getString(ConstantValues.FIELD_FORMATTED_PHONE_NUMBER);
			}
			
			// Extracting Place ID, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_ID)){
				place_id = jPlaceDetails.getString(ConstantValues.FIELD_ID);
			}
			
			// Extracting Place ID, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_RATING)){
				rating = jPlaceDetails.getString(ConstantValues.FIELD_RATING);
			}
			
			// Extracting Photo Reference, if available
			if(!jPlaceDetails.isNull(ConstantValues.FIELD_PHOTOS)){
				JSONArray photoArray = jPlaceDetails.getJSONArray(ConstantValues.FIELD_PHOTOS);
				if(photoArray.length()>0)
					photoReference = ((JSONObject)photoArray.get(0)).getString(ConstantValues.FIELD_PHOTO_REFERENCE);	// Takes reference of first photo				
			}			
			
			latitude = jPlaceDetails.getJSONObject(ConstantValues.FIELD_GEOMETRY).getJSONObject(ConstantValues.FIELD_LOCATION).getString(ConstantValues.FIELD_LATITUDE);
			longitude = jPlaceDetails.getJSONObject(ConstantValues.FIELD_GEOMETRY).getJSONObject(ConstantValues.FIELD_LOCATION).getString(ConstantValues.FIELD_LONGITUDE);
						
			hPlaceDetails.put(ConstantValues.EXTRA_NAME, name);
			hPlaceDetails.put(ConstantValues.EXTRA_LATITUDE, latitude);
			hPlaceDetails.put(ConstantValues.EXTRA_LONGITUDE, longitude);
			hPlaceDetails.put(ConstantValues.EXTRA_FORMATTED_ADDRESS, formatted_address);
			hPlaceDetails.put(ConstantValues.EXTRA_FORMATTED_PHONE_NUMBER, formatted_phone);
			hPlaceDetails.put(ConstantValues.EXTRA_PLACE_ID, place_id);
			hPlaceDetails.put(ConstantValues.EXTRA_RATING, rating);
			hPlaceDetails.put(ConstantValues.EXTRA_PHOTO_REFERENCE, photoReference);			
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}
				
		return hPlaceDetails;
	}
}
