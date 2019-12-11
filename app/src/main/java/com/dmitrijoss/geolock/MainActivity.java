package com.dmitrijoss.geolock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setUpPin(View view){
        Intent intent = new Intent(MainActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
        startActivityForResult(intent, REQUEST_CODE_ENABLE);
    }
}
