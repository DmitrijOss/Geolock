package com.dmitrijoss.geolock.geofence;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dmitrijoss.geolock.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

//This class is used to create geofences and add them to the geofencingClient

public class GeofenceCreate {

    private GeofencingClient geofencingClient;
    List<Geofence> geofences = new ArrayList<Geofence>();
    PendingIntent geofencePendingIntent;
    LocationServices locationServices;

    //Pass in paramaters to create a new geofence and add it to the geofencing client
    public void makeFence(int radius, LatLng latlng, String name, Context c){
        //add a new geofence object to the 'geofences' list
        geofencingClient = new GeofencingClient(c);
        geofences.add(new Geofence.Builder()
            .setRequestId(name)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setCircularRegion(latlng.latitude, latlng.longitude, radius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_EXIT)
            .build());

        //add geofences to the geofencing client and return success or failure
        try{
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent(c));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Create and return a new GeofencingRequest object by taking all the geofences in the 'geofences' list
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    //Create a new pendingRequest to point to the GeofenceBroadcastReceiver class
    private PendingIntent getGeofencePendingIntent(Context c) {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(c, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(c, 011, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    public void deleteGeofence(String geofenceID, Context c){
        geofencingClient = new GeofencingClient(c);
        List<String> al = new ArrayList<String>();
        al.add(geofenceID);
        geofences.remove(geofenceID);
        geofencingClient.removeGeofences(al);
    }
}
