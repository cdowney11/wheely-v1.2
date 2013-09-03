package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/** A class to parse the Google Places in JSON format */
class TaskParsePlaces extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

	JSONObject jObject;
	IFaceTaskParsePlaces placesActivity;
	Place[] placeArray;
	HashMap<String, String> hAccessibility;
	
	public TaskParsePlaces(IFaceTaskParsePlaces placesActivity) {
		this.placesActivity = placesActivity;			
	}
	
	public void setHAccessibility(HashMap<String, String> hAccessibility){
		this.hAccessibility = hAccessibility;
	}
	
	// Invoked by execute() method of this object
	@Override
	protected List<HashMap<String,String>> doInBackground(String... jsonData) {
	
		List<HashMap<String, String>> places = null;			
		PlaceJSONParser placeJsonParser = new PlaceJSONParser();
    
        try{
        	jObject = new JSONObject(jsonData[0]);       	
        	
            /** Getting the parsed data as a List construct */
            places = placeJsonParser.parse(jObject,hAccessibility);
            
        }catch(Exception e){
                Log.d("Exception",e.toString());
        }
        
        
        // Populating Place array from Place list
        // The array is needed to pass the data to map
        if(places.size()>0){
        	placeArray = new Place[places.size()];
	        for(int i=0;i<places.size();i++){
	        	placeArray[i] = new Place();
	        	placeArray[i].mLat = places.get(i).get(ConstantValues.EXTRA_LATITUDE);
	        	placeArray[i].mLng = places.get(i).get(ConstantValues.EXTRA_LONGITUDE);
	        	placeArray[i].mPlaceName = places.get(i).get(ConstantValues.EXTRA_NAME);		        	
	        	placeArray[i].mReference = places.get(i).get(ConstantValues.EXTRA_REFERENCE);
	        	placeArray[i].mPlaceId = places.get(i).get(ConstantValues.EXTRA_PLACE_ID);
	        	placeArray[i].mAccessibility = places.get(i).get(ConstantValues.EXTRA_ACCESSIBILITY);
	        }
        }        
                
        return places;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(List<HashMap<String,String>> list){	
		placesActivity.onEndTaskParsePlaces(list, placeArray);            
	}
	
	public void detach(){
		if(placesActivity!=null)
			placesActivity = null;
	}
}	
