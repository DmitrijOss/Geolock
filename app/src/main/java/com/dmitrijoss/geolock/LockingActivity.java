package com.dmitrijoss.geolock;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

public class LockingActivity extends AppCompatActivity {

    private static final int CALL_PIN_REQUEST_CODE = 1;
    public Button button;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        //lockManager.enableAppLock(this, CustomPinActivity.class);
        setContentView(R.layout.temp);
        button = findViewById(R.id.finishButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Intent i = new Intent(this, CustomPinActivity.class);
        //startActivityForResult(i, CALL_PIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }


}
