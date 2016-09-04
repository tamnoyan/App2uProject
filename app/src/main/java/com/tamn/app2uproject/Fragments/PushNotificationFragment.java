package com.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.tamn.app2uproject.Adapter.SendNotificationAsyncAdapter;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.IOHelper;
import com.tamn.app2uproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PushNotificationFragment extends Fragment {


    EditText etPushTitle;
    EditText etPushContent;
    TextView tvNotificationSent;
    Button btnPushSend;

    TextInputLayout login_layout;
    public PushNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View pushView = inflater.inflate(R.layout.fragment_push_notification, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.push_notification));
        etPushTitle = (EditText) pushView.findViewById(R.id.etPushTitle);
        etPushContent = (EditText) pushView.findViewById(R.id.etPushContent);
        btnPushSend = (Button) pushView.findViewById(R.id.btnPushSend);
        tvNotificationSent = (TextView) pushView.findViewById(R.id.tvNotificationSent);
        login_layout = (TextInputLayout) pushView.findViewById(R.id.login_layout);

        initEvents();
        return pushView;
    }

    private void initEvents() {
        btnPushSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPushTitle.getText().toString().equals("") && etPushContent.getText().toString().equals("")){
                    login_layout.setErrorEnabled(true);
                    login_layout.setError(getString(R.string.required_field));
                    IOHelper.getAnimation(etPushTitle, Techniques.Shake);
                    IOHelper.getAnimation(etPushContent, Techniques.Shake);

                }else {
                    startAsyncTaskNotification(etPushTitle.getText().toString(), etPushContent.getText().toString());
                    Toast.makeText(getActivity(), getString(R.string.sent_successfully), Toast.LENGTH_LONG).show();
                    etPushTitle.getText().clear();
                    etPushContent.getText().clear();
                    //tvNotificationSent.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void startAsyncTaskNotification(String title,String content) {
        new SendNotificationAsyncAdapter(title,content).execute(Constants.GOOGLE_NOTIFICATION_END_POINT);
    }

}
