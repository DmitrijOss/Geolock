package com.dmitrijoss.geolock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;


public class GeofenceCreateFragment extends Fragment {
    private Button confirm;
    private Button colour;
    private Button location;
    private double radius;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geofences_create_fragment,container, false);

        confirm = (Button) view.findViewById(R.id.geofence_create_confirm);
        colour = (Button) view.findViewById(R.id.geofence_colour);
        location = (Button) view.findViewById(R.id.goefence_new_location);
        GeofenceViewModel g = new GeofenceViewModel();
        radius = g.radiusVal;
        Log.d("radiusIs", String.valueOf(radius));

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "Confirm creation", Toast.LENGTH_SHORT).show();
                ((GeofenceMain)getActivity()).setViewPager(0);
            }
        });

        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(getActivity(), R.style.ThemeOverlay_AppCompat_Dark)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

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

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Change location", Toast.LENGTH_SHORT).show();

                ((GeofenceMain)getActivity()).setViewPager(3);
            }
        });

        return view;
    }
}
