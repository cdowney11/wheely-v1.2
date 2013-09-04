package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class AllNearbyPlacesActivity extends Activity implements IFaceTaskDownloadAccessibility, 
																			 IFaceTaskParseAccessibility,
																			 IFaceTaskDownloadPlaces,
																			 IFaceTaskParsePlaces{

	public String TAG = "alkis";
    private double mLatitude, mLongitude;
    private Button mButtonShowOnMaps;
    private ProgressDialog mProgressDialog;
    private HashMap<String, String> mHAccessibility;
    
    private TaskDownloadAccesibility mTaskDownloadAccessibility;    
    private TaskParseAccessibility mTaskParseAccessibility;
    private TaskDownloadPlaces mTaskDownloadPlaces;
    private TaskParsePlaces mTaskParsePlaces;
    private ListView mList;    
    
    private Place mPlaces[] ;
    
    
    public void setPlaceArray(Place[] places){    	
    	mPlaces = places;
    }   
    
    public void startProgressDialog(){
    	mProgressDialog.show();
    }
    
    
    public void endProgressDialog(){
    	mProgressDialog.dismiss();
    }
    
    public void setListViewData(List<HashMap<String,String>> list){
    	ListAdapter listAdapter = new SimpleAdapter(AllNearbyPlacesActivity.this, list, 
				R.layout.list_item, 
				new String[]{
					ConstantValues.EXTRA_REFERENCE, 
					ConstantValues.EXTRA_ACCESSIBILITY_ICON,
					ConstantValues.EXTRA_NAME
				}, 
				new int[]{
					R.id.textViewReference, 
					R.id.img_accessibility_icon,
					R.id.textViewName
				}
			);   	
    	mList.setAdapter(listAdapter);
    }
    
    
    public void showPlacesNotFoundMessage(){
    	Toast.makeText(getApplicationContext(), "No places found", Toast.LENGTH_LONG).show();   	
    }
    
    @Override
	public void preStartTaskDownloadAccessibility() {		
    	startProgressDialog();
	}
    
    @Override
    public void startTaskDownloadAccessibility(){
    	mTaskDownloadAccessibility.execute(ConstantValues.ACCESSIBILITY_GET_URL);
    }
    
    @Override
    public void onEndTaskDownloadAccessibility(String result) {   	
    	startTaskParseAccessibility(result);
    }
    
    @Override
    public void startTaskParseAccessibility(String data){
    	mTaskParseAccessibility.execute(data);
    }
    
    @Override
    public void onEndTaskParseAccessibility(	HashMap<String, String> hAccessibility) {
    	this.mHAccessibility = hAccessibility;
    	startTaskDownloadPlaces();    	
    }  
    
    
    @Override
    public void startTaskDownloadPlaces(){
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
    public void startTaskParsePlaces(String data){    	
    	// We are passing accessibilities to places parser, so that, the parsed result will 
    	// contain accessibility linked places     
    	mTaskParsePlaces.setHAccessibility(mHAccessibility);
    	
    	// Start Parsing
    	mTaskParsePlaces.execute(data);
    }
    
    @Override
    public void onEndTaskParsePlaces(List<HashMap<String, String>> list, Place[] placeArray) {    	
    	if(list!=null){
			if(list.size()>0){				
	            setListViewData(list);            
	            setPlaceArray(placeArray);	            
			}else{
				showPlacesNotFoundMessage();
			}            
		}else{
			showPlacesNotFoundMessage();
		}
        endProgressDialog();
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {  	    	
    	
    	mTaskDownloadAccessibility.cancel(true);
    	mTaskParseAccessibility.cancel(true);
    	mTaskDownloadPlaces.cancel(true);
    	mTaskParsePlaces.cancel(true);

    	mTaskParsePlaces.detach();
		mTaskDownloadPlaces.detach();
    	mTaskParseAccessibility.detach();
    	mTaskDownloadAccessibility.detach();
    	
    	super.onSaveInstanceState(outState);   	
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);    
        setContentView(R.layout.all_nearby_places);
        
        mList = (ListView) findViewById(android.R.id.list);
        
        initializeComponent();
        ActionBar ab = getActionBar(); 
		ab.setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ab.setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        
    }   
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	initializeTasks();
    	
    	// Starting AccessibilityDownloadTask
    	startTaskDownloadAccessibility();
    }
    
    
    
    private void initializeTasks(){
    	// Creating a new non-ui thread task to download accessibilities from server
    	mTaskDownloadAccessibility = new TaskDownloadAccesibility(this);
        
        // Creating a new non-ui thread task to parse accessibilities
    	mTaskParseAccessibility = new TaskParseAccessibility(this);
        
        // Creating a new non-ui thread task to download places
        mTaskDownloadPlaces = new TaskDownloadPlaces(this);
        
        // Creating a new non-ui thread task to parse places
        mTaskParsePlaces = new TaskParsePlaces(this);
    	
    }
    
    
    private void initializeComponent() {
    	
    	Intent intent = getIntent();
        mLatitude = intent.getDoubleExtra(ConstantValues.EXTRA_LATITUDE, 0);
        mLongitude = intent.getDoubleExtra(ConstantValues.EXTRA_LONGITUDE, 0);
    	
        mHAccessibility = new HashMap<String, String>();
        
        //ListView listView = getListView();
        mList.setOnItemClickListener(listViewOnItemClickListener);

        mProgressDialog = new ProgressDialog(AllNearbyPlacesActivity.this);
        mProgressDialog.setMessage("Αναζήτηση γύρω σημείων...");        
        
        mButtonShowOnMaps = (Button)findViewById(R.id.buttonShowOnMaps);
        
        
        // Event handler to show all places in Google Maps
        mButtonShowOnMaps.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(mPlaces!=null){
					if(mPlaces.length>0){
						Intent intent = new Intent(getApplicationContext(), GooglePlacesMapActivity.class);
			            intent.putExtra(ConstantValues.EXTRA_LATITUDE, mLatitude );
			            intent.putExtra(ConstantValues.EXTRA_LONGITUDE,mLongitude);   
			            intent.putExtra(ConstantValues.EXTRA_ALL_NEARBY_PLACES, mPlaces);     
			            intent.putExtra(ConstantValues.EXTRA_MAP_VIA, ConstantValues.EXTRA_MAP_VIA_LIST);
			            startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "No places found", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "No places found", Toast.LENGTH_LONG).show();
				}
			}
		});
    }    
	    
    private AdapterView.OnItemClickListener listViewOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {       	
        	
        	// Getting the Container Layout of the ListView
            LinearLayout linearLayoutParent = (LinearLayout) view;

            // Getting the reference
            TextView tvReference = (TextView) linearLayoutParent.getChildAt(0);
        	
            String reference = tvReference.getText().toString();
            
            Intent intent = new Intent(AllNearbyPlacesActivity.this, SinglePlaceActivity.class);
            intent.putExtra(ConstantValues.EXTRA_REFERENCE, reference);
            startActivity(intent);
        }
    };
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}