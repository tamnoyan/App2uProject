package com.tamn.app2uproject;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;

/**
 * Created by Tamn on 22/08/2016.
 */
public class IOHelper {


    /***
     * Getting the current Time and date
     * @return dateStr, string in a format of (hh:mm ,dd/mm/yyyy)
     */
    public static String gettingDate(){
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, kk:mm");
        String dateStr = "(" +sdf.format(date) + ")";

        return dateStr;
    }


    /***
     *  set animation on views
     * @param view - the view that we want to operate the animation on
     * @param techniques - which animation to operate e.g: Techniques.Shake
     */
    public static void getAnimation(View view, Techniques techniques){
        YoYo.with(techniques)
            .duration(700)
            .playOn(view) ;
    }

}
