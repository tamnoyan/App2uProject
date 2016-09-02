package com.tamn.app2uproject;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Tamn on 02/09/2016.
 */
public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        for (String key : data.keySet()) {
            Log.d("DDebugging",key + ":" + data.get(key));
        }

        String title = data.get("title");
        String message = data.get("message");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(this).notify(0,builder.build());

    }
}
