package com.dmitrijoss.geolock.geofence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dmitrijoss.geolock.R;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;

import java.util.List;

public class GeofenceListAdapter extends RecyclerView.Adapter<GeofenceListAdapter.MyViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(GeofenceEntities mGeofenceEntity);
    }

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<GeofenceEntities> mGeofenceEntities;
    private OnDeleteClickListener onDeleteClickListener;

    public GeofenceListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.geofence_list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (mGeofenceEntities != null) {
            GeofenceEntities geofenceEntities = mGeofenceEntities.get(position);
            holder.setData(geofenceEntities.getLatLng(), position);
            holder.setData(String.valueOf(geofenceEntities.getRadius()), position);
            holder.setData(geofenceEntities.getColour(), position);
            holder.setData(geofenceEntities.getAreaName(), position);
            holder.setListeners();
        } else {
            // Covers the case of data not being ready yet.
            holder.noteItemView.setText("Data is null");
        }
    }

    @Override
    public int getItemCount() {
        if (mGeofenceEntities != null)
            return mGeofenceEntities.size();
        else return 0;
    }

    public void setGeofences(List<GeofenceEntities> notes) {
        mGeofenceEntities = notes;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView noteItemView;
        private int mPosition;
        private ImageView imgDelete, imgEdit;
        //constructor
        public MyViewHolder(View itemView) {
            super(itemView);
            noteItemView = itemView.findViewById(R.id.geofenceName);
            imgDelete 	 = itemView.findViewById(R.id.geofenceDeleteButton);
            imgEdit 	 = itemView.findViewById(R.id.geofenceEditButton);
        }

        //
        public void setData(String geofence, int position) {
            noteItemView.setText(geofence);
            mPosition = position;
        }

        public void setListeners() {
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GeofenceEditActivity.class);
                    intent.putExtra("geofence_id", mGeofenceEntities.get(mPosition).getId());
                    intent.putExtra("geofence_name", mGeofenceEntities.get(mPosition).getAreaName());
                    intent.putExtra("geofence_latlng", mGeofenceEntities.get(mPosition).getLatLng());
                    intent.putExtra("geofence_radius", mGeofenceEntities.get(mPosition).getRadius());
                    intent.putExtra("geofence_colour", mGeofenceEntities.get(mPosition).getColour());
                    ((Activity)mContext).startActivityForResult(intent, GeofenceMainActivity.UPDATE_GEOFENCE_ACTIVITY_REQUEST_CODE);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(mGeofenceEntities.get(mPosition));
                    }
                }
            });
        }
    }
}
