package com.tamn.app2uproject.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.IOHelper;
import com.tamn.app2uproject.Model.EventItem;
import com.tamn.app2uproject.R;
import com.tamn.app2uproject.Utils;
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
    StorageReference storageRef = storage.getReferenceFromUrl(Constants.STORAGE_URL);
    //image path url
    String imageUrl;
    String title;
    String content;
    String eventDate;

    public UploadEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateView = inflater.inflate(R.layout.fragment_upload__events, container, false);

        etEventTitle = (EditText) inflateView.findViewById(R.id.etEventTitle);
        etEventContent = (EditText) inflateView.findViewById(R.id.etEventContent);
        ivUploadImage = (ImageView) inflateView.findViewById(R.id.ivUploadImage);
        btnUploadEvent = (Button) inflateView.findViewById(R.id.btnUploadEvent);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.upload_event));

        initLayout();
        Utils.checkIfPermissionNeeded(getActivity());
        initEvents();


        return inflateView;
    }

    private void initLayout() {
        galleryPhoto = new GalleryPhoto(getContext());
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
                if (title.equals("") && content.equals("")){

                    etEventTitle.setError(getString(R.string.required_field));
                    etEventContent.setError(getString(R.string.required_field));
                    IOHelper.getAnimation(etEventTitle , Techniques.Shake );
                    IOHelper.getAnimation(etEventContent , Techniques.Shake );
                }else {
                    uploadImageToServer();


                etEventTitle.getText().clear();
                etEventContent.getText().clear();
                }
            }
        });
    }

    private void uploadImageToServer() {
        //Todo:show the user uploading text...
        // Get the data from an ImageView as bytes
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
        StorageReference imageRef = storageRef.child(Constants.EVENTS_IMAGES + ts);
        //Upload to storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        eventDate = IOHelper.gettingDate();
        EventItem item = new EventItem(title,content,url,eventDate );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.EVENTS);

        reference.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),getResources().getString(R.string.upload_successfully) , Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error)+ e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
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
