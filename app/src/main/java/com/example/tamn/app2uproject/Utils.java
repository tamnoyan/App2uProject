package com.example.tamn.app2uproject;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Tamn on 31/08/2016.
 */
public class Utils {

    private static final int REQUEST_STORAGE_CODE = 5;
    public static void checkIfPermissionNeeded(Activity activity) {
        int result = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            String[] permission = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity,permission,REQUEST_STORAGE_CODE);
        }
    }
}
