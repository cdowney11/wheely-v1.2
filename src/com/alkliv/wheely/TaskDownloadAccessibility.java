package com.alkliv.wheely;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

	/** A class, to download Accessibility Details */
	class TaskDownloadAccesibility extends AsyncTask<String, Integer, String>{	

		String data = null;
		IFaceTaskDownloadAccessibility placesActivity;
		
		public TaskDownloadAccesibility(IFaceTaskDownloadAccessibility placesActivity) {
			this.placesActivity = placesActivity;
		}		
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();            
            placesActivity.preStartTaskDownloadAccessibility();            
        }
		
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){
			placesActivity.onEndTaskDownloadAccessibility(result);						
		}
		
		void detach(){
			placesActivity = null;
		}
		
		// Fetch data from strUrl    
	    String downloadUrl(String strUrl) throws IOException{
	        String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	                URL url = new URL(strUrl);
	                // Creating an http connection to communicate with url 
	                urlConnection = (HttpURLConnection) url.openConnection();

	                // Connecting to url 
	                urlConnection.connect();

	                // Reading data from url 
	                iStream = urlConnection.getInputStream();

	                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	                StringBuffer sb  = new StringBuffer();

	                String line = "";
	                while( ( line = br.readLine())  != null){
	                        sb.append(line);
	                }

	                data = sb.toString();

	                br.close();

	        }catch(Exception e){
	                Log.d("Exception while downloading url", e.toString());
	        }finally{
	                iStream.close();
	                urlConnection.disconnect();
	        }
	        return data;
	    }   

		
		
	}