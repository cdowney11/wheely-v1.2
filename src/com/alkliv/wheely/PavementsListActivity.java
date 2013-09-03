package com.alkliv.wheely;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PavementsListActivity extends ListActivity {
	
	DownloadPavementsTask mDownloadPavementsTask = null;
    ParsePavementsTask mParsePavementsTask = null;
    private ProgressDialog mProgressDialog = null;
    private ListView mList = null;
    private double mLatitude = 0;
    private double mLongitude = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.pavement_activity);
    	
    	mList = (ListView) findViewById(android.R.id.list);
    	
    	mLatitude = getIntent().getDoubleExtra(ConstantValues.EXTRA_LATITUDE,0);
    	mLongitude = getIntent().getDoubleExtra(ConstantValues.EXTRA_LONGITUDE,0);   	
    	
    	mDownloadPavementsTask = new DownloadPavementsTask(this);
        mParsePavementsTask = new ParsePavementsTask(this);   
        
        mProgressDialog = new ProgressDialog(PavementsListActivity.this);
        mProgressDialog.setMessage("Loading comments ...");        
        
        mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// Getting the Container Layout of the ListView
	            LinearLayout linearLayoutParent = (LinearLayout) view;

	            // Getting the reference
	            TextView tvComment = (TextView) linearLayoutParent.getChildAt(0);
	            TextView tvLat = (TextView) linearLayoutParent.getChildAt(1);
	            TextView tvLng = (TextView) linearLayoutParent.getChildAt(2);
	        	
	            String comment = tvComment.getText().toString();
	            String lat = tvLat.getText().toString();
	            String lng = tvLng.getText().toString();
	            
	            
	            
	            Intent intent = new Intent(PavementsListActivity.this, SinglePavementActivity.class);
	            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_COMMENT, comment);
	            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE, lat);
	            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE, lng);
	            intent.putExtra(ConstantValues.EXTRA_LATITUDE,mLatitude);
	            intent.putExtra(ConstantValues.EXTRA_LONGITUDE, mLongitude);
	            
	            startActivity(intent);
			}        	
		});        
        
        // Start downloading pavements from server
        StringBuilder sb = new StringBuilder(ConstantValues.PAVEMENT_GET_URL);       
        startDownloadPavementsTask(sb.toString());  
        
        
    	
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	mDownloadPavementsTask.cancel(true);
    	mParsePavementsTask.cancel(true);
    	
    	mDownloadPavementsTask.detach(); 
    	mParsePavementsTask.detach();
    	
    	super.onSaveInstanceState(outState);
    }
    
	
	private void startDownloadPavementsTask(String url){
    	mDownloadPavementsTask.execute(url);
    }
    
    private void startPavementsParserTask(String data){
    	mParsePavementsTask.execute(data);
    }
    
    private void startProgressDialog(){
    	mProgressDialog.show();
    }
    
    private void endProgressDialog(){
    	mProgressDialog.dismiss();
    }
    
    
    
    private void setListViewData(List<HashMap<String, String>> list){
    	ListAdapter listAdapter = new SimpleAdapter(PavementsListActivity.this, list, 
				R.layout.pavement_list_item, 
				new String[]{
					ConstantValues.EXTRA_PAVEMENT_COMMENT,
					ConstantValues.EXTRA_PAVEMENT_LATITUDE,
					ConstantValues.EXTRA_PAVEMENT_LONGITUDE
					
				}, 
				new int[]{
					R.id.tv_comment,
					R.id.tv_lat,
					R.id.tv_lng
				}
			);    	
    	setListAdapter(listAdapter);
    }
    
	/** A method to download data from url */
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
    
    
    /** A class, to download Pavements Details */
	private static class DownloadPavementsTask extends AsyncTask<String, Integer, String>{	
		
		PavementsListActivity pavementsListActivity;
		public DownloadPavementsTask(PavementsListActivity pavementsListActivity) {
			this.pavementsListActivity = pavementsListActivity;
		}
		
		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			pavementsListActivity.startProgressDialog();
		}
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			String data = null;

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
			pavementsListActivity.startPavementsParserTask(result);			
		}
		
		private void detach(){
			pavementsListActivity = null;
		}
	}
	
	
	/** A class, to download Pavements Details */
	private static class ParsePavementsTask extends AsyncTask<String, Integer, List<HashMap<String, String>>>{	
		
		PavementsListActivity pavementsListActivity;
		
		public ParsePavementsTask(PavementsListActivity pavementsListActivity) {
			this.pavementsListActivity = pavementsListActivity;
		}
		
		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsondata) {
			PavementsJSONParser pavementsJSONParser = new PavementsJSONParser();
			JSONObject json = null;
			try {
				json = new JSONObject(jsondata[0]);
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			 
			List<HashMap<String, String>> pavementsList = pavementsJSONParser.parse(json);
			return pavementsList;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> pavementsList){			
			pavementsListActivity.setListViewData(pavementsList);		
			pavementsListActivity.endProgressDialog();
		}
		
		private void detach(){
			pavementsListActivity = null;
		}		
	}
}
