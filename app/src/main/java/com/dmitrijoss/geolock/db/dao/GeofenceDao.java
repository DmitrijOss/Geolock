package com.dmitrijoss.geolock.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmitrijoss.geolock.db.entity.GeofenceEntities;

import java.util.List;

@Dao
public interface GeofenceDao {

    @Query("SELECT * FROM geofences")
    LiveData<List<GeofenceEntities>> getAll();

    @Query("SELECT * FROM geofences WHERE id=:geofenceID")
    LiveData<GeofenceEntities> getGeofence(String geofenceID);

    @Insert
    void insert(GeofenceEntities geofenceEntities);

    @Update
    void update(GeofenceEntities geofenceEntities);

    @Delete
    int delete(GeofenceEntities geofenceEntities);
}
