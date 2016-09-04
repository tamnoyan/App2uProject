package com.tamn.app2uproject.Fragments;


import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.tamn.app2uproject.IOHelper;
import com.tamn.app2uproject.MainActivity;
import com.tamn.app2uproject.R;
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
    ProgressDialog progressDialog;
    View inflateView;


    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_signin, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.Signin));

        initLayout();
        initEvents();
        return inflateView;
    }

    private void initLayout() {
        etSigninEmail = (EditText) inflateView.findViewById(R.id.etSigninEmail);
        etSigninPassword = (EditText) inflateView.findViewById(R.id.etSigninPassword);
        tvResetPassword = (TextView) inflateView.findViewById(R.id.tvResetPassword);
        tvResetmessage = (TextView) inflateView.findViewById(R.id.tvResetmessage);
        btnSignin = (Button) inflateView.findViewById(R.id.btnSignin);
        progressDialog = new ProgressDialog(getActivity());
    }


    private void initEvents() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninWithEmailAndPass();
                //progressDialog.setMessage(getResources().getString(R.string.connecting));
                //progressDialog.show();
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
        try{
        if (email != null){
            FirebaseAuth.getInstance().sendPasswordResetEmail(etSigninEmail.getText().toString())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            etSigninEmail.setError(getResources().getString(R.string.invalid_email));
                            Toast.makeText(getActivity(),getResources().getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
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
            etSigninEmail.setError(getResources().getString(R.string.required_field));
            IOHelper.getAnimation(etSigninEmail, Techniques.Shake);
        }
    }catch (Exception e){
            progressDialog.dismiss();
            etSigninEmail.setError(getResources().getString(R.string.required_field));

            IOHelper.getAnimation(etSigninEmail, Techniques.Shake);

            tvResetmessage.setVisibility(View.VISIBLE);
            IOHelper.getAnimation(tvResetmessage,Techniques.ZoomIn);
        }
    }


    private void SigninWithEmailAndPass() {
        email = etSigninEmail.getText().toString();
        pass = etSigninPassword.getText().toString();

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

                            Toast.makeText(getActivity(), getResources().getString(R.string.error) + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();

                        }
                    });
        }catch (Exception e) {
            //progressDialog.dismiss();
            IOHelper.getAnimation(etSigninEmail, Techniques.Shake);
            IOHelper.getAnimation(etSigninPassword, Techniques.Shake);

            Toast.makeText(getActivity(), getResources().getString(R.string.Email_and_Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

}
