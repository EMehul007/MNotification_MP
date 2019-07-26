package com.example.mnotification.Notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;


public class NotificationService extends NotificationListenerService {

    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();


    }

    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {

        try {
            String pack = sbn.getPackageName();
            String ticker = "";
            if (sbn.getNotification().tickerText != null) {
                ticker = sbn.getNotification().tickerText.toString();
            }

            Toast.makeText(this, "Come__", Toast.LENGTH_SHORT).show();

            Log.e("*****=====", String.valueOf(sbn.getNotification().actions));
            Log.e("*****=====", String.valueOf(pack));

            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString("android.title");
            String text = extras.getCharSequence("android.text").toString();



            Log.i("Package", pack);
            Log.i("Ticker", ticker);
            Log.i("Title", title);
            Log.i("Text", text);
            Log.i("Bundle", extras.toString());

            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("package", pack);
            msgrcv.putExtra("ticker", ticker);
            msgrcv.putExtra("title", title);
            msgrcv.putExtra("text", text);


            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


        } catch (Exception e) {
            Toast.makeText(context, "Error*****" + e.getMessage().toString(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg", "Notification Removed");

    }


}
