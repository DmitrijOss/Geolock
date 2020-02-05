package com.dmitrijoss.geolock;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class GeofenceMain extends AppCompatActivity {
    private GeolockPageAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofences_main);

        adapter = new GeolockPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager = (ViewPager) findViewById(R.id.containter);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager mViewPager) {
        adapter.addFragment(new GeofenceMainFragment(), "Main geofence menu");
        adapter.addFragment(new GeofenceCreateFragment(), "Create new Geofence");
        adapter.addFragment(new GeofenceEditFragment(), "Geofence edit");
        adapter.addFragment(new GeofenceMapFragment(), "Map");
        mViewPager.setAdapter(adapter);

    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }
}
