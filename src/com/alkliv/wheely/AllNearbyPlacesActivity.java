package com.alkliv.wheely;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alkliv.wheely.vo.GooglePlace;
import com.google.gson.Gson;
import com.hintdesk.core.utils.JSONHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/11/13
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllNearbyPlacesActivity extends ListActivity {

    private double latitude, longitude;
    private GooglePlace[] googlePlaces = null;
    private Button buttonShowOnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.all_nearby_places);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ConstantValues.EXTRA_LATITUDE, 0);
        longitude = intent.getDoubleExtra(ConstantValues.EXTRA_LONGITUDE, 0);
        initializeComponent();
        new GetGooglePlacesInfoTask().execute(latitude, longitude);
    }

    private void initializeComponent() {
        ListView listView = getListView();
        listView.setOnItemClickListener(listViewOnItemClickListener);

        buttonShowOnMaps = (Button)findViewById(R.id.buttonShowOnMaps);
        buttonShowOnMaps.setOnClickListener(buttonShowOnMapsOnClickListener);
    }

    private View.OnClickListener buttonShowOnMapsOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), GooglePlacesMapActivity.class);
            intent.putExtra(ConstantValues.EXTRA_LATITUDE, latitude );
            intent.putExtra(ConstantValues.EXTRA_LONGITUDE,longitude);
            intent.putExtra(ConstantValues.EXTRA_ALL_NEARBY_PLACES,  new Gson().toJson(googlePlaces) );
            startActivity(intent);
        }
    };

    private AdapterView.OnItemClickListener listViewOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String reference = ((TextView) findViewById(R.id.textViewReference)).getText().toString();
            Intent intent = new Intent(AllNearbyPlacesActivity.this, SinglePlaceActivity.class);
            intent.putExtra(ConstantValues.EXTRA_REFERENCE, reference);
            startActivity(intent);
        }
    };

    class GetGooglePlacesInfoTask extends AsyncTask<Double, String, String> {

        @Override
        protected void onPostExecute(String s) {
            ArrayList<HashMap<String, String>> googlePlaceList = new ArrayList<HashMap<String, String>>();

            if (googlePlaces != null) {
                for (GooglePlace googlePlace : googlePlaces) {
                    HashMap<String, String> mapGooglePlace = new HashMap<String, String>();
                    String field = ConstantValues.FIELD_REFERENCE;
                    String value = googlePlace.getReference();
                    mapGooglePlace.put(field, value);
                    mapGooglePlace.put(ConstantValues.FIELD_NAME, googlePlace.getName());
                    googlePlaceList.add(mapGooglePlace);
                }
            }
            ListAdapter listAdapter = new SimpleAdapter(AllNearbyPlacesActivity.this, googlePlaceList, R.layout.list_item, new String[]{ConstantValues.FIELD_REFERENCE, ConstantValues.FIELD_NAME}, new int[]{R.id.textViewReference, R.id.textViewName});
            setListAdapter(listAdapter);
        }

        @Override
        protected String doInBackground(Double... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(params[0])));
            nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(params[1])));
            nameValuePairs.add(new BasicNameValuePair("radius", "2000"));
            nameValuePairs.add(new BasicNameValuePair("types", "cafe|restaurant|store"));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            googlePlaces = jsonHttpClient.Get(ConstantValues.GOOGLE_PLACES_URL, nameValuePairs, GooglePlace[].class);
            return null;
        }
    }
}
