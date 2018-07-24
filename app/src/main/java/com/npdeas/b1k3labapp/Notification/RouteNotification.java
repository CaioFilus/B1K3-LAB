package com.npdeas.b1k3labapp.Notification;

import android.annotation.TargetApi;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.npdeas.b1k3labapp.Activities.Fragments.StartRouteFragment;
import com.npdeas.b1k3labapp.Activities.MainActivity;
import com.npdeas.b1k3labapp.Broadcast.RouteBroadcastReciver;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Utils.CircleTransform;

import java.util.Random;

public class RouteNotification {

    private static final String NOTIFICATION_TAG = "ROUTE_CONTROLLER";

    private static RouteNotification thisObject;
    private Notification notification;
    private NotificationCompat.Builder builder;
    private Intent commandIntent;
    private Context context;
    private boolean buttonState = false;

    private RouteNotification(Context context) {
        thisObject = this;
        this.context = context;

        final Bitmap picture = selectPicture(context);
        //final String ticker = exampleString;
        final String title = "SmartMobility";
        final String text = ""/*res.getString(
                R.string.new_message_notification_placeholder_text_template, exampleString)*/;

        //Craido a ação do bottao
        NotificationCompat.Action action = getAction(RouteBroadcastReciver.START_ROUTE_ACTION);

        //criando a pedint Intend do Main Activity
        PendingIntent pMainActivity = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setOnlyAlertOnce(false)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(picture)
                .setContentIntent(pMainActivity)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0))
                .addAction(action)
                .setOngoing(true);
        this.builder = builder;
        this.notification = builder.build();
        notifyNotification();
    }

    public static RouteNotification getInstance(Context context) {
        if (thisObject == null) {
            return new RouteNotification(context);
        } else {
            return thisObject;
        }
    }

    public Notification getNotification() {
        return notification;
    }

    public Notification switchButtonState() {
        NotificationCompat.Action action;
        if(!buttonState){
            action = getAction(RouteBroadcastReciver.STOP_ROUTE_ACTION);
        }else{
            action = getAction(RouteBroadcastReciver.START_ROUTE_ACTION);
        }
        buttonState = !buttonState;
        builder.mActions.clear();
        builder.addAction(action);
        notification = builder.build();
        notifyNotification();
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notifyNotification() {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, 0, notification);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void cancel() {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);

    }

    private NotificationCompat.Action getAction(String action){
        commandIntent = new Intent(context, RouteBroadcastReciver.class);
        commandIntent.setType(Notification.class.getName());

        if(action.equals(RouteBroadcastReciver.START_ROUTE_ACTION)){
            commandIntent.setAction(RouteBroadcastReciver.START_ROUTE_ACTION);

            PendingIntent pIntentStart = PendingIntent.getBroadcast(context, 1,
                    commandIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            return new NotificationCompat.Action(android.R.drawable.ic_media_play, "Iniciar",
                    pIntentStart);
        }else{
            commandIntent.setAction(RouteBroadcastReciver.STOP_ROUTE_ACTION);

            PendingIntent pIntentStart = PendingIntent.getBroadcast(context, 1,
                    commandIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            return new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Parar",
                    pIntentStart);
        }
    }

    private static Bitmap selectPicture(Context context) {
        Bitmap result;
        final Resources res = context.getResources();
        Random random = new Random();
        int randomInt = ((random.nextInt(3))) + 1;
        switch (randomInt) {
            case 1:
                result = BitmapFactory.decodeResource(res, R.mipmap.bike_background_1);
                break;
            case 2:
                result = BitmapFactory.decodeResource(res, R.mipmap.bike_background_2);
                break;
            case 3:
                result = BitmapFactory.decodeResource(res, R.mipmap.bike_background_3);
                break;
            case 4:
                result = BitmapFactory.decodeResource(res, R.mipmap.bike_background_4);
                break;
            default:
                return null;
        }
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            return result;
        }
        return new CircleTransform().transform(result);
    }


}
