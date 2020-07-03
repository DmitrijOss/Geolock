package com.dmitrijoss.geolock;

import android.app.Application;

public class GlobalVars extends Application {
    private boolean inGeofence;
    private String openedApp;

    public String getOpenedApp() {
        return openedApp;
    }

    public void setOpenedApp(String openedApp) {
        this.openedApp = openedApp;
    }

    public boolean getInGeofence() {
        return inGeofence;
    }

    public void setInGeofence(boolean inGeofence) {
        this.inGeofence = inGeofence;
    }


}
