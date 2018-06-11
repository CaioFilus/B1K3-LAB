package com.npdeas.b1k3labapp.Sensors;

import android.hardware.Sensor;

/**
 * Created by NPDEAS on 26/04/2018.
 */

public enum SensorType {

    TEMPERATURE(Sensor.TYPE_AMBIENT_TEMPERATURE),HUMIDITY(Sensor.TYPE_RELATIVE_HUMIDITY),
    ACCELEROMETER(Sensor.TYPE_ACCELEROMETER),GRAVITY(Sensor.TYPE_GRAVITY) ,MICROPHONE(0);

    private final int sensorType;

    SensorType(int sensorType){
        this.sensorType = sensorType;
    }

    public int getSensorType(){
        return sensorType;
    }

}
