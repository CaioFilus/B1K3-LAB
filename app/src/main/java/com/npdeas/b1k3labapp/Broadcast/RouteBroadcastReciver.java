package com.npdeas.b1k3labapp.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.npdeas.b1k3labapp.UI.Activities.MainActivity;
import com.npdeas.b1k3labapp.Maps.Map;
import com.npdeas.b1k3labapp.Notification.RouteNotification;
import com.npdeas.b1k3labapp.Services.RouteService;

/**
 * Created by NPDEAS on 7/9/2018.
 */

public class RouteBroadcastReciver extends BroadcastReceiver {
    public static final String START_ROUTE_ACTION = "android.intent.action.START_ROUTE";
    public static final String STOP_ROUTE_ACTION = "android.intent.action.STOP_ROUTE";

    public static final String CALLER_KEY = "caller";

    private RouteService routeService;
    private Map map;
    private MainActivity mainActivity;
    private RouteNotification notification;

    public RouteBroadcastReciver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        routeService = RouteService.getInstance();
        map = Map.getInstance();
        notification = RouteNotification.getInstance(context);
        String action = intent.getAction();

        if (action.equals(START_ROUTE_ACTION)) {
            //routeService.startTracking();
        } else if (action.equals(STOP_ROUTE_ACTION)) {
            routeService.stopTracking(map.getBitmap());
        }
        mainActivity.switchButton();
        notification.switchButtonState();
    }
}
