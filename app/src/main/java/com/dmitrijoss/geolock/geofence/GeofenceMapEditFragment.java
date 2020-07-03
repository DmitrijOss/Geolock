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

public class GeofenceMapEditFragment extends DialogFragment implements OnMapReadyCallback {

    public interface OnInputListener{
        void sendInput(LatLng location, int rad);
    }
    public OnInputListener mOnInputListener;

    LatLng selectedLocation;    //location the user selects
    LatLng ll;                  //location that the user originally selected
    Button confirm;
    EditText radiusText;
    private GoogleMap gMap;
    MapView mapView;
    double currentLat;
    double currentLong;
    int radius = 100;
    boolean markerExists = false;


    /*static GeofenceMapEditFragment newInstance(String loc, int rad) {
        OnInputListener mOnInputListener;

        GeofenceMapEditFragment g = new GeofenceMapEditFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("radius", rad);
        args.putString("latLng", loc);
        g.setArguments(args);

        return g;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.geofences_map, container, false);
        radius = getArguments().getInt("geofenceRadius");
        String passedLatLng = getArguments().getString("geofenceLatLng");
        String asd[] = passedLatLng.split(",");

        ll = new LatLng(Double.parseDouble(asd[0]), Double.parseDouble(asd[1]));
        selectedLocation = ll;

        //initialize widgets
        confirm = (Button) view.findViewById(R.id.geofence_confirm_location);
        radiusText = (EditText) view.findViewById(R.id.radius);
        mapView = (MapView) view.findViewById(R.id.map);
       //gMap = ((SupportMapFragment) getFragment()
        //        .findFragmentById(R.id.map)).getMap();

        //google map initialization
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        radiusText.setText(String.valueOf(radius));
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
                radius = Integer.parseInt(radiusText.getText().toString());
                mOnInputListener.sendInput(selectedLocation, radius);
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        gMap.addMarker(new MarkerOptions().position(ll).title("title"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15f));
        googleMap.addCircle(new CircleOptions()
                .radius(radius)
                .center(ll)
                .strokeColor(Color.RED))
                .setFillColor(0x220000FF);
        markerExists = true;

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext().getApplicationContext());
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
