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

public class PavementsJSONParser {

	
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		JSONArray jPavements = new JSONArray();
		
		try {			
			/** Retrieves all the pavements in the array */
			jPavements = jObject.getJSONArray(ConstantValues.FIELD_PAVEMENT_PAVEMENTS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPavements with the array of json object
		 * where each json object represent a pavement
		 */
		return getPavements(jPavements);
	}
	
	
	private List<HashMap<String, String>> getPavements(JSONArray jPavements){
		int pavementsCount = jPavements.length();
		List<HashMap<String, String>> pavementsList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> pavement = null;	

		/** Taking each pavement, parses and adds to list object */
		for(int i=0; i<pavementsCount;i++){
			try {
				
				/** Call getPavement with pavement JSON object to parse the pavement */
				pavement = getPavement((JSONObject)jPavements.get(i));
				pavementsList.add(pavement);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		
		return pavementsList;
	}
	
	/** Parsing the Pavement JSON object */
	private HashMap<String, String> getPavement(JSONObject jPavement){

		HashMap<String, String> hPavement = new HashMap<String, String>();
		
		String latitude = "";
		String longitude = "";
		String lat_lng = "";
		String comment = "";		
		
		try {
			// Extracting latitude
			if(!jPavement.isNull(ConstantValues.FIELD_PAVEMENT_LATITUDE)){
				latitude = jPavement.getString(ConstantValues.FIELD_PAVEMENT_LATITUDE);
			}
			
			// Extracting longitude
			if(!jPavement.isNull(ConstantValues.FIELD_PAVEMENT_LONGITUDE)){
				longitude = jPavement.getString(ConstantValues.FIELD_PAVEMENT_LONGITUDE);
			}
			
			// Extracting comment
			if(!jPavement.isNull(ConstantValues.FIELD_PAVEMENT_COMMENT)){
				comment = jPavement.getString(ConstantValues.FIELD_PAVEMENT_COMMENT);
			}
			
			// Both latitude and longitude in a single variable for displaying in the pavements listview
			lat_lng = latitude + "," + longitude;
			
			hPavement.put(ConstantValues.EXTRA_PAVEMENT_LATITUDE, latitude);
			hPavement.put(ConstantValues.EXTRA_PAVEMENT_LONGITUDE, longitude);
			hPavement.put(ConstantValues.EXTRA_PAVEMENT_LAT_LNG, lat_lng);
			hPavement.put(ConstantValues.EXTRA_PAVEMENT_COMMENT, comment);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return hPavement;
	}
}
