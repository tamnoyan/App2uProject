package com.example.tamn.app2uproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Tamn on 07/08/2016.
 */
public class AppManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*App Events let you measure installs on your mobile app ads,
          create high value audiences for targeting,
          and view analytics including user demographics*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
