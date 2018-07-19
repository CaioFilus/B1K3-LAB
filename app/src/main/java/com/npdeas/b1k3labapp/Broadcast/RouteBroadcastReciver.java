package com.npdeas.b1k3labapp.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.npdeas.b1k3labapp.Activities.MainActivity;
import com.npdeas.b1k3labapp.Maps.FragmentGPS;
import com.npdeas.b1k3labapp.Services.RouteService;

/**
 * Created by NPDEAS on 7/9/2018.
 */

public class RouteBroadcastReciver extends BroadcastReceiver{
    public static final String START_ROUTE_ACTION = "android.intent.action.START_ROUTE";
    public static final String STOP_ROUTE_ACTION = "android.intent.action.STOP_ROUTE";

    private RouteService routeService;
    private FragmentGPS fragmentGPS;
    private Button button;

    @Override
    public void onReceive(Context context, Intent intent) {
        routeService = RouteService.getInstance();
        fragmentGPS = FragmentGPS.getInstance();

        String action = intent.getAction();
        if (action.equals(START_ROUTE_ACTION)) {
            routeService.startTracking();
        } else if (action.equals(STOP_ROUTE_ACTION)) {
            routeService.stopTracking(fragmentGPS.getBitmap());

        }
        //This is used to close the notification tray
        /*Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);*/
    }

}
