package com.alkliv.wheely;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hintdesk.core.CoreConstants;
import com.hintdesk.core.listeners.GPSUtilLocationListener;
import com.hintdesk.core.utils.GPSUtil;

public class MainActivity extends Activity {
    private GPSUtil gpsUtil = null;
    private TextView textViewLocation;
    private Button buttonSearchNearby;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeComponent();
        initializeVariables();
    }

    private void initializeVariables() {
        gpsUtil = new GPSUtil(MainActivity.this);
        gpsUtil.setOnLocationListener(LocationListener);

        if (!gpsUtil.isGPSProviderEnabled() || !gpsUtil.isNetworkProviderEnabled())
        {
            RedirectToLocationServiceSetting();
        }

        gpsUtil.start();
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
            intent.putExtra(ConstantValues.EXTRA_LATITUDE, gpsUtil.getLocation().getLatitude());
            intent.putExtra(ConstantValues.EXTRA_LONGITUDE,gpsUtil.getLocation().getLongitude());
            startActivity(intent);
                }
    };


    private GPSUtilLocationListener LocationListener = new GPSUtilLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            textViewLocation.setText(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
            buttonSearchNearby.setEnabled(true);
        }
    };

    private void RedirectToLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location service not available")
                .setMessage("To use this demo app:" + CoreConstants.ENVIRONMENT_NEWLINE + CoreConstants.ENVIRONMENT_NEWLINE +
                        "● Turn on GPS and mobile network location" + CoreConstants.ENVIRONMENT_NEWLINE + CoreConstants.ENVIRONMENT_NEWLINE +
                        "● Turn on Wi-Fi")
                .setCancelable(false)
                .setPositiveButton("Settings",buttonLocationServiceSettingOnClickListener )
                .setNegativeButton("Skip",buttonOKOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private DialogInterface.OnClickListener buttonOKOnClickListener = new DialogInterface.OnClickListener(){


        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    private  DialogInterface.OnClickListener buttonLocationServiceSettingOnClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        gpsUtil.stop();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }


}
