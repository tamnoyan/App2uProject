package com.example.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_CODE = 15;
    private static final int FACEBOOK_SIGN_IN = 64206;
    // signIn/signUp with email/password
    EditText etUser, etPassword;
    Button btnLogin;
    TextView tvSignUp;
    //Google Button
    SignInButton btnGoogleSignIn;
    //Facebook Button
    LoginButton btnFacebookSignin;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLatout();
        initEvents();

    }

    private void initEvents() {

        //Sign Up Button
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserEmailAndPass();
            }
        });

        //Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithEmailAndPass();
            }
        });

        //Google SignIn button
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        //Facebook SignIn
        btnFacebookSignin.setReadPermissions("email","public_profile");
        //To respond to a login result, need to register a callback with either LoginManager or LoginButton
        btnFacebookSignin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //If login succeeds, the LoginResult parameter has the new AccessToken, and the most recently granted or declined permissions.
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //Facebook
    //AccessToken Use this class Graph API requests. It shows the user id, and the accepted and denied permissions
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        // Initialize Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //TODO: save to database USER TABLE
                            moveToMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInWithGoogle() {
        //Configure Google Sign in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
                                            // server's client ID
        ).requestIdToken(getString(R.string.web_application))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        if (!connectionResult.isSuccess()){
                            Toast.makeText(LoginActivity.this, "Error:" + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        // sending the request code to google and waiting for a response.
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,GOOGLE_SIGN_CODE);
    }

    /**
     * Login with email and password
     */
    private void LoginWithEmailAndPass() {
        String email = etUser.getText().toString();
        String pass = etPassword.getText().toString();

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
                    Toast.makeText(LoginActivity.this, "Error:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    //Sign Up with email and password
    private void createUserEmailAndPass() {
        String email = etUser.getText().toString();
        String pass = etPassword.getText().toString();

            try {

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
                            Toast.makeText(LoginActivity.this, "Sign up Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            }

    }

    private void moveToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

   /* private void resetPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(getEmail())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(btnResetPassword,
                                e.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).
                                setAction("dismiss", new OnClickListener() {
                                    @Override
                                    public void onClick(View view) { }
                                });
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this,
                        "Sent - Check your email...", Toast.LENGTH_SHORT).show();
            } });
    }*/

    /**
     * All Results Should appear here...
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*Google Sign In
            Result returned from launching the Intent from
            GoogleSignInApi.getSignInIntent(...);*/
        if (requestCode == GOOGLE_SIGN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                // Google Sign In was successful, authenticate with Firebase
                // need to send answer to Firebase.
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseResultWithGoogle(account);
            }

        }else if (requestCode == FACEBOOK_SIGN_IN){
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }


    }

    // sending info from google account to Firebase:
    private void firebaseResultWithGoogle(GoogleSignInAccount account) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            moveToMainActivity();
                        }else {
                            Toast.makeText(LoginActivity.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void initLatout() {
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        //Google
        btnGoogleSignIn = (SignInButton) findViewById(R.id.btnGoogleSignIn);
        //Facebook
        btnFacebookSignin = (LoginButton) findViewById(R.id.btnFacebookSignin);
        callbackManager = CallbackManager.Factory.create();

    }
}
