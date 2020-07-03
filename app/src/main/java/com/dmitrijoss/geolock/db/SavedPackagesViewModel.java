package com.dmitrijoss.geolock.db;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dmitrijoss.geolock.db.dao.GeofenceDao;
import com.dmitrijoss.geolock.db.dao.SavedPackagesDao;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;
import com.dmitrijoss.geolock.db.entity.SavedPackagesEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class SavedPackagesViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private SavedPackagesDao savedPackagesDao;
    private GeofenceDatabase geofenceDB;
    private List<SavedPackagesEntity> allPackages;
    private LiveData<List<SavedPackagesEntity>> allPackagesLivedata;

    public SavedPackagesViewModel(Application application){
        super(application);

        geofenceDB = GeofenceDatabase.getDatabase(application);
        savedPackagesDao = geofenceDB.savedPackagesDao();
    }

    public void insert(SavedPackagesEntity savedPackagesEntity) {
        new InsertAsyncTask(savedPackagesDao).execute(savedPackagesEntity);
    }


    public List<SavedPackagesEntity> getAllPackages(Context c) {
        RetreiveParams retreiveParams = new RetreiveParams(c, savedPackagesDao);
        try {
            allPackages = new RetreieveAsyncTask(c).execute(retreiveParams).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allPackages;
    }

    public LiveData<List<SavedPackagesEntity>> getPackageLiveData(Context c) {
        RetreiveParams retreiveParams = new RetreiveParams(c, savedPackagesDao);
        try {
            allPackagesLivedata = new RetreieveLiveDataAsyncTask(c).execute(retreiveParams).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allPackagesLivedata;
    }

    public void delete(SavedPackagesEntity savedPackagesEntity) {
        new DeleteAsyncTask(savedPackagesDao).execute(savedPackagesEntity);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private static class RetreiveParams{
        Context context;
        SavedPackagesDao savedPackagesDao;

        RetreiveParams(Context context, SavedPackagesDao savedPackagesDao){
            this.context = context;
            this.savedPackagesDao = savedPackagesDao;
        }
    }

    private class OperationsAsyncTask extends AsyncTask<SavedPackagesEntity, Void, Void> {

        SavedPackagesDao mAsyncTaskDao;

        OperationsAsyncTask(SavedPackagesDao savedPackagesDao) {
            this.mAsyncTaskDao = savedPackagesDao;
        }

        @Override
        protected Void doInBackground(SavedPackagesEntity... savedPackagesEntities) {
            return null;
        }
    }

    private class RetreieveAsyncTask extends AsyncTask<RetreiveParams, Void, List<SavedPackagesEntity>>{


        List<SavedPackagesEntity> savedPackagesEntities;
        public ProgressDialog dialog;
        Context context;

        RetreieveAsyncTask(Context context){
            this.context = context;
            this.dialog = new ProgressDialog(context);
        }

        @Override
        protected List<SavedPackagesEntity> doInBackground(RetreiveParams... params) {
            context = params[0].context;
            SavedPackagesDao savedPackagesDao = params[0].savedPackagesDao;

            savedPackagesEntities = savedPackagesDao.getAll();

            return savedPackagesEntities;
        }

        protected void onPostExecute(List<SavedPackagesEntity> result) {
            allPackages = result;
        }
    }

    private class RetreieveLiveDataAsyncTask extends AsyncTask<RetreiveParams, Void, LiveData<List<SavedPackagesEntity>>>{


        LiveData<List<SavedPackagesEntity>> savedPackagesEntities;
        public ProgressDialog dialog;
        Context context;

        RetreieveLiveDataAsyncTask(Context context){
            this.context = context;
            this.dialog = new ProgressDialog(context);
        }

        @Override
        protected LiveData<List<SavedPackagesEntity>> doInBackground(RetreiveParams... params) {
            context = params[0].context;
            SavedPackagesDao savedPackagesDao = params[0].savedPackagesDao;

            savedPackagesEntities = savedPackagesDao.getLiveData();

            return savedPackagesEntities;
        }

        protected void onPostExecute(LiveData<List<SavedPackagesEntity>> result) {
            allPackagesLivedata = result;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(SavedPackagesDao savedPackagesDao) {
            super(savedPackagesDao);
        }

        @Override
        protected Void doInBackground(SavedPackagesEntity... savedPackagesEntities) {
            mAsyncTaskDao.insert(savedPackagesEntities[0]);
            return null;
        }
    }


    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(SavedPackagesDao savedPackagesDao) {
            super(savedPackagesDao);
        }

        @Override
        protected Void doInBackground(SavedPackagesEntity... savedPackagesEntities) {
            mAsyncTaskDao.delete(savedPackagesEntities[0]);
            return null;
        }
    }


}
