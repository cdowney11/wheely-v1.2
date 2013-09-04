package com.alkliv.wheely;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GooglePlacesMapActivity extends Activity implements IFaceTaskDownloadAccessibility,
																			 IFaceTaskParseAccessibility,
																			 IFaceTaskDownloadPlaces,
																			 IFaceTaskParsePlaces {
	
    private GoogleMap mGoogleMap;
    
    View mView = null;    
    AlertDialog.Builder mBuilder = null;
    AlertDialog mDialog = null;   
    
    private HashMap<String, String> mHAccessibility;
    
    TaskPostData mTaskPostData = null;						// For posting pavement to server
    TaskDownloadPavements mTaskDownloadPavements = null;	// For downloading pavements from server
    TaskParsePavements mTaskParsePavements = null;			// For parsing pavement json data from server    
    TaskDownloadAccesibility mTaskDownloadAccessibility = null;
    TaskParseAccessibility mTaskParseAccessibility = null;
    TaskDownloadPlaces mTaskDownloadPlaces = null;
    TaskParsePlaces mTaskParsePlaces = null;
    
    private double mLatitude, mLongitude;
    LatLng mPavementLocation = null;		// This is the location at which user long press
    private HashMap<String, String> mHReference;    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_places_map);       
    }
    
    @Override
    protected void onStart() { 	   	
    	
    	super.onStart();
    	
    	initVars();    	
    	
    	mGoogleMap.clear();
        
        mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				String id = marker.getId();
				String markerTitle = marker.getTitle();
				
				if(mHReference.containsKey(id)){	// All nearby place has a reference 
					String reference = mHReference.get(id);
					Intent intent = new Intent(GooglePlacesMapActivity.this, SinglePlaceActivity.class);
		            intent.putExtra(ConstantValues.EXTRA_REFERENCE, reference);
		            startActivity(intent);
				}else if(markerTitle.equals(ConstantValues.PAVEMENT_MARKER_TITLE)){ // Title of all pavements are ConstantValues.PAVEMENT_MARKER_TITLE
					Intent intent = new Intent(GooglePlacesMapActivity.this, SinglePavementActivity.class);
		            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_COMMENT, marker.getSnippet());
		            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE, Double.toString(marker.getPosition().latitude));	// Pavement Latitude
		            intent.putExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE, Double.toString(marker.getPosition().longitude));	// Pavement Longitude
		            intent.putExtra(ConstantValues.EXTRA_LATITUDE,mLatitude);
		            intent.putExtra(ConstantValues.EXTRA_LONGITUDE, mLongitude);		            
		            startActivity(intent);
				}
			}
		});        
      
        Intent intent = getIntent();
        
        // Drawing Current location in Google Map                
        drawCurrentLocation();
        
        // Which activity invoked this activity
        String via = intent.getStringExtra(ConstantValues.EXTRA_MAP_VIA);       
        
        if(via.equals(ConstantValues.EXTRA_MAP_VIA_MAIN_ACTIVITY)){	// From MainActivity
        	showLocation(mLatitude,mLongitude);	// Move camera to current location
        	startTaskDownloadAccessibility();        	       	
        }else if(via.equals(ConstantValues.EXTRA_MAP_VIA_LIST)){	// From AllNearbyPlacesActivity
        	showLocation(mLatitude,mLongitude);	// Move camera to current location
	        Parcelable[] p = intent.getParcelableArrayExtra(ConstantValues.EXTRA_ALL_NEARBY_PLACES);	        
	        Place[] places = new Place[p.length];	     
	        for(int i=0;i<p.length;i++){
	        	places[i] = (Place) p[i];
	        }		        
	        drawNearByPlaces(places);	         
        }else if(via.equals(ConstantValues.EXTRA_MAP_VIA_PAVEMENT_DETAILS)){	// From SinglePavementActivity
        	String lat = getIntent().getStringExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE);
        	String lng = getIntent().getStringExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE);
        	showLocation(Double.parseDouble(lat), Double.parseDouble(lng));   // Move camera to pavement location       	
        	startTaskDownloadAccessibility();        	
        }
        
        // Setting up AlertDialog for entering pavement comment
        setupAlertDialog();
        
        // Start downloading pavements from server              
        startDownloadPavementsTask();
    	
    	
    	
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	mTaskPostData.cancel(true);
    	mTaskDownloadPavements.cancel(true);
    	mTaskParsePavements.cancel(true);    	
    	mTaskDownloadAccessibility.cancel(true);
    	mTaskParseAccessibility.cancel(true);
    	mTaskDownloadPlaces.cancel(true);
    	mTaskParsePlaces.cancel(true);
    	
    	
    	mTaskDownloadPavements.detach(); 
    	mTaskParsePavements.detach();    	
    	mTaskDownloadAccessibility.detach();
    	mTaskParseAccessibility.detach();
    	mTaskDownloadPlaces.detach();
    	mTaskParsePlaces.detach();
    	
    	super.onSaveInstanceState(outState);
    }
    
    
    
    private void initVars(){
    	
    	// Initializing Tasks
        mTaskPostData = new TaskPostData();
        mTaskDownloadPavements = new TaskDownloadPavements(this);
        mTaskParsePavements = new TaskParsePavements(this);        
        mTaskDownloadAccessibility = new TaskDownloadAccesibility(this);
        mTaskParseAccessibility = new TaskParseAccessibility(this);
        mTaskDownloadPlaces = new TaskDownloadPlaces(this);
        mTaskParsePlaces = new TaskParsePlaces(this);
        
        mHAccessibility = new HashMap<String, String>();
        mHReference = new HashMap<String, String>();
        
        
        Intent intent = getIntent();
        
        mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();        
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);             
        
        mLatitude = intent.getDoubleExtra(ConstantValues.EXTRA_LATITUDE,0);
        mLongitude = intent.getDoubleExtra(ConstantValues.EXTRA_LONGITUDE,0);
        
        
    	
    }    
    
    private void showLocation(double lat,double lng){
    	
    	// Showing the current location in Google Map
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

        // Zoom in the Google Map
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }    
    
    
    private void drawCurrentLocation(){
    	LatLng currentLatLng = new LatLng(mLatitude,mLongitude);
    	MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);        
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOptions.title("User.");
        Marker marker = mGoogleMap.addMarker(markerOptions);
        marker.showInfoWindow();
        
    }
    
    
    /**
     * Drawing all the near by places in Google Map
     */
    private void drawNearByPlaces(Place[] places){
    	
    	MarkerOptions markerOptions = new MarkerOptions();
    	
        for(int i=0;i<places.length;i++){       	
        	        		
        	LatLng latlng = new LatLng(Double.parseDouble(places[i].mLat), Double.parseDouble(places[i].mLng));
        	markerOptions = new MarkerOptions();
        	markerOptions.position(latlng);
        	markerOptions.title(places[i].mPlaceName);
        	if(places[i].mAccessibility.equals("0")){
        		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));	// If no accessibility
        	}else if(places[i].mAccessibility.equals("1")){
        		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        	}else if(places[i].mAccessibility.equals("2")){
        		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        	}else if(places[i].mAccessibility.equals("3")){
        		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        	}
        	Marker m = mGoogleMap.addMarker(markerOptions);
        	
        	// Linking place reference with marker id
        	// This mapping is used to return back to place details
        	mHReference.put(m.getId(), places[i].mReference);
        	
        }
    }
    
    private void setupAlertDialog(){
    	
    	mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Add comment");
        mBuilder.setPositiveButton("Add", new OnClickListener() {
        	
        	private void addPavement(String message){    
            	MarkerOptions markerOptions = new MarkerOptions();
            	markerOptions.position(mPavementLocation);
            	//markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_issues));
            	markerOptions.title(ConstantValues.PAVEMENT_MARKER_TITLE);
            	markerOptions.snippet(message);
            	mGoogleMap.addMarker(markerOptions);
            	
            	postData(	
            			Double.toString(mPavementLocation.latitude),
            			Double.toString(mPavementLocation.longitude), 
            			message
            			);            	
            }
        	
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				EditText etPavement = (EditText) mView.findViewById(R.id.et_pavement);
				addPavement(etPavement.getText().toString());
			}
		});
        
        mBuilder.setNegativeButton("Cancel", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				
			}
		});        
        
        mView = getLayoutInflater().inflate(R.layout.pavement_dialog, null);
        mBuilder.setView(mView);		// Clears previously entered data
        mDialog = mBuilder.create();
        
        mGoogleMap.setOnMapLongClickListener(new OnMapLongClickListener() {        	
			@Override
			public void onMapLongClick(LatLng latlng) {
				EditText etPavement = (EditText) mView.findViewById(R.id.et_pavement);
				etPavement.setText("");
				mPavementLocation = latlng;	// Storing the currently long pressed location
				mDialog.show();	
			}			
		});   	
    }
    
    
    private void startDownloadPavementsTask(){    	
    	mTaskDownloadPavements.execute(ConstantValues.PAVEMENT_GET_URL);
    }
    
    public void startPavementsParserTask(String data){
    	mTaskParsePavements.execute(data);
    }
    
    public void showPavement(HashMap<String, String> hPavement){
    	MarkerOptions markerOptions = new MarkerOptions();
    	LatLng latlng = new LatLng(	Double.parseDouble(	hPavement.get(ConstantValues.EXTRA_PAVEMENT_LATITUDE )), 
    								Double.parseDouble(	hPavement.get(ConstantValues.EXTRA_PAVEMENT_LONGITUDE ))
    							);
    	String comment = hPavement.get(ConstantValues.EXTRA_PAVEMENT_COMMENT);
    	
    	markerOptions.position(latlng);
    	markerOptions.title(ConstantValues.PAVEMENT_MARKER_TITLE);
    	markerOptions.snippet(comment);
    	markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));  	
    	
    	Marker marker = mGoogleMap.addMarker(markerOptions);
    	
    	
    	Intent intent = getIntent();    	
    	String lat = intent.getStringExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE);
    	String lng = intent.getStringExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE);    	
    	if(lat!=null){
    		if(lat.equals(hPavement.get(ConstantValues.EXTRA_PAVEMENT_LATITUDE)) && lng.equals(hPavement.get(ConstantValues.EXTRA_PAVEMENT_LONGITUDE)) ){
    			marker.showInfoWindow();
    		}
    	}
    	
    }
    
    
    /** A method to download data from url */
     public String downloadUrl(String strUrl) throws IOException{
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
    
    
    public void postData(final String lat, final String lng, final String msg) {
    	
    	String[] data = new String[] { lat,lng,msg };
    	mTaskPostData = new TaskPostData();
    	
		// Post comments to server
    	mTaskPostData.execute( data );
    }
    
    @Override
	public void preStartTaskDownloadAccessibility() {
		// TODO Auto-generated method stub	
	}
    
	
	@Override
	public void startTaskDownloadAccessibility() {
		mTaskDownloadAccessibility.execute(ConstantValues.ACCESSIBILITY_GET_URL);		
	}
	
	@Override
	public void onEndTaskDownloadAccessibility(String result){
		startTaskParseAccessibility(result);
	}
	
	@Override
	public void startTaskParseAccessibility(String result) {
		mTaskParseAccessibility.execute(result);		
	}	
	
	@Override
	public void onEndTaskParseAccessibility(HashMap<String, String> hAccessibility) {
		this.mHAccessibility = hAccessibility;
		startTaskDownloadPlaces();		
	}

	@Override
	public void startTaskDownloadPlaces() {
		StringBuilder sb = new StringBuilder(ConstantValues.GOOGLE_PLACE_URL);    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());    
    	String radius = prefs.getString("radius_preference", "");
    	
    	String placeType = "none";    	
    	HashMap<String, Boolean> hMap = (HashMap<String, Boolean>)prefs.getAll();   	
    	
    	for(int i=0;i<ConstantValues.PLACE_TYPES.length;i++){
    		String key = "place_type_" + ConstantValues.PLACE_TYPES[i] + "_preference" ; 
    		if(hMap.containsKey(key)){
	    		if(hMap.get(key))
	    			placeType += "|" + ConstantValues.PLACE_TYPES[i] ;
    		}
    	}    	
		sb.append("&radius="+radius + "&location="+ mLatitude+","+ mLongitude + "&types=" +  placeType );		
		mTaskDownloadPlaces.execute(sb.toString());		
	}
	
	@Override
	public void onEndTaskDownloadPlaces(String result) {		
		startTaskParsePlaces(result);
	}	

	@Override
	public void startTaskParsePlaces(String result) {
		// We are passing accessibilities to places parser, so that, the parsed result will 
		// contain accessibility linked places
    	mTaskParsePlaces.setHAccessibility(mHAccessibility);
    	mTaskParsePlaces.execute(result);		
	}
	
	@Override
	public void onEndTaskParsePlaces(List<HashMap<String, String>> list, Place[] placeArray) {
		if(list!=null){
			if(list.size()>0){                        
	            setPlaceArray(placeArray);	            
			}else{
				showPlacesNotFoundMessage();
			}
		}else{
			showPlacesNotFoundMessage();
		}
	}	
	
	public void setPlaceArray(Place[] places) {
		drawNearByPlaces(places);		
	}
	
	public void showPlacesNotFoundMessage() {
		Toast.makeText(getApplicationContext(), "No places found", Toast.LENGTH_LONG).show();		
	}
	
}