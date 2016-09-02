package com.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.wang.avi.AVLoadingIndicatorView;

public class WelcomeActivity extends AppCompatActivity {

    AVLoadingIndicatorView avi;
    private static boolean splashLoaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        if (!splashLoaded) {
            setContentView(R.layout.activity_welcome);

            avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
            startAnim();

            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 3000);
            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }

    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }
}
