package com.npdeas.b1k3labapp.Sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by NPDEAS on 26/04/2018.
 */

public  class NpdeasSensorManager implements SensorEventListener{

    private Activity activity;
    private SensorManager sensorManager;
    private NpdeasSensor [] sensor;

    private final static int SENSOR_LEN = 4;

    public  NpdeasSensorManager(Activity activity){
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensor = new NpdeasSensor[SENSOR_LEN];
        for(int i = 0; i < SENSOR_LEN; i++){
            sensor[i] = null;
        }
    }

    public  NpdeasSensorManager(Activity activity, SensorType sensorType){
            this(activity);
            addSensor(sensorType);
    }

    public void addSensor(SensorType sensorType){
        int i;
        for(i = 0;i < SENSOR_LEN;i++){
            if(sensor[i] == null){
                break;
            }
        }
        switch (sensorType){
            case ACCELEROMETER:{
                sensor[i] = new Accelerometer(activity,sensorManager);
                break;
            }
            case TEMPERATURE:{
                sensor[i] = new Temperature(activity,sensorManager);
                break;
            }
            case GRAVITY:{
                sensor[i] = new Seismograph(activity,sensorManager);
                break;
            }
        }
        if(sensor[i].getSensor() != null){
            sensorManager.registerListener(this,sensor[i].getSensor(),SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    public float[] getSensorResult(SensorType sensorType){
        for(int i = 0;i < SENSOR_LEN;i++){
            if(sensor[i] != null){
                if(sensor[i].getSensorType() == sensorType){
                    return sensor[i].getResult();
                }
            }
        }
        return null;
    }
    public boolean isSensorWork(SensorType sensorType){
        for(int i = 0;i < SENSOR_LEN;i++){
            if(sensor[i] != null){
                if(sensor[i].getSensorType() == sensorType){
                    return sensor[i].getSensor() != null;
                }
            }
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor thisSensor = sensorEvent.sensor;

        for(int i = 0; i < SENSOR_LEN;i++){
            if(sensor[i]!= null) {
                if(thisSensor.getType() == sensor[i].getSensorType().getSensorType()){
                    sensor[i].onSensorChanged(sensorEvent);
                }
            }
        }
    }

    public void onResume(){
        for(int i = 0; i < SENSOR_LEN;i++){
            if(sensor[i] != null) {
                sensorManager.registerListener(this, sensor[i].getSensor(),
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }
    public void onPause(){
        sensorManager.unregisterListener(this);
    }


}
