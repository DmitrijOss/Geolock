package com.dmitrijoss.geolock.db;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dmitrijoss.geolock.db.GeofenceDatabase;
import com.dmitrijoss.geolock.db.dao.GeofenceDao;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;

import java.util.List;



public class GeofenceViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private GeofenceDao geofenceDao;
    private GeofenceDatabase geofenceDB;
    private LiveData<List<GeofenceEntities>> allFences;

    public GeofenceViewModel(Application application) {
        super(application);

        geofenceDB = GeofenceDatabase.getDatabase(application);
        geofenceDao = geofenceDB.geofenceDao();
        allFences = geofenceDao.getAll();
    }

    public void insert(GeofenceEntities geofenceEntities) {
        new InsertAsyncTask(geofenceDao).execute(geofenceEntities);
    }

    public LiveData<List<GeofenceEntities>> getAllFences() {
        return allFences;
    }

    public void update(GeofenceEntities geofenceEntities) {
        new UpdateAsyncTask(geofenceDao).execute(geofenceEntities);
    }

    public void delete(GeofenceEntities geofenceEntities) {
        new DeleteAsyncTask(geofenceDao).execute(geofenceEntities);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private class OperationsAsyncTask extends AsyncTask<GeofenceEntities, Void, Void> {

        GeofenceDao mAsyncTaskDao;

        OperationsAsyncTask(GeofenceDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(GeofenceEntities... geofenceEntities) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(GeofenceDao mNoteDao) {
            super(mNoteDao);
        }

        @Override
        protected Void doInBackground(GeofenceEntities... geofenceEntities) {
            mAsyncTaskDao.insert(geofenceEntities[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(GeofenceDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(GeofenceEntities... geofenceEntities) {
            mAsyncTaskDao.update(geofenceEntities[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(GeofenceDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(GeofenceEntities... geofenceEntities) {
            mAsyncTaskDao.delete(geofenceEntities[0]);
            return null;
        }
    }
}
