package com.dmitrijoss.geolock;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dmitrijoss.geolock.geofence.GeofenceMainActivity;
import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ENABLE = 11;
    BottomNavigationView bottomAppBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

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


    public void checkPin(View view){
        Intent intent = new Intent(SettingsActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        startActivity(intent);
    }

    public void setUpPin(View view){
        Intent intent = new Intent(SettingsActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
        startActivityForResult(intent, REQUEST_CODE_ENABLE);

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
    }

    public void startService(View view){
        Intent serviceIntent = new Intent(this, LockingForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View view){
        Intent serviceIntent = new Intent(this, LockingForegroundService.class);
        //LockingForegroundService l = new LockingForegroundService();
        //l.stopSelf();
        stopService(serviceIntent);
        //LockingForegroundService lockingForegroundService = new LockingForegroundService();
        //lockingForegroundService.stopSelf();
    }
}
