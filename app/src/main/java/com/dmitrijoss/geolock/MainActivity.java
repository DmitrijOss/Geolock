package com.dmitrijoss.geolock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

public class MainActivity extends AppCompatActivity {

    Button startServiceButton, stopServiceButton;
    private static final int REQUEST_CODE_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stopServiceButton);
    }

    public void goToF(View view){
        Intent i = new Intent(this, GeofenceMain.class);
        startActivity(i);
    }

    public void startService(View view){
        Intent serviceIntent = new Intent(this, LockingService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View view){
        Intent serviceIntent = new Intent(this, LockingService.class);
        stopService(serviceIntent);
    }

    public void checkPin(View view){
        Intent intent = new Intent(MainActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        startActivity(intent);
    }

    public void setUpPin(View view){
        Intent intent = new Intent(MainActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
        startActivityForResult(intent, REQUEST_CODE_ENABLE);

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
    }

    public void disablePin(View view){
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.disableAppLock();
    }

    public void goToInstalledPackages(View view){

        Intent intent = new Intent(this, FindInstalledApplications.class);
        startActivity(intent);
    }
}
