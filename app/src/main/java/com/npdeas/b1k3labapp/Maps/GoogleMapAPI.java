package com.npdeas.b1k3labapp.Maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by NPDEAS on 7/4/2018.
 */

public abstract class GoogleMapAPI implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Coordinates updates intervals in sec
    private final static int UPDATE_INTERVAL = 1000; // 1 sec
    private final static int FATEST_INTERVAL = 500; // 0.5 sec
    private final static int DISPLACEMENT = 5; // 5 meters

    private final static float MIN_ACCURACY = 0;// 20 metros

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    private Context context;

    private double latitude;
    private double longitude;
    private float speed;

    protected GoogleMapAPI(Context context) {
        this.context = context;
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                longitude = mLastLocation.getLongitude();
                latitude = mLastLocation.getLatitude();
            }
        }
    }


    protected GoogleApiClient getClient(){
        return this.mGoogleApiClient;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public float getSpeed() {
        return speed;
    }

    public void onLocationChanged(Location location){
        if(location.getAccuracy() < MIN_ACCURACY){
            speed =location.getSpeed();
            latitude = location.getLongitude();
            longitude = location.getLongitude();
            this.onGetLocation(location);
        }
    }

    public boolean isConnected(){
        return mGoogleApiClient.isConnected();
    }


    public abstract void onGetLocation(Location location);

}
