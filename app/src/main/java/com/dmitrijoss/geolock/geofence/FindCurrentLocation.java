package com.dmitrijoss.geolock.geofence;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class FindCurrentLocation {
    LocationManager locationManager;
    Context context;
    Double latitude;
    Double longitude;


    public FindCurrentLocation(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            //Auto-generated if statement
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            l = locationManager.getLastKnownLocation(providers.get(i));
            if(l != null){
                break;
            }
        }
        if(l != null){
            latitude = l.getLatitude();
            longitude = l.getLongitude();
        }}


        /**
         * Function to get latitude
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Function to get longitude
         */
        public double getLongitude() {
            return longitude;
        }

        public LatLng getLatLng(){
            LatLng ll = new LatLng(latitude, longitude);
            return ll;
        }
}
