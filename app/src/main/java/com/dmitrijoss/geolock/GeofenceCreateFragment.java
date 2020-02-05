package com.dmitrijoss.geolock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GeofenceCreateFragment extends Fragment {
    private Button confirm;
    private Button colour;
    private Button location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geofences_create_fragment,container, false);

        confirm = (Button) view.findViewById(R.id.geofence_create_confirm);
        colour = (Button) view.findViewById(R.id.geofence_colour);
        location = (Button) view.findViewById(R.id.goefence_new_location);

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
                Toast.makeText(getActivity(), "Change colour", Toast.LENGTH_SHORT).show();
                //ColorSheet
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Confirm", Toast.LENGTH_SHORT).show();

                ((GeofenceMain)getActivity()).setViewPager(3);
            }
        });

        return view;
    }
}
