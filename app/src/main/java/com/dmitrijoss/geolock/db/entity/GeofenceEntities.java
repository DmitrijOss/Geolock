package com.dmitrijoss.geolock.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "geofences")
public class GeofenceEntities {
    @PrimaryKey
    @NonNull
    public String id;

    private String areaName;
    private String latLng;
    private int radius;
    private String colour;

    public String getId() {
        return id;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getLatLng() {
        return latLng;
    }

    public int getRadius() {
        return radius;
    }

    public String getColour() {
        return colour;
    }

    public GeofenceEntities(String id, String areaName, String latLng, int radius, String colour) {
        this.id = id;
        this.areaName = areaName;
        this.latLng = latLng;
        this.radius = radius;
        this.colour = colour;
    }
}
