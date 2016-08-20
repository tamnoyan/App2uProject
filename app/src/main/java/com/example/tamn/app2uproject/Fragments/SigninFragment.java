package com.example.tamn.app2uproject.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamn.app2uproject.MainActivity;
import com.example.tamn.app2uproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {

    EditText etSigninEmail, etSigninPassword;
    TextView tvResetPassword , tvResetmessage;
    String email = null;
    String pass = null;
    Button btnSignin;


    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateView = inflater.inflate(R.layout.fragment_signin, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.Signin));

        etSigninEmail = (EditText) inflateView.findViewById(R.id.etSigninEmail);
        etSigninPassword = (EditText) inflateView.findViewById(R.id.etSigninPassword);
        tvResetPassword = (TextView) inflateView.findViewById(R.id.tvResetPassword);
        tvResetmessage = (TextView) inflateView.findViewById(R.id.tvResetmessage);
        btnSignin = (Button) inflateView.findViewById(R.id.btnSignin);

        initEvents();
        return inflateView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initEvents() {

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninWithEmailAndPass();
            }
        });

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }




    private void resetPassword() {

        if (email != null){
            FirebaseAuth.getInstance().sendPasswordResetEmail(etSigninEmail.getText().toString())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(tvResetPassword,
                                    e.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).
                                    setAction("dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    });
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.send_restore_mail), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
           tvResetmessage.setVisibility(View.VISIBLE);
//            btnSignin.setText(getResources().getString(R.string.restore));
        }
    }



    private void SigninWithEmailAndPass() {
        email = etSigninEmail.getText().toString();
        pass = etSigninPassword.getText().toString();
       // tvResetmessage.setVisibility(View.VISIBLE);

        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // succeed in login a user
                                moveToMainActivity();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.Error) + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Email_and_Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

}
