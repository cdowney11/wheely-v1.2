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
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

class TaskPostData extends AsyncTask<String, Void, Void>{	
	
	// Invoked by execute() method of this object
	@Override
	protected Void doInBackground(String... data) {
		String lat = data[0];
		String lng = data[1];
		String msg = data[2];
		
		try {
        	// Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ConstantValues.PAVEMENT_INSERT_URL);
            try {
            	
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("lat", lat));
                nameValuePairs.add(new BasicNameValuePair("lng", lng));
                nameValuePairs.add(new BasicNameValuePair("comment", msg));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                HttpResponse response = httpclient.execute(httppost);
                String result = EntityUtils.toString(response.getEntity());                   
                
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
}