package com.tamn.app2uproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.tamn.app2uproject.Adapter.MyViewPagerAdapter;
import com.tamn.app2uproject.Model.EventItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class GiveAndTakeActivity extends AppCompatActivity  {

    FloatingActionButton fab;
    TabLayout tabs;
    ViewPager viewPager;
    private static final int GALLERY_REQUEST = 12;
    // Dialog to send data to firebase
    AlertDialog dialogSendData;

    RadioGroup rgGiveTake;
    RadioButton rbGive,rbTake;
    String category;
    //For Requesting image from Gallery
    GalleryPhoto galleryPhoto;
    //Dialog Image View
    ImageView ivUserChoice;
    //EditTexts
    EditText etDialogTitle;
    EditText etDialogContent;

    FirebaseUser currentUser;
    /**
     *Storage
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Constants.STORAGE_URL);
    //image path url
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_and_take);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.give_take));

        initLayout();
        Utils.checkIfPermissionNeeded(this);
        initEvents();

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsUserLogin();

            }
        });
    }

    private void initLayout() {
        galleryPhoto = new GalleryPhoto(this);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    /**
     * Custom Dialog to send data to Firebase
     */
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_give_take_post_item,null,false);
        etDialogTitle = (EditText) dialogView.findViewById(R.id.etDialogTitle);
        etDialogContent = (EditText) dialogView.findViewById(R.id.etDialogContent);
        Button btnItemSend = (Button) dialogView.findViewById(R.id.btnItemSend);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        rbGive = (RadioButton) dialogView.findViewById(R.id.rbGive);
        rbTake = (RadioButton) dialogView.findViewById(R.id.rbTake);
        rgGiveTake = (RadioGroup) dialogView.findViewById(R.id.rgGiveTake);

        ivUserChoice = (ImageView) dialogView.findViewById(R.id.ivUserChoice);

        rgGiveTake.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGive:
                        category = Constants.GIVE_ITEM ;
                        break;
                    case R.id.rbTake:
                        category = Constants.TAKE_ITEM;
                        break;
                    default:
                        break;
                }
            }
        });
        ivUserChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });

        btnItemSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if radio group is uncheck
                if(rgGiveTake.getCheckedRadioButtonId() == -1){
                   rbTake.setError(getString(R.string.choose_one_option));
                   rbGive.setError(getString(R.string.choose_one_option));
                   IOHelper.getAnimation(rbTake , Techniques.Shake );
                   IOHelper.getAnimation(rbGive , Techniques.Shake );

                }else if(etDialogTitle.getText().toString().equals("") && etDialogContent.getText().toString().equals("")) {
                    etDialogTitle.setError(getString(R.string.required_field));
                    IOHelper.getAnimation(etDialogTitle, Techniques.Shake);

                }else
                    uploadImageToServer();
            }
        });

        //Dismiss Dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSendData.dismiss();
            }
        });
        builder.setView(dialogView);
        dialogSendData = builder.show();
    }

    private void checkIsUserLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null){
            showDialogAlertLogin();
        }else showCustomDialog();
    }

    private void showDialogAlertLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.alert_message))
                // .setIcon(R.drawable.ic_wifi)
                .setCancelable(true)
                .setPositiveButton("יותר מאוחר", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(getResources().getString(R.string.Signin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(GiveAndTakeActivity.this, LoginActivity.class));
                    }
                }).show();
    }

    private void uploadImageToServer() {
        //Todo:show the user uploading text...
        ivUserChoice.setDrawingCacheEnabled(true);
        ivUserChoice.buildDrawingCache();
        //Convert ImageView to Bytes
        Bitmap bitmap = ivUserChoice.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,baos);
        byte[] data = baos.toByteArray();

        //Epoch time File name
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        //File name in Storages + directory
        StorageReference imageRef = storageRef.child(Constants.GIVE_TAKE_IMAGES + ts);
        //Upload to storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GiveAndTakeActivity.this, getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageUrl = downloadUrl.toString();
                uploadEverythingToDataBase();
            }
        });
    }

    private void uploadEverythingToDataBase() {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference(category);

        EventItem item = new EventItem(etDialogTitle.getText().toString(),
                etDialogContent.getText().toString(),
                imageUrl,
                IOHelper.gettingDate()
        );

        reference.push().setValue(item).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialogSendData.dismiss();

                if (task.isSuccessful()){
                    Toast.makeText(GiveAndTakeActivity.this, getString(R.string.upload_successfully), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(GiveAndTakeActivity.this, getString(R.string.error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri uri = data.getData();
                    galleryPhoto.setPhotoUri(uri);

                    String path = galleryPhoto.getPath();

                    try {
                        Bitmap bitmap = ImageLoader.init()
                                .from(path).requestSize(400,400).getBitmap();

                        ivUserChoice.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
