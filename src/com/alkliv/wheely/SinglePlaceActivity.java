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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * 
 * Flow : mPlacesTask.execute(sb.toString()) { Download placedetails } => startParsing() { Parsing placedetails } =>  startImageDownload()
 * The above flow() is invoked from onCreate()
 */


public class SinglePlaceActivity extends Activity {
    private ProgressDialog mProgressDialog;
    private ImageButton mImgBtnRed;		// Red Button
    private ImageButton mImgBtnGreen;	// Green Button
    private ImageButton mImgBtnOrange;	// Orange Button 
    private ImageView mImgPhoto;		// Place Photo
    
    private String mPlaceId = "";
    private String mAccessibilityMessage = "";    
    
    public String TAG = "alkis";    
    
    private PlacesTask mPlacesTask = null;
    private ParserTask mParserTask = null;
    private ImageDownloadTask mImageDownloadTask = null;
    private PostDataTask mPostDataTask = null;
    
    
    private void setPhoto(Bitmap bitmap){    	
    	mImgPhoto.setImageBitmap(bitmap);
    }
    
    
    private void setName(String name){
    	TextView textViewName = (TextView) findViewById(R.id.textViewName);
    	textViewName.setText(name);        
    }
    
    private void setAddress(String address){
    	TextView textViewAddress = (TextView) findViewById(R.id.textViewAddress);
    	textViewAddress.setText(address);        
    }
    
    private void setPhone(String phone){
    	TextView textViewPhone = (TextView) findViewById(R.id.textViewPhone);
    	textViewPhone.setText(Html.fromHtml("<b>Phone:</b> " + phone));        
    }
    
    
    private void setLocation(String lat, String lng){
    	TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
    	textViewLocation.setText(Html.fromHtml("<b>Latitude:</b> " + lat + ", <b>Longitude:</b> " + lng));        
    }
    
    private void setRating(String rating){
    	TextView textViewRating = (TextView) findViewById(R.id.textViewRating);
    	textViewRating.setText(Html.fromHtml("<b>Rating:</b>" + rating ));
    }
    
    private void setPlaceId(String placeid){
    	mPlaceId = placeid;
    }
    
    private String getPlaceId(){
    	return mPlaceId;
    }
    
    private void startProgressDialog(){
    	mProgressDialog.show();
    }
    
    private void endProgressDialog(){
    	mProgressDialog.dismiss();
    }
    
    private void showAccessibilityPostCompletedMessage(){
    	Toast.makeText(getApplicationContext(), mAccessibilityMessage , Toast.LENGTH_SHORT).show();
    }    
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	mPlacesTask.cancel(true);
    	mParserTask.cancel(true);
    	mImageDownloadTask.cancel(true);
    	mPostDataTask.cancel(true);    	
    	
    	mPlacesTask.detach();
    	mParserTask.detach();
    	mImageDownloadTask.detach();
    	mPostDataTask.detach();    	
    	
