package com.dmitrijoss.geolock;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.dmitrijoss.geolock.db.SavedPackagesViewModel;
import com.dmitrijoss.geolock.db.entity.SavedPackagesEntity;
import com.dmitrijoss.geolock.geofence.FindCurrentLocation;
import com.dmitrijoss.geolock.geofence.GeofenceBroadcastReceiver;
import com.dmitrijoss.lollipin.CustomPinActivity;
import com.google.android.gms.location.Geofence;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LockingForegroundService extends Service {

    public static final String CHANNEL_ID = "LockingServiceChannel";
    FindCurrentLocation findCurrentLocation;
    SavedPackagesViewModel savedPackagesViewModel;
    List<String> protectedPackageNames;
    public String redirectedApp;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //public void stop(){
    //    this.stopSelf();
    //}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        findCurrentLocation = new FindCurrentLocation(this);
        savedPackagesViewModel = new SavedPackagesViewModel(getApplication());  //instantiate savedPackagesViewModel
        protectedPackageNames = new ArrayList<>();          //Contains a list of all the package names that needs to be protected
        redirectedApp = null;

        //Creates an observer to be constantly checking to see if any new packages were added to the SavedPackageEntity table
        //If there is a change then update the protectedPackageNames list
        LiveData<List<SavedPackagesEntity>> savedPackagesEntities = savedPackagesViewModel.getPackageLiveData(getApplication());
        savedPackagesEntities.observeForever(new Observer<List<SavedPackagesEntity>>() {
            @Override
            public void onChanged(List<SavedPackagesEntity> savedPackagesEntities) {
                for(SavedPackagesEntity entity : savedPackagesEntities){
                    protectedPackageNames.add(entity.getPackageName());
                }
            }
        });

        createNotificationChannel();
        //Create new pendingIntent for the Foreground service notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Geolock")
                .setContentText("Geolock is running in the background")
                .setSmallIcon(R.drawable.button_selector)
                .setContentIntent(pendingIntent)
                .build();

        //Start a foreground service with the id of 1 and a notification
        startForeground(1, notification);


        //do heavy work on a background thread
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        // This schedule a runnable task every second
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new findActiveApp().execute();
            }
        }, 0, 1, TimeUnit.SECONDS);
        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class findActiveApp extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            GlobalVars mApplication = ((GlobalVars)getApplicationContext());
            if(mApplication.getInGeofence() == false){
                Log.d("testingPackageName", "not currently in geofence");
            }else{
                Log.d("testingPackageName", "currently in geofence!");
                ComponentName activeApp = topApp();
                String s = activeApp.getPackageName();     //Retrieve the applications package name
                //If an app was previously redirected, the current active app IS NOT the app that was redirected and it's not geolock then reset redirectedApp
                if(redirectedApp != null && !s.equals(redirectedApp) && !s.equals("com.dmitrijoss.geolock")){
                    redirectedApp = null;
                }
                else if(redirectedApp == null){
                    for(String packageName : protectedPackageNames){
                        if (s.equals(packageName)) {
                            ((GlobalVars) getApplication()).setOpenedApp(s);
                            redirectedApp = s;
                            redirect();
                        }
                    }
                }
            }
            return null;
        }
    }


    public void redirect(){
        Intent i = new Intent(this, CustomPinActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public ComponentName topApp(){
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


}