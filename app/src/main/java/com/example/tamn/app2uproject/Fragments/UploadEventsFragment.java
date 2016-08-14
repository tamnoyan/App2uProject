package com.example.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadEventsFragment extends Fragment {

    EditText etEventTitle, etEventContent;
    ImageView ivEvent;
    Button btnUploadEvent;

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
        ivEvent = (ImageView) inflateView.findViewById(R.id.ivEvent);
        btnUploadEvent = (Button) inflateView.findViewById(R.id.btnUploadEvent);

        initEvents();


        return inflateView;
    }

    private void initEvents() {
        btnUploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etEventTitle.getText().toString();
                String content = etEventContent.getText().toString();

                uploadDataToFirebase(title,content);
                etEventTitle.setText("");
                etEventContent.setText("");

            }
        });
    }

    private void uploadDataToFirebase(String title, String content) {
        MessageItem item = new MessageItem(title,content);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.EVENTS);

        reference.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Your Event has been Uploaded successfully!!!", Toast.LENGTH_LONG).show();
                //Log.d("Tam", "sucess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
