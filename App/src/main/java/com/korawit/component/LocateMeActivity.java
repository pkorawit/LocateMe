package com.korawit.component;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocateMeActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {


    private static final String TAG = "LocateMeActivity";

    LocationClient locationClient;
    Location currentLocation;
    LocationRequest locationRequest;

    private static final long UPDATE_INTERVAL = 15000;
    private static final long FASTEST_INTERVAL = 10000;
    private static final String broadcast_filter = "LOCATION_UPDATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_me);

        locationClient = new LocationClient(this, this, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.locate_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_current_location) {
            GetCurrentLocation();
            return true;
        }else if (id == R.id.action_track_location) {
            TrackLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        locationClient.connect();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        locationClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Log.d("LocationProvider", "Connected");


    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Log.d("LocationProvider", "Disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d("LocationProvider",connectionResult.toString());
    }

    public void GetCurrentLocation(){

        currentLocation = locationClient.getLastLocation();
        TextView txtCurrentLocation = (TextView) findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(String.format("%s,%s", currentLocation.getLatitude(), currentLocation.getLongitude()));
    }

    public void TrackLocation(){

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        //locationClient.requestLocationUpdates(locationRequest, this);


        IntentFilter filter = new IntentFilter(broadcast_filter);

        LocationUpdateReceiver locationReceiver = new LocationUpdateReceiver();

        registerReceiver(locationReceiver, filter);

        Intent intent = new Intent(broadcast_filter);
        PendingIntent locationUpdateIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        locationClient.requestLocationUpdates(locationRequest, locationUpdateIntent);

        Log.d(TAG,"TrackLocation");

    }


    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
