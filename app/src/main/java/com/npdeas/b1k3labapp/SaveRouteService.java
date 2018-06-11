package com.npdeas.b1k3labapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Maps.Maps;
import com.npdeas.b1k3labapp.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.Sensors.Microphone;

/**
 * Created by NPDEAS on 5/16/2018.
 */

public class SaveRouteService extends IntentService {

    private Maps map;
    private Microphone mic;
    private Bluetooth bluetooth;
    private FileStruct struct;

    public SaveRouteService(Maps map, Microphone mic, Bluetooth bluetooth) {
        super("SaveRouteService");
        this.map = map;
        this.mic = mic;
        this.bluetooth = bluetooth;
        struct = new FileStruct();
        map.addOnLocationChaged(new Maps.OnLocationChanged() {
            @Override
            public void OnLocationChanged() {
                /*struct.setLatitude(map.getLatitude());
                struct.setLongetude(map.getLongitude());
                struct.setSpeed(map.getSpeed());
                if(bluetooth!= null){
                    if(bluetooth.isConnected()){
                        struct.setDistance(bluetooth.getDistance());
                    }
                }*/
            }
        });
    }
    public SaveRouteService(){
        super("SaveRouteService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        map.startRoute();
        mic.startRecord();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.finishRoute();
    }
}
