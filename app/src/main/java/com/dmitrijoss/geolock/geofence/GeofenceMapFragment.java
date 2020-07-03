package com.dmitrijoss.geolock.geofence;

import android.content.Context;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dmitrijoss.geolock.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static androidx.constraintlayout.widget.Constraints.TAG;

/*
    This class is used with geofences_map.xml
    It is used to select the desired location on the map object within that view
 */

public class GeofenceMapFragment extends DialogFragment implements OnMapReadyCallback {

    public interface OnInputListener{
        void sendInput(LatLng location, int rad);
    }
    public OnInputListener mOnInputListener;

    LatLng selectedLocation;
    GeofenceMapFragment geofenceMapFragment;
    Button confirm;
    EditText radiusText;
    private GoogleMap gMap;
    MapView mapView;
    double currentLat;
    double currentLong;
    int radius = 100;
    boolean markerExists = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.geofences_map, container, false);

        //initialize widgets
        confirm = (Button) view.findViewById(R.id.geofence_confirm_location);
        radiusText = (EditText) view.findViewById(R.id.radius);
        mapView = (MapView) view.findViewById(R.id.map);
        geofenceMapFragment = new GeofenceMapFragment();

        //google map initialization
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


        radiusText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    radius = Integer.parseInt(radiusText.getText().toString());
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity().getApplicationContext(), "Confirm location", Toast.LENGTH_SHORT).show();
                if (!radiusText.getText().toString().equals("")) {
                    radius = Integer.parseInt(radiusText.getText().toString());
                }else{radius = 100;}
                mOnInputListener.sendInput(selectedLocation, radius);
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        FindCurrentLocation currentLatLng = new FindCurrentLocation(getContext());
        currentLatLng.getLocation();

        LatLng latLng = currentLatLng.getLatLng();

        gMap = googleMap;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                currentLong = latLng.longitude;
                currentLat = latLng.latitude;
                Log.d("latLng", "Latitude:  "+ currentLat + " Longitude: " + currentLong );

                Toast.makeText(getActivity().getApplicationContext(), "Latitude:  "+ currentLat + " Longitude: " + currentLong, Toast.LENGTH_SHORT).show();

                selectedLocation = latLng;
                if(markerExists == false){
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("geofence"));
                    googleMap.addCircle(new CircleOptions()
                            .radius(radius)
                            .center(latLng)
                            .strokeColor(Color.RED))
                            .setFillColor(0x220000FF);
                    markerExists = true;
                }
                else{
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("geofence"));
                    googleMap.addCircle(new CircleOptions()
                            .radius(radius)
                            .center(latLng)
                            .strokeColor(Color.RED))
                            .setFillColor(0x220000FF);
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
