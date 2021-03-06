package com.dmitrijoss.geolock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dmitrijoss.geolock.geofence.GeofenceMainActivity;
import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends PinCompatActivity {

    Button startServiceButton, stopServiceButton;
    BottomNavigationView bottomAppBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stopServiceButton);
        ((GlobalVars) getApplication()).setOpenedApp("com.dmitrijoss.geolock");

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

    public void goToF(View view){
        Intent i = new Intent(this, GeofenceMainActivity.class);
        startActivity(i);
    }

    public void goToInstalledPackages(View view){

        Intent intent = new Intent(this, FindInstalledApplications.class);
        startActivity(intent);
    }


    public void disablePin(View view){
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.disableAppLock();
    }







}
