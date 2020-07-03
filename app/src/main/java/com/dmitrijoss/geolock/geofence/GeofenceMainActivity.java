package com.dmitrijoss.geolock.geofence;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmitrijoss.geolock.FindInstalledApplications;
import com.dmitrijoss.geolock.R;
import com.dmitrijoss.geolock.SettingsActivity;
import com.dmitrijoss.geolock.db.GeofenceViewModel;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

/*
    The main class for the views relating to geofencing
    Used with geofences_main.xml
    Going to be used to display the list of created geofences and allow you to create new geofences or edit existing ones.
 */
public class GeofenceMainActivity extends AppCompatActivity implements GeofenceListAdapter.OnDeleteClickListener{
    FloatingActionButton create;
    private static final int NEW_GEOFENCE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_GEOFENCE_ACTIVITY_REQUEST_CODE = 2;
    private String TAG = this.getClass().getSimpleName();
    private GeofenceViewModel geofenceViewModel;
    private GeofenceListAdapter geofenceListAdapter;
    private GeofenceCreate geofenceCreate;
    private BottomNavigationView bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofences_main);
        create = findViewById(R.id.geofence_create);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        geofenceListAdapter = new GeofenceListAdapter(this, this);
        geofenceCreate = new GeofenceCreate();
        recyclerView.setAdapter(geofenceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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

        geofenceViewModel = new ViewModelProvider(this).get(GeofenceViewModel.class);
        LiveData<List<GeofenceEntities>> g = null;
        try{
            g = geofenceViewModel.getAllFences();
        }catch (Exception e){
            e.printStackTrace();
        }

       geofenceViewModel.getAllFences().observe(this, new Observer<List<GeofenceEntities>>() {
           @Override
           public void onChanged(@Nullable List<GeofenceEntities> geofenceEntities) {
               geofenceListAdapter.setGeofences(geofenceEntities);
           }
       });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            if (requestCode == NEW_GEOFENCE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

                // Code to insert a new geofence
                final String geofenceEntities_id = UUID.randomUUID().toString();
                GeofenceEntities geofenceEntities = new GeofenceEntities(
                        geofenceEntities_id,
                        data.getStringExtra(GeofenceCreateActivity.GEOFENCE_NAME),
                        data.getStringExtra(GeofenceCreateActivity.GEOFENCE_LATLNG),
                        data.getIntExtra(GeofenceCreateActivity.GEOFENCE_RADIUS, 1000),
                        data.getStringExtra(GeofenceCreateActivity.GEOFENCE_COLOUR));
                geofenceViewModel.insert(geofenceEntities);

                Toast.makeText(
                        getApplicationContext(),
                        "saved",
                        Toast.LENGTH_LONG).show();
            } else if (requestCode == UPDATE_GEOFENCE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

                // Code to update a geofence
                GeofenceEntities geofenceEntities = new GeofenceEntities(
                        data.getStringExtra(GeofenceEditActivity.GEOFENCE_ID),
                        data.getStringExtra(GeofenceEditActivity.UPDATED_GEOFENCE_NAME),
                        data.getStringExtra(GeofenceEditActivity.UPDATED_GEOFENCE_LATLNG),
                        data.getIntExtra(GeofenceEditActivity.UPDATED_GEOFENCE_RADIUS, 1000),
                        data.getStringExtra(GeofenceEditActivity.UPDATED_GEOFENCE_COLOUR));
                geofenceViewModel.update(geofenceEntities);

                Toast.makeText(
                        getApplicationContext(),
                        "updated",
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "not saved",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void OnDeleteClickListener(GeofenceEntities geofenceEntity) {
        // Code for Delete operation
        geofenceViewModel.delete(geofenceEntity);
        geofenceCreate.deleteGeofence(geofenceEntity.getAreaName(), this);
    }



    public void makeNew(View v){
        Toast.makeText(getApplicationContext(), "Create new geofence", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, GeofenceCreateActivity.class);
        startActivityForResult(i, NEW_GEOFENCE_ACTIVITY_REQUEST_CODE);
    }



}
