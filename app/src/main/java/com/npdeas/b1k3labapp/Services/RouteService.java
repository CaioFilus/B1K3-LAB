package com.npdeas.b1k3labapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.npdeas.b1k3labapp.Activities.MainActivity;
import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Broadcast.RouteBroadcastReciver;
import com.npdeas.b1k3labapp.Maps.Maps;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Route;
import com.npdeas.b1k3labapp.Sensors.Microphone;

/**
 * Created by NPDEAS on 5/16/2018.
 */

public class RouteService extends Service {

    private static RouteService thisObject = null;
    private static ServiceListener serviceListener;
    private boolean isStartService = false;
    private Maps map;
    private Microphone mic;
    private long time;
    private Bluetooth bluetooth;
    private Route route;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thisObject == null) {
            thisObject = this;
            map = new Maps(this);
            mic = new Microphone();
            bluetooth = new Bluetooth();
            time = System.currentTimeMillis();
            route = new Route(this);
            mic.startRecord();

            final Resources res = this.getResources();
            final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.bike_background_1);
            Intent itActionStart = new Intent(this, RouteBroadcastReciver.class);
            Intent itActionStop = new Intent(this, RouteBroadcastReciver.class);
            itActionStart.setAction(RouteBroadcastReciver.START_ROUTE_ACTION);
            itActionStop.setAction(RouteBroadcastReciver.STOP_ROUTE_ACTION);

            PendingIntent pIntentStart = PendingIntent.getBroadcast(this, 1,
                    itActionStart, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pIntentStop = PendingIntent.getBroadcast(this, 1,
                    itActionStop, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder notification = new Notification.Builder(this)
                    // Show controls on lock screen even when user hides sensitive content.
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(android.R.drawable.ic_dialog_map)

                    .setLargeIcon(picture)
                    // Add media control buttons that invoke intents in your media service
                    .addAction(android.R.drawable.ic_media_play, "Iniciar", pIntentStart) // #0
                    .addAction(android.R.drawable.ic_media_pause, "Parar", pIntentStop)
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0))
                    .setContentTitle("SmartMobility")
                    .setOngoing(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

            mNotificationManager.notify(0, notification.build());


            map.addOnlocationChangedEvent(new Maps.OnLocationChanged() {
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

        //map.finishRoute();
    }
    public static RouteService getInstance(){
        return thisObject;
    }

    public void startTracking() {
        isStartService = true;
    }

    public void stopTracking(Bitmap bitmap) {
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
        routeNode.setLatitude(map.getLatitude());
        routeNode.setLongetude(map.getLongitude());
        routeNode.setSpeed(map.getSpeed());
        routeNode.setDb((int) mic.getDbMobilAvarage());
        routeNode.setOvertaking(bluetooth.getDistance());
        routeNode.setTime(System.currentTimeMillis() - time);
        return routeNode;
    }

    public interface ServiceListener {
        void onCreateService(RouteService routeService);
    }
}
