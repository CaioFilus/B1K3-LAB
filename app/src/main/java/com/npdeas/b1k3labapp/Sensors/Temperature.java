package com.npdeas.b1k3labapp.Sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * Created by NPDEAS on 26/04/2018.
 */

public class Temperature implements NpdeasSensor{
    private Sensor sensor;
    private float[] result;

    public Temperature(Activity activity, SensorManager sensorManager){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        result = new float[3];
    }

    @Override
    public SensorType getSensorType() {
        return SensorType.TEMPERATURE;
    }

    @Override
    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public float[] getResult() {
        return result;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        result = sensorEvent.values;
    }

}
