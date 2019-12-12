package com.dmitrijoss.geolock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PackageItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> ids;
    private ArrayList<String> names;
    public PackageItemAdapter(Context context, ArrayList<Integer> ids, ArrayList<String> names) {
        this.context = context;
        this.ids=ids;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.package_list_item, null);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
        TextView tv = (TextView) convertView.findViewById(R.id.textView);
        img.setImageResource(ids.get(position));
        tv.setText(names.get(position));

        return convertView;
    }
}
