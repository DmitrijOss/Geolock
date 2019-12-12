package com.dmitrijoss.geolock;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.PinCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FindInstalledApplications extends PinActivity {

    ArrayList<Drawable> imageIDs;
    ArrayList<String> packageNames;
    PackageItemAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installed_app_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        final PackageManager pm = getPackageManager();
        imageIDs = new ArrayList<>();
        packageNames = new ArrayList<>();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo pack : packages){
            packageNames.add(pack.packageName);
            imageIDs.add(pack.loadIcon(pm));
            Log.d("packagesList", Integer.toString(packageNames.size()));

        }

        PackageItemAdapter packageItemAdapter = new PackageItemAdapter();
        listView.setAdapter(packageItemAdapter);


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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.package_list_item, null);

            ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
            TextView tv = (TextView) convertView.findViewById(R.id.textView);
            img.setImageDrawable(imageIDs.get(position));
            tv.setText(packageNames.get(position));

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
}
