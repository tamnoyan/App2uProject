package com.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AddAdminFragment extends Fragment {

    TextView tvAdmin1;
    EditText  etAdmin2, etAdmin3, etAdmin4;
    Button btnUpdateAdmin;
    ImageView ivEdit2, ivEdit3 ,ivEdit4;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(Constants.ADMINDS);

    public AddAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_add_admin, container, false);
        tvAdmin1 = (TextView) inflate.findViewById(R.id.tvAdmin1);
        etAdmin2 = (EditText) inflate.findViewById(R.id.etAdmin2);
        etAdmin3 = (EditText) inflate.findViewById(R.id.etAdmin3);
        etAdmin4 = (EditText) inflate.findViewById(R.id.etAdmin4);

        ivEdit2 = (ImageView) inflate.findViewById(R.id.ivEdit2);
        ivEdit3 = (ImageView) inflate.findViewById(R.id.ivEdit3);
        ivEdit4 = (ImageView) inflate.findViewById(R.id.ivEdit4);

        btnUpdateAdmin = (Button) inflate.findViewById(R.id.btnUpdateAdmin);

        initEvents();

        return inflate;
    }

    private void initEvents() {
        // get admin list from db
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> admins = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    admins.add(child.getValue().toString());
                }

                tvAdmin1.setText(admins.get(0));
                etAdmin2.setText(admins.get(1));
                etAdmin3.setText(admins.get(2));
                etAdmin4.setText(admins.get(3));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

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
        HashMap<String,String> adminsList = new HashMap<>();

        adminsList.put("admin1","tam@gmail.com");
        adminsList.put("admin2","avigail@gmail.com");
        adminsList.put("admin3",etAdmin3.getText().toString());
        adminsList.put("admin4",etAdmin4.getText().toString());


        reference.setValue(adminsList).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), ""+getString(R.string.sent_successfully), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
