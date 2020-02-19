package com.dmitrijoss.geolock;

import androidx.lifecycle.ViewModel;

public class GeofenceViewModel extends ViewModel {
    public double radiusVal;

    public void setRadius(double d){
        radiusVal = d;
    }
}
