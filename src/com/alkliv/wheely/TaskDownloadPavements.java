package com.alkliv.wheely;

import android.os.AsyncTask;
import android.util.Log;

class TaskDownloadPavements extends AsyncTask<String, Integer, String>{	
	
	GooglePlacesMapActivity googlePlacesMapActivity;
	public TaskDownloadPavements(GooglePlacesMapActivity googlePlacesMapActivity) {
		this.googlePlacesMapActivity = googlePlacesMapActivity;
	}
	
	// Invoked by execute() method of this object
	@Override
	protected String doInBackground(String... url) {
		String data = null;

		try{
			data = googlePlacesMapActivity.downloadUrl(url[0]);
		}catch(Exception e){
			 Log.d("Background Task",e.toString());
		}
		return data;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(String result){					
		googlePlacesMapActivity.startPavementsParserTask(result);			
	}
	
	public void detach(){
		googlePlacesMapActivity = null;
	}
}
