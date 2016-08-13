package com.example.tamn.app2uproject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity {

    Button btnUploadImage;
    ImageView ivSelected,ivDownloaded;
    Button btnDownload;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    //https://console.firebase.google.com/project/app2uproject/storage/files/images/
    StorageReference storageRef = storage.getReferenceFromUrl("gs://app2uproject.appspot.com");

    String nameOfFile;
    String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        initLayout();
        initEvent();
    }

    private void initEvent() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPicture();
            }
        });


        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPicture();
            }
        });
    }

    private void downloadPicture() {
        btnDownload.setText("Downloading Picture");

        StorageReference pathRef = storageRef.child("images/" + nameOfFile);

        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                btnDownload.setText("Success");

                Picasso.with(getApplicationContext()).load(uri).into(ivDownloaded);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPicture() {
        btnUploadImage.setText("uploading now...");
        Long ls = System.currentTimeMillis()/1000;
        nameOfFile = ls.toString();

        ivSelected.setDrawingCacheEnabled(true);
        ivSelected.buildDrawingCache();

        Bitmap bitmap = ivSelected.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,60,baos);
        byte[] data = baos.toByteArray();

        StorageReference imageRef = storageRef.child("images/" + nameOfFile);
        //Upload Process
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                btnUploadImage.setText("Finished Uploading..");
                //Getting the Url
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageURL = downloadUrl.toString();
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initLayout() {
        ivDownloaded = (ImageView) findViewById(R.id.ivDownloaded);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        ivSelected = (ImageView) findViewById(R.id.ivSelected);
        btnDownload = (Button) findViewById(R.id.btnDownload);
    }
}
