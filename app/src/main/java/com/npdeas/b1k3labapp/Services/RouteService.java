package com.npdeas.b1k3labapp.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.MapView;
import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Maps.Maps;
import com.npdeas.b1k3labapp.Route.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.Sensors.Microphone;

/**
 * Created by NPDEAS on 5/16/2018.
 */

public class RouteService extends Service {

    private static RouteService thisObject = null;
    private OnStartService startService;
    private Thread serviceThread;
    private Maps map;
    private Microphone mic;
    private FileStruct struct;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thisObject = this;
        serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        startService.onStartService();
        return Service.START_REDELIVER_INTENT;
    }

    public void addOnServiceStart(OnStartService onStartService){
        startService = onStartService;

    }
    public void setStartService(Bundle bundle, MapView mapFragment){
        map = new Maps(getBaseContext(),bundle,mapFragment);
        mic = new Microphone();

        serviceThread.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        map.finishRoute();
    }

    public static RouteService getService(){
        return thisObject;
    }
    public interface OnStartService{
        void onStartService();
        void onRouteNoteIsReady();
    }

}
