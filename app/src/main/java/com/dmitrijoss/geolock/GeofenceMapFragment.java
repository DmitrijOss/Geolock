package com.dmitrijoss.geolock;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;



public class GeofenceMapFragment extends Fragment implements OnMapReadyCallback {
    Button confirm;
    private GoogleMap gMap;
    static String currentLocation = "Ireland";
    MapView mapView;
    double currentLat;
    double currentLong;
    LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geofences_map_fragment,container, false);
        confirm = (Button) view.findViewById(R.id.geofence_confirm_location);
        /*SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "Confirm location", Toast.LENGTH_SHORT).show();
                ((GeofenceMain)getActivity()).setViewPager(0);
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                currentLong = latLng.longitude;
                currentLat = latLng.latitude;
                Log.d("latLng", "Latitude:  "+ currentLat + " Longitude: " + currentLong );

                Toast.makeText(getActivity().getApplicationContext(), "Latitude:  "+ currentLat + " Longitude: " + currentLong, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
