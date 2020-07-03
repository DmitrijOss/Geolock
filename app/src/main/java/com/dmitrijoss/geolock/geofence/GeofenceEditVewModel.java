package com.dmitrijoss.geolock.geofence;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dmitrijoss.geolock.db.GeofenceDatabase;
import com.dmitrijoss.geolock.db.dao.GeofenceDao;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;

public class GeofenceEditVewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private GeofenceDao geofenceDao;
    private GeofenceDatabase db;

    public GeofenceEditVewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "Edit ViewModel");
        db = GeofenceDatabase.getDatabase(application);
        geofenceDao = db.geofenceDao();
    }

    public LiveData<GeofenceEntities> getGeofence(String noteId) {
        return geofenceDao.getGeofence(noteId);
    }
}
