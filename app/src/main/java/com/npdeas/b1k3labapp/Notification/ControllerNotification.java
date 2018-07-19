package com.npdeas.b1k3labapp.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;



/**
 * Created by NPDEAS on 7/12/2018.
 */

public class ControllerNotification{

    private Notification notification;
    private NotificationManager notificationManager;
    private Notification.Action action;

    public ControllerNotification(Context context) {

        /*Intent itActionStart = new Intent(context, ActivityRouteReceiver.class);
        itActionStart.setAction(RouteActionReciver.START_ROUTE_ACTION);

        PendingIntent pIntentStart = PendingIntent.getBroadcast(context, 1,
                itActionStart, PendingIntent.FLAG_UPDATE_CURRENT);

        action = new Notification.Action(android.R.drawable.ic_media_play, "Iniciar", pIntentStart);
        Notification.Builder notification = new Notification.Builder(context)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                // Add media control buttons that invoke intents in your media service
                .addAction(android.R.drawable.ic_media_play, "Iniciar", pIntentStart) // #0
                .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0))
                .setContentTitle("SmartMobility");

        /*NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, notification.build());*/
    }
}
