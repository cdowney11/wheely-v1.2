package com.alkliv.wheely;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alkliv.wheely.AllNearbyPlacesActivity.GetGooglePlacesInfoTask;
import com.alkliv.wheely.vo.GooglePlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapsAndPlacesActivity extends Activity {
    private GoogleMap googleMap;
    private double latitude, longitude;
    private GooglePlace[] googlePlaces = null;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.google_places_map);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ConstantValues.EXTRA_LATITUDE,0);
        longitude = intent.getDoubleExtra(ConstantValues.EXTRA_LONGITUDE,0);
        
        AllNearbyPlacesActivity myActivity2 = new AllNearbyPlacesActivity();
        
        googlePlaces = new Gson().fromJson(intent.getStringExtra(ConstantValues.EXTRA_ALL_NEARBY_PLACES), GooglePlace[].class);

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setOnCameraChangeListener(GoogleMapOnCameraChangeListener);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng currentLatLng = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markergreen));
        markerOptions.title("Your current position");
        googleMap.addMarker(markerOptions);


        for(GooglePlace googlePlace:googlePlaces)
        {
            currentLatLng = new LatLng(googlePlace.getGeometry().getLocation().getLat(),googlePlace.getGeometry().getLocation().getLng());
            builder.include(currentLatLng);
            markerOptions = new MarkerOptions();
            markerOptions.position(currentLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerred));
            markerOptions.title(googlePlace.getName());
            googleMap.addMarker(markerOptions);
        }


    }

    private GoogleMap.OnCameraChangeListener GoogleMapOnCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),10));
            googleMap.setOnCameraChangeListener(null);
        }
    };
}
