package com.example.sebastian.smb_geolocalization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private PendingIntent mGeofencePendingIntent;
    private LocationRepository locationRepository;
    private GeofencingClient mGeofencingClient;
    private final int GEOFENCE_EXPIRATION_IN_MILLISECONDS = 10*60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        locationRepository = new LocationRepository(this);
        addGeofences();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            this.requestPermissions(permissionList, 0);
        }
    }

    public void seeMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void addLocation(View view){
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void addGeofences(){
        if(!locationRepository.getAllPositions().isEmpty()){
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("MainActivity", "Added geofences " + locationRepository.getAllPositions().size());
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("MainActivity", "Adding geofences failed");
                            Log.e("MainActivity", e.getMessage());
                        }
                    });
        }
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTriggerIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(prepareGeofences());
        return builder.build();
    }

    private ArrayList<Geofence> prepareGeofences(){
        ArrayList<Geofence> mGeofenceList = new ArrayList<>();
        for (Position position : locationRepository.getAllPositions()){
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(position.getId().toString())

                    .setCircularRegion(
                            position.getLatitude(),
                            position.getLongitude(),
                            ((float) position.getRadius())
                    )

                    .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();


            mGeofenceList.add(geofence);
        }
        return mGeofenceList;
    }

}
