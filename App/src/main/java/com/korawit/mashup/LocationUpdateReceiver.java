package com.korawit.mashup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;

public class LocationUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String locationKey = LocationClient.KEY_LOCATION_CHANGED;

        if (intent.hasExtra(locationKey)) {

            Location location = (Location) intent.getExtras().get(locationKey);

            String msg = "Updated Location(Pending Intent): " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }
        else{

            String msg = "Updated Location(Pending Intent): Intent extra not found";
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }

    }
}
