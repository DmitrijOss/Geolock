package com.dmitrijoss.geolock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public GeofenceBroadcastReceiver() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
