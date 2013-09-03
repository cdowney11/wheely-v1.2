package com.alkliv.wheely;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements android.location.LocationListener {
    private TextView textViewLocation;
    private Button buttonSearchNearby;
    
    private Location mLocation;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Setting default values for preferences from the file R.xml.prefs_radius
        // false : should not read again. Set the default value only once.
        PreferenceManager.setDefaultValues(this, R.xml.prefs_radius,false);
        

        initializeComponent();
        // initializeVariables(); commented by GMK
        ActionBar ab = getActionBar(); 
		ab.setDisplayShowTitleEnabled(false);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());		
		
		if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	        int requestCode = 10;
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();

		}else { // Google Play Services are available
			
			// Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);
            
            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            
		}
		
    }
    
    
    @Override
    public void onLocationChanged(Location location) {
    	textViewLocation.setText(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
        buttonSearchNearby.setEnabled(true);     
        mLocation = location;
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.welcome, menu);
        return true;
    }
    
    
    private void initializeComponent() {
        textViewLocation = (TextView)findViewById(R.id.textViewLocation);
        buttonSearchNearby = (Button)findViewById(R.id.buttonSearchNearby);
        buttonSearchNearby.setOnClickListener(buttonSearchNearbyOnClickListener);
        buttonSearchNearby.setEnabled(false);
    }

    private View.OnClickListener buttonSearchNearbyOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AllNearbyPlacesActivity.class );
            intent.putExtra(ConstantValues.EXTRA_LATITUDE, mLocation.getLatitude());
            intent.putExtra(ConstantValues.EXTRA_LONGITUDE,mLocation.getLongitude());
            startActivity(intent);
        }
    };
    
    
    @Override
    protected void onDestroy() {
        // gpsUtil.stop(); // Commented by GMK
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }    
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.places_map:
	        	Intent intentMap = new Intent(this,GooglePlacesMapActivity.class);
	        	intentMap.putExtra(ConstantValues.EXTRA_LATITUDE, mLocation.getLatitude());
	        	intentMap.putExtra(ConstantValues.EXTRA_LONGITUDE,mLocation.getLongitude());
	        	intentMap.putExtra(ConstantValues.EXTRA_MAP_VIA,ConstantValues.EXTRA_MAP_VIA_MAIN_ACTIVITY);
	        	startActivity(intentMap);
	        	
	            return true;
	            
	        case R.id.places_nearby:
	            Intent intent_list = new Intent(this, AllNearbyPlacesActivity.class );
	            intent_list.putExtra(ConstantValues.EXTRA_LATITUDE, mLocation.getLatitude());
	            intent_list.putExtra(ConstantValues.EXTRA_LONGITUDE,mLocation.getLongitude());
	            startActivity(intent_list);
	            
	            return true;
	            
	        case R.id.pavements:
	        	Intent intentPavementsList = new Intent(this,PavementsListActivity.class);
	        	intentPavementsList.putExtra(ConstantValues.EXTRA_LATITUDE, mLocation.getLatitude());
	            intentPavementsList.putExtra(ConstantValues.EXTRA_LONGITUDE,mLocation.getLongitude());
	        	startActivity(intentPavementsList);
	            return true;
	            
	        case R.id.action_settings:
	        	Intent intentNearbyPreferenceActivity = new Intent(this,NearbyPreferenceActivity.class);
	        	startActivity(intentNearbyPreferenceActivity);
	        	return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}	

	
		
	
	
}
