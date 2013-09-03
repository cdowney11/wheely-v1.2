package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SinglePavementActivity extends Activity {
	
	private String mPavementLatitude;
	private String mPavementLongitude;
	
	private double mLatitude = 0;
	private double mLongitude = 0;
	
	private TaskDownloadGeocode mTaskDownloadGeocode;
	private TaskParseGeocode mTaskParseGeocode;
	private TextView mTvComment;
	private TextView mTvAddress;
	private TextView mTvLocation;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_pavement);
		
		mPavementLatitude = getIntent().getStringExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE);
		mPavementLongitude = getIntent().getStringExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE);
		
		mLatitude = getIntent().getDoubleExtra(ConstantValues.EXTRA_LATITUDE,0);
		mLongitude = getIntent().getDoubleExtra(ConstantValues.EXTRA_LONGITUDE,0);
		
		String comment = getIntent().getStringExtra(ConstantValues.EXTRA_PAVEMENT_COMMENT);
		
		setComment(comment);
		setLatLng(mPavementLatitude, mPavementLongitude);		
		startTaskDownloadGeocode(mPavementLatitude,mPavementLongitude);	// For Location Address
		
		Button mBtnShow = (Button) findViewById(R.id.btn_pavement_show_in_map);
		
		
		
		mBtnShow.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(SinglePavementActivity.this, GooglePlacesMapActivity.class);
				mapIntent.putExtra(ConstantValues.EXTRA_PAVEMENT_LATITUDE, mPavementLatitude);
				mapIntent.putExtra(ConstantValues.EXTRA_PAVEMENT_LONGITUDE, mPavementLongitude);
				mapIntent.putExtra(ConstantValues.EXTRA_LATITUDE, mLatitude);
				mapIntent.putExtra(ConstantValues.EXTRA_LONGITUDE, mLongitude);
				mapIntent.putExtra(ConstantValues.EXTRA_MAP_VIA, ConstantValues.EXTRA_MAP_VIA_PAVEMENT_DETAILS);
				startActivity(mapIntent);
			}
		});		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mTaskDownloadGeocode.cancel(true);
		mTaskParseGeocode.cancel(true);
		
		mTaskDownloadGeocode.detach();
		mTaskParseGeocode.detach();		
		
		super.onSaveInstanceState(outState);
	}
	
	
	protected void startTaskDownloadGeocode(String lat,String lng){		
		mTaskDownloadGeocode = new TaskDownloadGeocode(this);
		mTaskDownloadGeocode.execute(new String[] { lat,lng});
	}
	
	/**
	 * This method will be executed at the end of TaskDownloadGeocode
	 */
	protected void onEndTaskDownloadGeocode(String result){	
		startTaskParseGeocode(result);		
	}
	
	protected void startTaskParseGeocode(String data){
		mTaskParseGeocode = new TaskParseGeocode(this);
		mTaskParseGeocode.execute(data);		
	}
	
	/**
	* This method will be executed at the end of TaskParseGeocode
	*/
	protected void onEndTaskParseGeocode(List<HashMap<String, String>> list){
		String formattedAddress="";
		
		/**
		 * Geocode api may return multiple addresses corresponding to a latlng.
		 * So here, we are taking an address that is max.length
		 */
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("formatted_address").toString().length() > formattedAddress.length()  ){
				formattedAddress = list.get(i).get("formatted_address").toString();
			}			
		}
		setAddress(formattedAddress);	
	}
	
	protected void setComment(String comment){
		mTvComment = (TextView) findViewById(R.id.tv_comment);
		if(comment!=null)
			mTvComment.setText(comment);	
	}
	
	protected void setLatLng(String lat,String lng){
		mTvLocation = (TextView) findViewById(R.id.tv_location);
		mTvLocation.setText(Html.fromHtml("<b>Latitude:</b> " + lat + ", <b>Longitude:</b> " + lng));		
	}
	
	protected void setAddress(String address){
		mTvAddress = (TextView) findViewById(R.id.tv_address);
		mTvAddress.setText(address);
	}
}