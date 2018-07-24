package com.npdeas.b1k3labapp.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Maps.Coordinates;
import com.npdeas.b1k3labapp.Maps.Map;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Route;
import com.npdeas.b1k3labapp.Sensors.Microphone;

/**
 * Created by NPDEAS on 5/16/2018.
 */

public class RouteService extends Service {

    private Map map;
    private static RouteService thisObject = null;
    private static ServiceListener serviceListener;
    private boolean isStartService = false;
    private Coordinates coordiantes;
    private Microphone mic;
    private long time;
    private Bluetooth bluetooth;
    private Route route;

    public RouteService() {
        super();
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thisObject == null) {
            thisObject = this;
            coordiantes = new Coordinates(this);
            mic = new Microphone();
            bluetooth = new Bluetooth();
            time = System.currentTimeMillis();
            route = new Route(this);
            mic.startRecord();


            coordiantes.addOnlocationChangedEvent(new Coordinates.OnLocationChanged() {
                @Override
                public void OnLocationChanged() {
                    if (isStartService) {
                        route.addRouteNode(loadRouteNode());
                    }
                }
            });
        }
        if (serviceListener != null) {
            serviceListener.onCreateService(this);
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thisObject = null;
        mic.stopRecording();
        //coordiantes.finishRoute();
    }
    public static RouteService getInstance(){
        return thisObject;
    }

    public void startTracking() {
        if(map == null){
            map = Map.getInstance();
        }
        map.startRoute();
        mic.startRecord();
        isStartService = true;
    }

    public void stopTracking(Bitmap bitmap) {
        map.finishRoute();
        mic.stop();
        isStartService = false;
        route.saveRoute(bitmap);
    }

    public static void addServiceListener(ServiceListener serviceListener) {
        RouteService.serviceListener = serviceListener;
    }

    public RouteNode getRouteNode() {
        return loadRouteNode();
    }

    private RouteNode loadRouteNode() {
        RouteNode routeNode = new RouteNode();
        routeNode.setLatitude(coordiantes.getLatitude());
        routeNode.setLongetude(coordiantes.getLongitude());
        routeNode.setSpeed(coordiantes.getSpeed());
        routeNode.setDb((int) mic.getDbMobilAvarage());
        routeNode.setOvertaking(bluetooth.getDistance());
        routeNode.setTime(System.currentTimeMillis() - time);
        return routeNode;
    }

    public interface ServiceListener {
        void onCreateService(RouteService routeService);
    }
}
