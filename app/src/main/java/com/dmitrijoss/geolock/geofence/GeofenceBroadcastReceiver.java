package com.dmitrijoss.geolock.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.dmitrijoss.geolock.GlobalVars;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public List<Geofence> triggeringGeofences;

    public GeofenceBroadcastReceiver() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GlobalVars mApplication = ((GlobalVars)context.getApplicationContext());
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        //when user enters geofence, add it to the list of currently triggering geofences
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.d("GEOFENCE_ENTER", "ENTERED GEOFENCE");
            mApplication.setInGeofence(true);
        }

        //when user leaves geofence, if it exists in the currently triggering geofences list
        //then remove it from that list.
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            mApplication.setInGeofence(false);
            Log.d("GEOFENCE_EXIT", "EXITED GEOFENCE");
        }
    }
}
