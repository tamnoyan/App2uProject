package com.tamn.app2uproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Tamn on 22/08/2016.
 */
public class IOHelper {

    static String imageUrl;
    public static String uploadImageToServer(ImageView imageView, final Context context, String storageFolderName) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.STORAGE_URL);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        //Convert ImageView to Bytes
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();

        //Epoch time File name
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        //File name in Storages + directory
        StorageReference imageRef = storageRef.child(storageFolderName+"/" + ts);
        //Upload to storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( context, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener((Activity) context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageUrl = downloadUrl.toString();
            }
        });

        return imageUrl;
    }


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
