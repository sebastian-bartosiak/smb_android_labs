package com.example.sebastian.smb_geolocalization;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTriggerIntentService extends IntentService {

    private final String TAG = "GeofenceTriggerIntent";
    private LocationRepository locationRepository;

    public GeofenceTriggerIntentService() {
        super("GeofenceTriggerIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        locationRepository = new LocationRepository(this);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, geofencingEvent.toString());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            for (Geofence geofence: triggeringGeofences) {
                Position position = locationRepository.getPositionsById(geofence.getRequestId());
                String msg = "You have entered " + position.getName() + " area!";
                if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                    msg = "You have left " + position.getName() + " area!";
                }
                sendNotification(msg, position.getId().hashCode());
                Log.i(TAG, msg);
            }


            // Send notification and log the transition details.


        } else {
            // Log the error.
            Log.e(TAG, "Wrong notification found!");
        }
    }

    private void sendNotification(String message, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Geofence triggered!")
                        .setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }



}
