package com.npdeas.b1k3labapp.Sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by NPDEAS on 11/05/2018.
 */

public class Seismograph implements NpdeasSensor{

    private float[] result;
    private Sensor sensor;

    public Seismograph(Activity activity, SensorManager sensorManager){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        result = new float[3];
    }

    @Override
    public SensorType getSensorType() {
        return SensorType.GRAVITY;
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
        Log.i("Gravidade ","X : " + result[0] + "Y : " + result[1] + "Z : " + result[2] );

    }

}
