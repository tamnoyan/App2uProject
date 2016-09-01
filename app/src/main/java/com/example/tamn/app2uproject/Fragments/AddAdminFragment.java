package com.example.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tamn.app2uproject.Constants;
import com.example.tamn.app2uproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AddAdminFragment extends Fragment {

    EditText etAdmin1;
    Button btnUpdateAdmin;

    public AddAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_add_admin, container, false);
        etAdmin1 = (EditText) inflate.findViewById(R.id.etAdmin1);
        btnUpdateAdmin = (Button) inflate.findViewById(R.id.btnUpdateAdmin);
        initEvents();

        return inflate;
    }

    private void initEvents() {
        btnUpdateAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAdmins();
            }
        });

    }

    /**
     * Save Admins to Firebase !!!
     */
    public void addAdmins(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.ADMINDS);

        HashMap<String,String> adminsList = new HashMap<>();
        adminsList.put("admin1",etAdmin1.getText().toString());
        adminsList.put("admin2","tam@gmail.com");
        /*adminsList.put("admin3","aba@yahoo.com");
        adminsList.put("admin4","aba@yahoo.com");*/

        reference.setValue(adminsList).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("DDebug",e.getMessage());
            }
        });
    }
}
