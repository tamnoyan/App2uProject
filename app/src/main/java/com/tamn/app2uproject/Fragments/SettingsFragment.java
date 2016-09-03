package com.tamn.app2uproject.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


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

        scNotification = (SwitchCompat) view.findViewById(R.id.scNotification);

        pref = getActivity().getSharedPreferences(Constants.APPLICATION_PREF, Context.MODE_PRIVATE);

        getSwitchCompatFromMemory();
        switchAction();


        return view;
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
        editor.commit();

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
