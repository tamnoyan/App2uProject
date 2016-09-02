package com.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tamn.app2uproject.Adapter.SendNotificationAsyncAdapter;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PushNotificationFragment extends Fragment {


    EditText etPushTitle;
    EditText etPushContent;
    Button btnPushSend;

    public PushNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View pushView = inflater.inflate(R.layout.fragment_push_notification, container, false);

        etPushTitle = (EditText) pushView.findViewById(R.id.etPushTitle);
        etPushContent = (EditText) pushView.findViewById(R.id.etPushContent);
        btnPushSend = (Button) pushView.findViewById(R.id.btnPushSend);

        btnPushSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startAsyncTaskNotification(etPushTitle.getText().toString(),etPushContent.getText().toString());

            }
        });


        return pushView;
    }

    private void startAsyncTaskNotification(String title,String content) {
        new SendNotificationAsyncAdapter(title,content).execute(Constants.GOOGLE_NOTIFICATION_END_POINT);
    }

}
