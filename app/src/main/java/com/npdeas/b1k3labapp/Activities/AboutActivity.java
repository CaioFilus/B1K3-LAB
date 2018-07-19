package com.npdeas.b1k3labapp.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.npdeas.b1k3labapp.Activities.Popup_Activityes.EasterEggPopup;
import com.npdeas.b1k3labapp.R;

import java.sql.Time;
import java.util.Timer;

/**
 * Created by NPDEAS on 03/04/2018.
 */

public class AboutActivity extends AppCompatActivity {

    private LinearLayout layout;
    private TextView editTextAbout;
    private int clickCount = 0;
    private Thread delayedThread;

    @Override
    protected void onCreate(Bundle salvedInstanceState) {
        super.onCreate(salvedInstanceState);
        setContentView(R.layout.activity_about);
        layout = findViewById(R.id.aboutLayout);
        editTextAbout = findViewById(R.id.editTextAbout);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setContentTitle("Smart Mobiliity")
                .setContentText("Já fez a sua rota de hoje ?")
                .setColor(getResources().getColor(R.color.blueLightNpDeas));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
        delayedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    clickCount = 0;
                } catch (Exception e) {
                    Log.i(this.getClass().getName(), e.getMessage());
                }
            }
        });

        editTextAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount >= 5) {
                    Intent intent = new Intent(AboutActivity.this, EasterEggPopup.class);
                    startActivity(intent);
                }
                if (!delayedThread.isAlive()) {
                    delayedThread.start();
                }
            }
        });


    }
}
