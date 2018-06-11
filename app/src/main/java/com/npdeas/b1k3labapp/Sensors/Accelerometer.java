package com.npdeas.b1k3labapp.Sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class Accelerometer implements NpdeasSensor{
    private Sensor sensor;
    private float[] result;

    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private float spdX;
    private float spdY;
    private float spdZ;

    private long time = 0;

    public Accelerometer(Activity activity, SensorManager sensorManager){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        result = new float[3];
    }

    @Override
    public SensorType getSensorType() {
        return SensorType.ACCELEROMETER;
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
        if(time == 0){
            time = System.currentTimeMillis();
        }else {
            float deltaTime = ((float)(System.currentTimeMillis() - time))/1000;
            spdX = ((result[0] + lastX)*deltaTime)/2;
            spdY = ((result[1] + lastY)*deltaTime)/2;
            spdZ = ((result[2] + lastZ)*deltaTime)/2;
            float module = (float) Math.pow(Math.pow(spdX,2) + Math.pow(spdY,2) + Math.pow(spdZ,2),0.5);
            float []direVector = new float[3];
            direVector[0] = spdX/module;
            direVector[1] = spdY/module;
            direVector[2] = spdZ/module;


        }




    }
}
