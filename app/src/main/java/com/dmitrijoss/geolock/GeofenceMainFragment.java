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

public class GeofenceMainFragment extends Fragment {
    Button create;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geofences_main_fragment,container, false);
        create = (Button) view.findViewById(R.id.geofence_create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Create new geofence", Toast.LENGTH_SHORT).show();
                ((GeofenceMain)getActivity()).setViewPager(1);
            }
        });


        return view;
    }
}
