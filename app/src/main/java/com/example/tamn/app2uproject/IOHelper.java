package com.example.tamn.app2uproject;

import java.text.SimpleDateFormat;

/**
 * Created by Tamn on 22/08/2016.
 */
public class IOHelper {


    public static String gettingDate(){

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, kk:mm");
        String dateStr = "(" +sdf.format(date) + ")";
        //tvDisplayDate.setText(dateString);

        return dateStr;
    }

}
