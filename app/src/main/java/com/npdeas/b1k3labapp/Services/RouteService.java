package com.npdeas.b1k3labapp.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.npdeas.b1k3labapp.Bluetooth.B1k3Lab;
import com.npdeas.b1k3labapp.Bluetooth.IotDevice;
import com.npdeas.b1k3labapp.Broadcast.RouteBroadcastReciver;
import com.npdeas.b1k3labapp.Database.AppDatabase;
import com.npdeas.b1k3labapp.Database.Daos.RouteDao;
import com.npdeas.b1k3labapp.Database.Daos.RouteNodeDao;
import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.Database.Structs.RouteNode;
import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Maps.Coordinates;
import com.npdeas.b1k3labapp.Maps.Map;
import com.npdeas.b1k3labapp.Notification.RouteNotification;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.Sensors.Microphone;
import com.npdeas.b1k3labapp.Utils.FileUtils;

import java.util.Date;

/**
 * Created by NPDEAS on 5/16/2018.
 */

public class RouteService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private Map map;
    private static RouteService thisObject = null;
    private static ServiceListener serviceListener;
    private boolean isStartService = false;
    //private RouteNotification notification;
    private Coordinates coordiantes;
    private Microphone mic;
    private long time;
    private B1k3Lab bluetooth;
    private Route route;

    public static final String BROADCAST = "com.npdeas.b1k3labapp.android.action.ROUTE";
    public static final String ACTION_FIELD = "action";

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            String action = intent.getStringExtra(ACTION_FIELD);
            if (action != null) {
                if (action.equals(RouteBroadcastReciver.START_ROUTE_ACTION)) {
                    startTracking(ModalType.BIKE);
                } else if (action.equals(RouteBroadcastReciver.STOP_ROUTE_ACTION)) {
                    stopTracking(null);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        thisObject = this;
        this.registerReceiver(stopReceiver, new IntentFilter(BROADCAST));
        //notification = RouteNotification.getInstance(this);
        coordiantes = new Coordinates(this);
        mic = new Microphone();
        bluetooth = new B1k3Lab(this, User.getCurrentUser().bluetooth);
        time = System.currentTimeMillis();
        mic.startRecord();

        coordiantes.addOnlocationChangedEvent(new Coordinates.OnLocationChanged() {
            @Override
            public void OnLocationChanged() {
                if (isStartService) {
                    route.routeNodes.add(loadRouteNode());
                }
            }
        });
        if (serviceListener != null) {
            serviceListener.onCreateService(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thisObject = null;
        mic.stopRecording();
        bluetooth.stop();
        unregisterReceiver(stopReceiver);
        //coordiantes.finishRoute();
    }

    public static RouteService getInstance() {
        return thisObject;
    }

    public void startTracking(ModalType modalType) {
        //notification.switchButtonState();
        route = new Route();
        route.modal = modalType;
        route.startDate = new Date();
        if (map == null) {
            map = Map.getInstance();
        }
        mic.startRecord();
        map.startRoute();
        isStartService = true;
    }

    public void stopTracking(Bitmap bitmap) {

        //notification.switchButtonState();
        map.finishRoute();

//        mic.stopRecording();
        isStartService = false;

        if (route.routeNodes.size() <= 5) {
            route.finishDate = new Date();
            RouteDao routeInfoDao = AppDatabase.getAppDatabase(this).routeDao();
            RouteNodeDao routeNodeDao = AppDatabase.getAppDatabase(this).routeNodeDao();
            route.id = routeInfoDao.insertAll(route);
            route.name = String.valueOf(route.id);
            for (RouteNode routeNode : route.routeNodes) {
                routeNode.routeId = route.id;
            }
            RouteNode routeNodes[] = new RouteNode[route.routeNodes.size()];
            routeNodes = route.routeNodes.toArray(routeNodes);
            routeNodeDao.insertAll(routeNodes);
            FileUtils.saveImg(this, bitmap, route.name);
            route.img = route.name;
            routeInfoDao.update(route);

        }
    }

    public boolean isRouting() {
        return isStartService;
    }

    public void releaseMicrophone() {
        mic.stopRecording();
    }

    public Route getRoute() {
        return route;
    }

    public IotDevice getIotDevice() {
        return bluetooth;
    }

    public RouteNode getRouteNode() {
        return loadRouteNode();
    }

    public void connectBluetooth() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                bluetooth.connect(User.getCurrentUser().bluetooth);
                return null;
            }
        }.execute();
    }

    public void disconnectBluetooth() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                bluetooth.disconnect();
                return null;
            }
        }.execute();
    }


    private RouteNode loadRouteNode() {
        RouteNode routeNode = new RouteNode();
        routeNode.latitude = coordiantes.getLatitude();
        routeNode.longetude = coordiantes.getLongitude();
        routeNode.speed = coordiantes.getSpeed();
        routeNode.db = (int) mic.getDbMobilAvarage();
        routeNode.overtaking = bluetooth.getDistance();
        routeNode.time = System.currentTimeMillis() - time;
        routeNode.temperature = bluetooth.getTemperature();
        routeNode.humidity = bluetooth.getHumidity();

        return routeNode;
    }

    public interface ServiceListener {
        void onCreateService(RouteService routeService);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public RouteService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RouteService.this;
        }
    }
}
