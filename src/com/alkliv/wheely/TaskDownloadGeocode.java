package com.alkliv.wheely;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class TaskDownloadGeocode extends AsyncTask<String, Void, String>{
	SinglePavementActivity pavementActivity;
	
	public TaskDownloadGeocode(SinglePavementActivity pavementActivity) {
		this.pavementActivity = pavementActivity;
	}
	
	/** A method to download json data from url */
    private static String downloadUrl(String strUrl) throws IOException{
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

	@Override
	protected String doInBackground(String... params) {
		String lat = params[0];
		String lng = params[1];
		String data = "";
		String url = ConstantValues.GEOCODING_URL + "&latlng="+lat+","+lng;
		
		try{
			data = downloadUrl(url);
		}catch(Exception e){
			 Log.d("Background Task",e.toString());
		}
		return data;
	}	
	
	@Override
	protected void onPostExecute(String result) {		
		super.onPostExecute(result);
		pavementActivity.onEndTaskDownloadGeocode(result);
	}
	
	
	protected void detach(){
		pavementActivity = null;
	}
	
}
