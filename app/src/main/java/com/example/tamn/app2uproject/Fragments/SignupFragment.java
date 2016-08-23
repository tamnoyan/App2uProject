package com.example.tamn.app2uproject.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.example.tamn.app2uproject.IOHelper;
import com.example.tamn.app2uproject.MainActivity;
import com.example.tamn.app2uproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    EditText etSignupEmail, etSignupPassword, etUser;
    Button btnSignup;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.create_account));
        //show/hide toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        etSignupEmail = (EditText) inflate.findViewById(R.id.etSignupEmail);
        etSignupPassword = (EditText) inflate.findViewById(R.id.etSignupPassword);
        etUser = (EditText) inflate.findViewById(R.id.etUser);
        btnSignup = (Button) inflate.findViewById(R.id.btnSignup);
        initEvents();

        return inflate;
    }

    private void initEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserEmailAndPass();
            }
        });
    }

    //Sign Up with email and password
    private void createUserEmailAndPass() {
        String email = etSignupEmail.getText().toString();
        String pass = etSignupPassword.getText().toString();
        String username = etUser.getText().toString();

            try {
                //todo: add support for username and image
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        // add a listener in case of success
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // succeed in creating a user
                                    moveToMainActivity();
                                }
                            }
                            // add a listener in case of failure
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                etSignupEmail.setError(getResources().getString(R.string.required_field));
               IOHelper.getAnimation(etSignupEmail, Techniques.Shake);

                etSignupPassword.setError(getResources().getString(R.string.required_field));
                IOHelper.getAnimation(etSignupPassword ,Techniques.Shake );

                etUser.setError(getResources().getString(R.string.required_field));
                IOHelper.getAnimation(etUser,Techniques.Shake);

                Toast.makeText(getActivity(), getResources().getString(R.string.Email_Password_username_cannot_be_empty), Toast.LENGTH_SHORT).show();
            }

    }

    private void moveToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

}
