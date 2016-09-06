package com.tamn.app2uproject.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.LoginActivity;
import com.tamn.app2uproject.Model.UserDetails;
import com.tamn.app2uproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    ImageView ivDetailsImage;
    EditText etDetailsName, etDetailsEmail, etDetailsPassword;
    Button btnSignout, btnConnect;

    SwitchCompat scNotification;
    SharedPreferences pref;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ivDetailsImage = (ImageView) view.findViewById(R.id.ivDetailsImage);
        etDetailsName = (EditText) view.findViewById(R.id.etDetailsName);
        etDetailsEmail = (EditText) view.findViewById(R.id.etDetailsEmail);
        etDetailsPassword = (EditText) view.findViewById(R.id.etDetailsPassword);
        btnSignout = (Button) view.findViewById(R.id.btnSignout);
        btnConnect = (Button) view.findViewById(R.id.btnConnect);
        scNotification = (SwitchCompat) view.findViewById(R.id.scNotification);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));

        gettingUserDetails();
        initLayout();
        initEvent();

        pref = getActivity().getSharedPreferences(Constants.APPLICATION_PREF, Context.MODE_PRIVATE);

        getSwitchCompatFromMemory();
        switchAction();

        return view;
    }

    private void initLayout() {
        etDetailsName.setFocusable(false);
        etDetailsEmail.setFocusable(false);
        etDetailsPassword.setFocusable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            btnSignout.setVisibility(View.GONE);
            btnConnect.setVisibility(View.VISIBLE);
        }else {
            btnSignout.setVisibility(View.VISIBLE);
            btnConnect.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        etDetailsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDetailsName.setFocusableInTouchMode(true);
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                btnSignout.setVisibility(View.VISIBLE);
                btnConnect.setVisibility(View.GONE);
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                    btnSignout.setVisibility(View.GONE);
                    btnConnect.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gettingUserDetails() {
        FirebaseUser currentUser  = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            etDetailsEmail.setText(currentUser.getEmail());

            if (currentUser.getPhotoUrl() != null) {
                Picasso.with(getActivity()).load(currentUser.getPhotoUrl()).into(ivDetailsImage);
                etDetailsName.setText(currentUser.getDisplayName());

            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference(Constants.USERS).child(currentUser.getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                        try {
                            if (userDetails.getUsername() != null) {
                                etDetailsName.setText(userDetails.getUsername());
                            }
                            if (userDetails.getImageUrl() != null) {
                                Picasso.with(getActivity()).load(userDetails.getImageUrl()).into(ivDetailsImage);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getString(R.string.error)+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        ref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    private void switchAction() {
        scNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                saveStateNotification(b);
            }
        });
    }

    private void saveStateNotification(boolean b) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.NOTIFICATION_SWITCH,b);
        editor.apply();

        if (b){
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.NEWS_NOTIFICATION);
        }else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NEWS_NOTIFICATION);
        }

    }

    private void getSwitchCompatFromMemory() {
        boolean isNotification = pref.getBoolean(Constants.NOTIFICATION_SWITCH, false);
        scNotification.setChecked(isNotification);
    }


}
