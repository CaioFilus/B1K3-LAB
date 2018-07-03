package com.npdeas.b1k3labapp.Maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * Created by NPDEAS on 05/04/2018.
 */

public class Maps implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, GoogleMap.OnCameraMoveCanceledListener,GoogleMap.SnapshotReadyCallback,LocationListener{

    private Bundle bundle;
    private Context context;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<Polyline> lines;
    private ArrayList<Circle> circles;
    private Bitmap snapshoot;
    private OnLocationChanged onLocationChanged;

    private float zoom = DEFAULT_ZOOM;
    private boolean isStartRoute = false;
    private boolean myLocalization = true;

    private double latitude;
    private double longitude;
    private float speed;

    // Location updates intervals in sec
    private final static int UPDATE_INTERVAL = 1000; // 1 sec
    private final static int FATEST_INTERVAL = 500; // 0.5 sec
    private final static int DISPLACEMENT = 5; // 5 meters
    private final static float DEFAULT_ZOOM = 16;

    private final static float MIN_ACCURACY = 20;// 20 metros

    public Maps(Context context, Bundle bundle, MapView mapFragment) {
        this.context = context;
        this.bundle = bundle;
        mLastLocation = null;
        circles = new ArrayList<>();
        lines = new ArrayList<>();
        mapFragment.onCreate(bundle);
        mapFragment.getMapAsync(this);
        mapFragment.setEnabled(true);

    }

    public void addOnLocationChaged(OnLocationChanged onLocationChanged){
        this.onLocationChanged = onLocationChanged;
    }

    public void startRoute(){
        isStartRoute = true;
    }
    public void finishRoute(){
        isStartRoute = false;
        removeLines();
        removeCircles();
        map.clear();
    }
    public void setMyLocation(boolean location){
        myLocalization = location;
    }
    public void removeCircles(){
        for(int i = 0;i < circles.size();i++){

            circles.get(i).remove();
            circles.remove(i);
        }
        circles.clear();
    }
    public void removeLines(){
        for(int i = 0;i < lines.size();i++){
            lines.get(i).remove();
            lines.remove(i);
        }

        lines.clear();
    }
    public void addCircle(double latitude,double longitude){
        CircleOptions circleOptions = new CircleOptions();

        circleOptions.center(new LatLng(latitude,longitude));
        circleOptions.fillColor(Color.RED);
        circleOptions.radius(2);
        circles.add(map.addCircle(circleOptions));
    }
    public void AddLine(double lat1,double lon1,double lat2,double lon2){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(new LatLng(lat1,lon1), new LatLng(lat2,lon2));
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        lines.add(map.addPolyline(polylineOptions));
    }

    public Bitmap getBitmap(){

        return snapshoot;
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
    public void onLocationChanged(Location location) {

        if (location.getAccuracy() < MIN_ACCURACY) {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            //move map camera
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
            if(isStartRoute) {
                if (mLastLocation != null) {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.add(latLng, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                    polylineOptions.color(Color.RED);
                    polylineOptions.width(4);
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(latLng);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.radius(3);
                    circleOptions.strokeColor(Color.TRANSPARENT);
                    circles.add(map.addCircle(circleOptions));
                    lines.add(map.addPolyline(polylineOptions));
                }

                map.snapshot(this,snapshoot);
            }
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            speed = location.getSpeed();
            mLastLocation = location;
            if(onLocationChanged!=null){
                onLocationChanged.OnLocationChanged();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            map.setMyLocationEnabled(myLocalization);
        }
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
            map.snapshot(this,snapshoot);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude())));
                map.animateCamera(CameraUpdateFactory.zoomTo(zoom));

                longitude = mLastLocation.getLongitude();
                latitude = mLastLocation.getLatitude();
            }
        }
    }
    @Override
    public void onCameraMoveCanceled() {
        zoom = map.getCameraPosition().zoom;
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

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        snapshoot = bitmap;
    }

    public interface OnLocationChanged{
       void OnLocationChanged();
    }
}
