package com.npdeas.b1k3labapp.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by NPDEAS on 26/04/2018.
 */

public interface NpdeasSensor {

    SensorType getSensorType();

    Sensor getSensor();

    float[] getResult();

    void onSensorChanged(SensorEvent sensorEvent);
}
