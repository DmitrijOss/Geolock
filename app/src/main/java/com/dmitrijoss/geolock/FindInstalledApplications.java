package com.dmitrijoss.geolock;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.dmitrijoss.geolock.db.SavedPackagesViewModel;
import com.dmitrijoss.geolock.db.entity.SavedPackagesEntity;
import com.dmitrijoss.geolock.geofence.GeofenceMainActivity;
import com.dmitrijoss.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class FindInstalledApplications extends AppCompatActivity {

    ArrayList<Drawable> imageIDs;
    ArrayList<String> packageNames;
    List<String> enabledPackages;
    List<SavedPackagesEntity> packagesEntities;
    SavedPackagesViewModel savedPackagesViewModel;
    PackageItemAdapter adapter;
    BottomNavigationView bottomAppBar;
    String s;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installed_app_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        final PackageManager pm = getPackageManager();
        imageIDs = new ArrayList<>();
        packageNames = new ArrayList<>();
        enabledPackages = new ArrayList<>();
        savedPackagesViewModel = new SavedPackagesViewModel(getApplication());
        packagesEntities = savedPackagesViewModel.getAllPackages(this);
        if(packagesEntities != null){
            for(SavedPackagesEntity entity : packagesEntities){
                enabledPackages.add(entity.packageName);
            }
        }

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

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo pack : packages){
            packageNames.add(pack.packageName);
            imageIDs.add(pack.loadIcon(pm));
        }

        PackageItemAdapter packageItemAdapter = new PackageItemAdapter();
        listView.setAdapter(packageItemAdapter);

        //listView.


    }

    @Override
    protected void onResume(){
        super.onResume();
        packagesEntities = savedPackagesViewModel.getAllPackages(this);
        if(packagesEntities != null){
            for(SavedPackagesEntity entity : packagesEntities){
                enabledPackages.add(entity.packageName);
            }
        }


    }


    class PackageItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imageIDs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //This will run for every item in the list

            //Initializing widgets/variables
            convertView = getLayoutInflater().inflate(R.layout.package_list_item, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.package_icon);
            TextView tv = (TextView) convertView.findViewById(R.id.package_name);
            SwitchMaterial sw = convertView.findViewById(R.id.package_switch);
            img.setImageDrawable(imageIDs.get(position));
            tv.setText(packageNames.get(position));
            //If the package at this position was previously switched on, then enable it
            s = packageNames.get(position);
            if(packagesEntities != null){
                for (String pName : enabledPackages){
                    if(pName.equals(s)){
                        sw.setChecked(true);
                    }
                }
            }
            //Attach a listener to each switch component
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    s = packageNames.get(position);
                    if(b == true){
                        savedPackagesViewModel.insert(new SavedPackagesEntity(s));
                    }
                    else if(b==false){
                        savedPackagesViewModel.delete(new SavedPackagesEntity(s));
                    }
                }
            });
            return convertView;
        }
    }

    /*public ArrayList<Integer> getList(){
        imageIDs = new ArrayList<>();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo pack : packages){
            imageIDs.add(pack.loadIcon(pm));

        }
        return imageIDs;
    }*/

    /*
            String a = pack.packageName;
            String[] name = a.split("\\.");
            if(name.length > 2){
                if(name[0].equals("com") && name[1].equals("android")){
                    Log.d("aaaaa", "aaaaaaaaaa");
                    // packages.remove(pack);
                }else{

                }
            }*/
}
