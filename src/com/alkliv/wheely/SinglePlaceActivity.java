package com.alkliv.wheely;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alkliv.wheely.vo.GooglePlaceDetails;
import com.hintdesk.core.utils.JSONHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePlaceActivity extends Activity {
    private ProgressDialog progressDialog;
    public String TAG = "alkis";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.single_place);
        Log.d(TAG, "after setContentView");
        new LoadPlaceDetailsTask().execute(getIntent().getStringExtra(ConstantValues.EXTRA_REFERENCE));
        
        final ImageButton btn1=(ImageButton)findViewById(R.id.btn_1);
        final ImageButton btn2=(ImageButton)findViewById(R.id.btn_2);
        final Button button = (Button) findViewById(R.id.button_sd);
        Log.d(TAG, "Before setOnClickListener");
        
        button.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View v) {
            	//Perform an action on click
            	Log.d(TAG, "inside onClick(View v)");
            	postData();
            	Log.d(TAG, "after calling postData()");
            }
        }); //end of button set on click


        btn1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Your Code Here....
            	Log.d(TAG, "inside on SECOND onClick(View v)");
            	Toast toast = Toast.makeText(getApplicationContext(), "Σημειώσατε το μέρος ως μη προσεγγίσιμο. :-(",  Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        
        btn2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Your Code Here....
            	Log.d(TAG, "inside on THIRD onClick(View v)");
            	Toast toast = Toast.makeText(getApplicationContext(), "Σημειώσατε το μέρος ως προσεγγίσιμο! :-)",  Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    
    }
    
   public void postData() {
        
            Log.d(TAG, "just got inside postData()");
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                	Log.d(TAG, "just got inside run()");
                    try {
                    	Log.d(TAG, "just got inside run's TRY");
                    	// Create a new HttpClient and Post Header
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://46.105.49.245/androidconnector/insertplace.php");

                        try {
                        	Log.d(TAG, "try adding DATA - list value pairs");
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("placeid", "155"));
                            nameValuePairs.add(new BasicNameValuePair("accessibility", "3"));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        //Your code goes here
                    	 // Execute HTTP Post Request
                            Log.d(TAG, "just before response");
                        HttpResponse response = httpclient.execute(httppost);
                        Log.d(TAG, "after response");
                        
                        //response.getEntity();
                        //Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) httppost.getEntity(),  Toast.LENGTH_SHORT);
                        //toast.show();
                        
                        } catch (ClientProtocolException e) {
                            // TODO Auto-generated catch block
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.d(TAG, "before start thread");
            thread.start(); 
            Log.d(TAG, "after start thread");

    } 

    class LoadPlaceDetailsTask extends AsyncTask<String, String, GooglePlaceDetails> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(SinglePlaceActivity.this);
            progressDialog.setMessage("Loading details...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(GooglePlaceDetails googlePlaceDetails) {
            String name,address,phone,latitude,longitude;

            name = address = phone=latitude = longitude = "not available";
            TextView textViewName = (TextView) findViewById(R.id.textViewName);
            TextView textViewAddress = (TextView) findViewById(R.id.textViewAddress);
            TextView textViewPhone = (TextView) findViewById(R.id.textViewPhone);
            TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);

            if (googlePlaceDetails != null) {

                name = googlePlaceDetails.getName();
                address = googlePlaceDetails.getFormatted_Address();
                phone = googlePlaceDetails.getFormatted_Phone_Number();
                latitude = String.valueOf(googlePlaceDetails.getGeometry().getLocation().getLat());
                longitude = String.valueOf(googlePlaceDetails.getGeometry().getLocation().getLng());
            }
            textViewName.setText(name);
            textViewAddress.setText(address);
            textViewPhone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
            textViewLocation.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
            progressDialog.dismiss();
            super.onPostExecute(googlePlaceDetails);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected GooglePlaceDetails doInBackground(String... params) {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("reference", params[0]));
            return jsonHttpClient.Get(ConstantValues.GOOGLE_PLACES_URL, nameValuePairs, GooglePlaceDetails.class);
        }
        
        public void selfDestruct(View view) {
            // Kabloey
        }
        
        
    }
    
    
    
}

