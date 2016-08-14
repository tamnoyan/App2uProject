package com.example.tamn.app2uproject.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tamn.app2uproject.Constants;
import com.example.tamn.app2uproject.Model.MessageItem;
import com.example.tamn.app2uproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadEventsFragment extends Fragment {

    private static final int REQUEST_STORAGE_CODE = 5;
    private static final int GALLERY_REQUEST = 7;
    EditText etEventTitle, etEventContent;
    ImageView ivUploadImage;
    Button btnUploadEvent;

    //For Requesting image from Gallery
    GalleryPhoto galleryPhoto;

   /***
    * Storage
    */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://app2uproject.appspot.com");
    //image path url
    String imageUrl;
    String title;
    String content;

    public UploadEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateView = inflater.inflate(R.layout.fragment_upload__events_, container, false);

        etEventTitle = (EditText) inflateView.findViewById(R.id.etEventTitle);
        etEventContent = (EditText) inflateView.findViewById(R.id.etEventContent);
        ivUploadImage = (ImageView) inflateView.findViewById(R.id.ivUploadImage);
        btnUploadEvent = (Button) inflateView.findViewById(R.id.btnUploadEvent);

        initLayout();
        checkIfPermissionNeeded();
        initEvents();


        return inflateView;
    }

    private void initLayout() {
        galleryPhoto = new GalleryPhoto(getContext());
    }

    private void checkIfPermissionNeeded() {
        int result = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED){

            //ask for permission
            showPermissionSystemDialog();
        }
    }

    private void showPermissionSystemDialog() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(getActivity(),permission,REQUEST_STORAGE_CODE);
    }

    private void initEvents() {

        ivUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST );
            }
        });


        btnUploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etEventTitle.getText().toString();
                content = etEventContent.getText().toString();
                uploadImageToServer();

                //TODO: fix

                etEventTitle.setText("");
                etEventContent.setText("");

            }
        });
    }

    private void uploadImageToServer() {
        //Todo:show the user uploading text...
        ivUploadImage.setDrawingCacheEnabled(true);
        ivUploadImage.buildDrawingCache();
        //Convert ImageView to Bytes
        Bitmap bitmap = ivUploadImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,baos);
        byte[] data = baos.toByteArray();

        //Epoch time File name
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        //File name in Storages + directory
        StorageReference imageRef = storageRef.child("images/" + ts);
        //Upload to storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageUrl = downloadUrl.toString();
                //uploadEverythingToDataBase();
                uploadDataToFirebase(title,content, imageUrl);
            }
        });
    }

    private void uploadDataToFirebase(String title, String content, String url ) {
        MessageItem item = new MessageItem(title,content,url  );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.EVENTS);

        reference.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Your Event has been Uploaded successfully!!!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1){
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri uri = data.getData();
                    galleryPhoto.setPhotoUri(uri);

                    String path = galleryPhoto.getPath();

                    try {
                        Bitmap bitmap = ImageLoader.init()
                                .from(path).requestSize(400,400).getBitmap();

                        ivUploadImage.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


            }
        }

    }
}
