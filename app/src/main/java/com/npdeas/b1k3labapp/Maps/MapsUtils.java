package com.npdeas.b1k3labapp.Maps;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by NPDEAS on 03/05/2018.
 */

public class MapsUtils {


    public static float getDistance(double lat1, double lng1, double lat2, double lng2) {
        LatLng point1 = new LatLng(lat1, lng1);
        LatLng point2 = new LatLng(lat2, lng2);
        return  getDistance(point1,point2);
    }

    public static float getDistance(LatLng point1, LatLng point2) {
        float[] result = new float[1];
        Location.distanceBetween(point1.latitude, point1.longitude,
                point2.latitude, point2.longitude, result);
        return result[0];
    }
}


