package com.dmitrijoss.geolock.geofence;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dmitrijoss.geolock.FindInstalledApplications;
import com.dmitrijoss.geolock.R;
import com.dmitrijoss.geolock.SettingsActivity;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GeofenceEditActivity extends AppCompatActivity implements GeofenceMapEditFragment.OnInputListener{

    @Override
    public void sendInput(LatLng location, int rad){
        Log.d(TAG, "sendInput: got the input: " + location + " and: " + rad);

        geofenceRadius = rad;
        double one = location.latitude;
        double two = location.longitude;
        String c = one + "," + two;
        geofenceLatLng = c;

    }
    public static final String GEOFENCE_ID = "geofence_id";
    static final String UPDATED_GEOFENCE_NAME = "updated_geofence_name";
    static final String UPDATED_GEOFENCE_LATLNG = "updated_geofence_latlng";
    static final String UPDATED_GEOFENCE_RADIUS = "updated_geofence_radius";
    static final String UPDATED_GEOFENCE_COLOUR = "updated_geofence_colour";
    private EditText etName;

    private Bundle bundle;
    private String geofenceID;
    BottomNavigationView bottomAppBar;
    private String geofenceName;
    private String geofenceLatLng;
    private int geofenceRadius;
    private LiveData<GeofenceEntities> geofenceEntities;

    GeofenceEditVewModel geofenceEditVewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofences_edit);

        etName = findViewById(R.id.editName);


        bundle = getIntent().getExtras();


        if (bundle != null) {
            geofenceID = bundle.getString("note_id");
            geofenceName = bundle.getString("geofence_name");
            geofenceLatLng = bundle.getString("geofence_latlng");
            geofenceRadius = bundle.getInt("geofence_radius", 1000);
        }

        etName.setText(geofenceName);

        geofenceEditVewModel = new ViewModelProvider(this).get(GeofenceEditVewModel.class);

        Log.d("SKRRT", "geofenceID is: " + geofenceID);
        geofenceEntities = geofenceEditVewModel.getGeofence(geofenceID);
        geofenceEntities.observe(this, new Observer<GeofenceEntities>() {
            @Override
            public void onChanged(@Nullable GeofenceEntities geofenceEntities) {
                if(geofenceEntities != null){
                    String s = geofenceEntities.getAreaName();
                    Log.d("SIS", s);
                    etName.setText(s);
                }

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

    public void updateNote(View view) {
        String updatedNote = etName.getText().toString();


        String[] a = geofenceLatLng.split(",");
        Double d1 = Double.parseDouble(a[0]);
        Double d2 = Double.parseDouble(a[1]);
        LatLng ll = new LatLng(d1, d2);
        GeofenceCreate gc = new GeofenceCreate();
        gc.makeFence(geofenceRadius, ll, updatedNote, this);

        Intent i = new Intent(this, GeofenceMainActivity.class);
        i.putExtra(GEOFENCE_ID, geofenceID);
        i.putExtra(UPDATED_GEOFENCE_NAME, updatedNote);
        i.putExtra(UPDATED_GEOFENCE_LATLNG, geofenceLatLng);
        i.putExtra(UPDATED_GEOFENCE_RADIUS, geofenceRadius);
        i.putExtra(UPDATED_GEOFENCE_COLOUR, "0xffff0000");
        setResult(RESULT_OK, i);
        Toast.makeText(getApplicationContext(), "Updated area.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelUpdate(View view) {
        finish();
    }

    public void selectLocation(View view){
        Toast.makeText(getApplicationContext(), "Change location", Toast.LENGTH_SHORT).show();
        GeofenceMapEditFragment geofenceMapEditFragment = new GeofenceMapEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("geofenceLatLng", geofenceLatLng);
        bundle.putInt("geofenceRadius", geofenceRadius);
        geofenceMapEditFragment.setArguments(bundle);
        geofenceMapEditFragment.show(getSupportFragmentManager(), "Location select");
        //GeofenceMapEditFragment newFragment = GeofenceMapEditFragment.newInstance(geofenceLatLng, geofenceRadius);
        //newFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_Panel);
        //newFragment.show(getSupportFragmentManager(), "Location select");
    }
}
