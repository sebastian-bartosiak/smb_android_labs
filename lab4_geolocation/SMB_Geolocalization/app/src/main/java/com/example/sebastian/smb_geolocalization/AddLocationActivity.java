package com.example.sebastian.smb_geolocalization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.UUID;


public class AddLocationActivity extends Activity {

    private LocationRepository repository;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        repository = new LocationRepository(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            this.requestPermissions(permissionList, 0);
        }
        getCurrentLocation();
    }

    public void saveLocalization(View view) {
        EditText name_et = findViewById(R.id.name_et);
        EditText desc_et = findViewById(R.id.desc_et);
        EditText radius_et = findViewById(R.id.radius_et);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            this.requestPermissions(permissionList, 0);
        } else {

            float radius = Float.parseFloat(radius_et.getText().toString());
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            String name = name_et.getText().toString();
            String desc = desc_et.getText().toString();

            Intent proximity_alert = new Intent("ACTION_PROXIMITY_ALERT");

            PendingIntent pendingIntent = PendingIntent.getService(this, 0, proximity_alert, 0);

            if (currentLocation != null) {
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();

                mLocationManager.addProximityAlert(latitude, longitude, radius, -1, pendingIntent);

                Position position = new Position(UUID.randomUUID(), name, desc, radius, latitude, longitude);

                repository.addLocation(position);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Can't find your location!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                        }
                    }
                });
    }
}
