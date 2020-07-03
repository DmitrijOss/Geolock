package com.dmitrijoss.geolock.geofence;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.dmitrijoss.geolock.FindInstalledApplications;
import com.dmitrijoss.geolock.R;
import com.dmitrijoss.geolock.SettingsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import static androidx.constraintlayout.widget.Constraints.TAG;



public class GeofenceCreateActivity extends AppCompatActivity implements GeofenceMapFragment.OnInputListener{


    //Get the values the user entered in the map fragment and use them here
    @Override
    public void sendInput(LatLng location, int rad){
        Log.d(TAG, "sendInput: got the input: " + location + " and: " + rad);

        radius = rad;
        latLng = location;

    }

    public static final String GEOFENCE_NAME = "new_geofence";
    public static final String GEOFENCE_LATLNG = "new_latlng";
    public static final String GEOFENCE_RADIUS = "new_radius";
    public static final String GEOFENCE_COLOUR = "new_colour";
    public FloatingActionButton confirm;
    private BottomNavigationView bottomAppBar;
    private Button colour;
    private int radius = 100;
    EditText geofenceName;
    LatLng latLng = null;

    @Nullable
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofences_create);
        confirm = (FloatingActionButton) findViewById(R.id.geofence_create_confirm);
        confirm.setVisibility(View.VISIBLE);
        colour = (Button) findViewById(R.id.geofence_colour);
        geofenceName = (EditText) findViewById(R.id.GeofenceCreateName);

        radius = getIntent().getIntExtra("radiusVal", 100);
        Log.d("radiusIs", String.valueOf(radius));

        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(getApplicationContext(), R.style.ThemeOverlay_AppCompat_Dark)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        //setLayoutColor(envelope);
                                    }
                                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                        .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                        .show();
            }
        });
        bottomAppBar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomAppBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.protected_apps){
                    Intent installedApplicationsIntent = new Intent(getApplicationContext(), FindInstalledApplications.class);
                    startActivity(installedApplicationsIntent);
                }else if(itemID == R.id.locations){
                    Intent locationsIntent = new Intent(getApplicationContext(), GeofenceMainActivity.class);
                    startActivity(locationsIntent);
                }else if(itemID == R.id.settings){
                    Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settingsIntent);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        confirm.setVisibility(View.VISIBLE);

    }


    /*private void setLayoutColor(ColorEnvelope envelope) {
        TextView textView = findViewById(R.id.textView);
        textView.setText("#" + envelope.getHexCode());

        AlphaTileView alphaTileView = findViewById(R.id.alphaTileView);
        alphaTileView.setPaintColor(envelope.getColor());
    }*/

    public void selectLocation(View view){
        Toast.makeText(getApplicationContext(), "Change location", Toast.LENGTH_SHORT).show();
        GeofenceMapFragment geofenceMapFragment = new GeofenceMapFragment();
        geofenceMapFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_Panel);
        geofenceMapFragment.show(getSupportFragmentManager(), "Location select");
    }

    public void confirm(View view){
        //check to see if a name was entered
        if(TextUtils.isEmpty(geofenceName.getText())){
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }else{

            String a;
            //Instantiate variables
            if(!geofenceName.getText().toString().equals(null)){
                a = geofenceName.getText().toString();
            }else{a = "null";}

            GeofenceCreate gc = new GeofenceCreate();                                       //Object to create the new geofence
            FindCurrentLocation currentLatLng = new FindCurrentLocation(this);                  //Object to get current location
            LatLng selectedLocation = latLng;
            if(latLng == null){                                                             //If the user didn't select a LatLng then create one at their location
                Log.d("selectedLocation", "selectedLocation is null");            //Log it
                currentLatLng.getLocation();
                Double d1 = currentLatLng.getLatitude();
                Double d2 = currentLatLng.getLongitude();
                selectedLocation = new LatLng(d1, d2);                      //Set the latLng to be users current location
            }
            if(selectedLocation == null){                                                             //check to see if it is still null, if it is then ask the user to select a value/turn on location services
                selectedLocation = new LatLng(0.0, 0.0);
            }
            gc.makeFence(radius, selectedLocation, geofenceName.getText().toString(), this);    //Create a new geofence with the selected radius,
                                                                                                   //selected location, selected name and the context.
            Intent i = new Intent(this, GeofenceMainActivity.class);
            double one = selectedLocation.latitude;
            double two = selectedLocation.longitude;
            String c = one + "," + two;

            i.putExtra(GEOFENCE_NAME, a);
            i.putExtra(GEOFENCE_LATLNG, c);
            i.putExtra(GEOFENCE_RADIUS, radius);
            i.putExtra(GEOFENCE_COLOUR, "0xffff0000");
            setResult(RESULT_OK, i);
            Toast.makeText(getApplicationContext(), "Created area.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
