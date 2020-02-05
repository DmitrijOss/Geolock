package com.dmitrijoss.geolock;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GeofenceCreate extends AppCompatActivity {

    private GeofencingClient geofencingClient;
    List<Geofence> geofences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geofencingClient = LocationServices.getGeofencingClient(this);

    }

    public void makeFenceOnLocation(){
        LatLng currentLoc;
        FindCurrentLocation findLocation = new FindCurrentLocation();
        currentLoc = findLocation.getLocation();
        geofences = new ArrayList<Geofence>();
        geofences.add(new Geofence.Builder()
                .setRequestId("1")
                .setCircularRegion(currentLoc.latitude, currentLoc.longitude, 20f)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        );
    }
}
