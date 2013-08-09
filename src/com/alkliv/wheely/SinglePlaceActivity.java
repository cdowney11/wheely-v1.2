package com.alkliv.wheely;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.single_place);

        new LoadPlaceDetailsTask().execute(getIntent().getStringExtra(ConstantValues.EXTRA_REFERENCE));
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
    }
}
