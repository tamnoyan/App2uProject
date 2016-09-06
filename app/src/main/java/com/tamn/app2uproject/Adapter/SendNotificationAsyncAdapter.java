package com.tamn.app2uproject.Adapter;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Tamn on 02/09/2016.
 */
public class SendNotificationAsyncAdapter extends AsyncTask<String,Integer,String> {

    private String title;
    private String content;

    public SendNotificationAsyncAdapter(String title,String content){
        this.title = title;
        this.content = content;
    }


    //Before we openning secondary thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /**
         * Progress Indicator start !!!
         */
    }

    @Override
    protected String doInBackground(String... url) {
        String googleEndPoint = url[0];
        //Json data String
        String jsonData = "{\"to\":\"/topics/news\",\"data\":{\"title\":\"" + title + "\",\"message\":\"" + content + "\"}}";

        //Convert JSon Data to Byte array
        byte[] postData = jsonData.getBytes(Charset.forName("UTF-8"));

        try {
            URL urlGoogle =  new URL(googleEndPoint);
            HttpURLConnection con = (HttpURLConnection) urlGoogle.openConnection();
            //by default true
            con.setDoInput(true);
            //Write Data To server
            con.setDoOutput(true);
            //Redirect url
            con.setInstanceFollowRedirects(true);
            //Requesting Post
            con.setRequestMethod("POST");
            //Headers
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","key=AIzaSyAssvD_hSJlzdtaR3rDmrAWtTas5q1HP00");
            con.setUseCaches(true);
            //Writing to server
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(postData);
            /************************************************************************************/
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            String output = "";
            while ((line = reader.readLine()) != null){
                output += line;
            }

            return output;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        /**
         * Stop Progress Indicator
         */
    }
}