    	super.onSaveInstanceState(outState);   	
    }
    
    
    private void startParsing(String data){
    	mParserTask.execute(data);
    }
    
    private void startImageDownload(String reference){
    	mImageDownloadTask.execute(reference);
    }
    
    private void initizializeTasks(){
    	
    	// Creating a new non-ui thread task to download Google place details 
        mPlacesTask = new PlacesTask(this);
        
        // Creating a new non-ui thread task to parse place details
        mParserTask = new ParserTask(this);
        
        // Creating a new non-ui thread task to download photo
        mImageDownloadTask = new ImageDownloadTask(this);        
        
        // Creating a new non-ui thread task to post accessibility data to server
        mPostDataTask = new PostDataTask(this);
    	
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.single_place);
        
        
        // Getting reference to RedButton
        mImgBtnRed = (ImageButton) findViewById(R.id.btn_red);
        
        // Getting reference to GreenButton
        mImgBtnGreen = (ImageButton) findViewById(R.id.btn_green);
        
        // Getting reference to OrangeButton
        mImgBtnOrange = (ImageButton) findViewById(R.id.btn_orange);
        
        // Getting reference to ImageView for PlacePhoto
        mImgPhoto = (ImageView) findViewById(R.id.img_photo);
        
        mProgressDialog = new ProgressDialog(SinglePlaceActivity.this);
        mProgressDialog.setMessage("Loading details...");   
        
               
        
        // Adding click event listener for RedButton
        mImgBtnRed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!mPlaceId.equals("")){
					
					// One task can be executed only once, so need to reinitialize
					initizializeTasks();
					
					postData(mPlaceId, "1"); // Sending accessibility options to server	
					mAccessibilityMessage = "Accessibility option RED is set";
				}
				else
					Toast.makeText(getApplicationContext(),"Error in Place ID", Toast.LENGTH_LONG).show();
			}
		});
        
        
        // Adding click event listener for GreenButton
        mImgBtnGreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!mPlaceId.equals("")){
					// One task can be executed only once, so need to reinitialize
					initizializeTasks();
					
					postData(mPlaceId, "2"); // Sending accessibility options to server
					mAccessibilityMessage = "Accessibility option GREEN is set";
				}
				else
					Toast.makeText(getApplicationContext(),"Error in Place ID", Toast.LENGTH_LONG).show();
			}
		});
        
        
        // Adding click event listener for OrangeButton
        mImgBtnOrange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!mPlaceId.equals("")){
					// One task can be executed only once, so need to reinitialize
					initizializeTasks();
					
					postData(mPlaceId, "3"); // Sending accessibility options to server
					mAccessibilityMessage = "Accessibility option ORANGE is set";
				}				
				else
					Toast.makeText(getApplicationContext(),"Error in Place ID", Toast.LENGTH_LONG).show();
			}
		});
              
        
        // Getting place reference from the map	
        String reference = getIntent().getStringExtra(ConstantValues.EXTRA_REFERENCE);
     		
     		
        StringBuilder sb = new StringBuilder(ConstantValues.GOOGLE_PLACE_DETAILS_URL);
     	sb.append("&reference="+reference);     		
     	
     	// Initialize all AsyncTasks
     	initizializeTasks();
             
     	// Invokes the "doInBackground()" method of the class PlaceTask
        mPlacesTask.execute(sb.toString());       
        
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
    
    
    /** A class, to download Google Place Details */
	private static class PlacesTask extends AsyncTask<String, Integer, String>{	
		
		SinglePlaceActivity placeActivity;
		public PlacesTask(SinglePlaceActivity placeActivity) {
			this.placeActivity = placeActivity;
		}
		
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            placeActivity.startProgressDialog();
        }		

		String data = null;
		
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
			// Start parsing the Google place details in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			placeActivity.startParsing(result);
		}
		
		private void detach(){
			placeActivity = null;
		}	
	}
	
	/** A class to parse the Google Place Details in JSON format */
	private static class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

		JSONObject jObject;		
		SinglePlaceActivity placeActivity;
		
		public ParserTask(SinglePlaceActivity placeActivity) {
			this.placeActivity = placeActivity;
			
		}
		
		// Invoked by execute() method of this object
		@Override
		protected HashMap<String,String> doInBackground(String... jsonData) {
		
			HashMap<String, String> hPlaceDetails = null;
			PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);	        	
	        	
	            // Start parsing Google place details in JSON format
	            hPlaceDetails = placeDetailsJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return hPlaceDetails;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(HashMap<String,String> hPlaceDetails){			
			
			
			String name = hPlaceDetails.get(ConstantValues.EXTRA_NAME);			
			String lat = hPlaceDetails.get(ConstantValues.EXTRA_LATITUDE);
			String lng = hPlaceDetails.get(ConstantValues.EXTRA_LONGITUDE);
			String formatted_address = hPlaceDetails.get(ConstantValues.EXTRA_FORMATTED_ADDRESS);
			String formatted_phone = hPlaceDetails.get(ConstantValues.EXTRA_FORMATTED_PHONE_NUMBER);
			String rating = hPlaceDetails.get(ConstantValues.EXTRA_RATING);
			String photoReference = hPlaceDetails.get(ConstantValues.EXTRA_PHOTO_REFERENCE);			
			String placeId = hPlaceDetails.get(ConstantValues.EXTRA_PLACE_ID);			
            
			placeActivity.setName(name);
			placeActivity.setAddress(formatted_address);
			placeActivity.setPhone(formatted_phone);
			placeActivity.setRating(rating);
			placeActivity.setLocation(lat, lng);
			placeActivity.setPlaceId(placeId);     
               
            
            if(!photoReference.equals("")){ 	
            	// Download and display Image in the ImageView
            	placeActivity.startImageDownload(photoReference);	   
            	
	            // new ImageDownloadTask(placeActivity).execute(photoReference);			
            }
            
			placeActivity.endProgressDialog();		
		}
		
		private void detach(){
			placeActivity = null;
		}
	}
	
	
	private static class PostDataTask extends AsyncTask<String, Void, Void>{
		
		private SinglePlaceActivity placeActivity;
		public PostDataTask(SinglePlaceActivity placeActivity) {
			this.placeActivity = placeActivity;
		}
		
		// Invoked by execute() method of this object
		@Override
		protected Void doInBackground(String... data) {
			String placeidvar = data[0];
			String accessin = data[1];
			
			try {
            	// Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ConstantValues.ACCESSIBILITY_INSERT_URL);
                try {
                	
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("placeid", placeidvar));
                    nameValuePairs.add(new BasicNameValuePair("accessibility", accessin));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
			return null;			
			
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(Void nodata){			
			placeActivity.showAccessibilityPostCompletedMessage();
		}
		
		private void detach(){
			placeActivity = null;
		}
		
	}
    
	public void postData(final String placeidvar, final String accessin) {
		// Post accessibility data to server
		mPostDataTask.execute(new String[] { placeidvar,accessin } );
	}
	
	private static Bitmap downloadImage(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
		    URL url = new URL(strUrl);
		
		    /** Creating an http connection to communcate with url */
		    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		
		    /** Connecting to url */
		    urlConnection.connect();
		
		    /** Reading data from url */
		    iStream = urlConnection.getInputStream();
		
		    /** Creating a bitmap from the stream returned from the url */
		    bitmap = BitmapFactory.decodeStream(iStream);
	
        }catch(Exception e){
        	Log.d("Exception while downloading url", e.toString());
        }finally{
        	iStream.close();
        }
        return bitmap;
	}

	
	 private static class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap>{
	        Bitmap bitmap = null;
	        
	        SinglePlaceActivity placeActivity;
	        
	        public ImageDownloadTask(SinglePlaceActivity placeActivity) {
	        	this.placeActivity = placeActivity;
			}
	        
	        
	        @Override
	        protected Bitmap doInBackground(String... reference) {
	            try{
	            	
	            	DisplayMetrics dm = new DisplayMetrics();
	            	
		            // Getting the screen display metrics	            	
		            placeActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);       	            
		            
		            int width = (int)(dm.widthPixels*3)/4;
		            int height = (int)(dm.heightPixels*1)/2;		
		            
		            String maxWidth="maxwidth=" + width;
		            String maxHeight = "maxheight=" + height;		            
		            String photoReference = "&" + "photoreference=" + reference[0];
		            
		            // URL for downloading the photo from Google Services
		            String url = ConstantValues.GOOGLE_PHOTO_URL + "&" + maxWidth + "&" + maxHeight + "&" + photoReference;	            
		        	
	                // Start image downloading
	                bitmap = downloadImage(url);
	                
	            }catch(Exception e){
	                Log.d("Background Task",e.toString());
	            }
	            return bitmap;
	        }

	        @Override
	        protected void onPostExecute(Bitmap result) {    
	        	if(result!=null)
	        		placeActivity.setPhoto(result);
	        }
	        
	        private void detach(){
				placeActivity = null;
			}	        
	    }    
}
