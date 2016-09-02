package com.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tamn.app2uproject.Fragments.SigninFragment;
import com.tamn.app2uproject.Fragments.SignupFragment;
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
    Button btnLogin;
    TextView tvSignUp;
    //Google Button
    SignInButton btnGoogleSignIn;
    //Facebook Button
    LoginButton btnFacebookSignin;
    CallbackManager callbackManager;
    RelativeLayout changeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Signin));


        initLatout();
        initEvents();

    }

    private void initEvents() {

        //Sign Up TextView
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // createUserEmailAndPass();
                changeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SignupFragment())
                        .commit();
            }
        });

        //Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SigninFragment())
                .commit();
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


    private void moveToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

   /*
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
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        //Google
        btnGoogleSignIn = (SignInButton) findViewById(R.id.btnGoogleSignIn);
        //getString(com.google.android.gms.R.string.common_signin_button_text_long);
        //Facebook
        btnFacebookSignin = (LoginButton) findViewById(R.id.btnFacebookSignin);
        callbackManager = CallbackManager.Factory.create();

        changeLayout = (RelativeLayout) findViewById(R.id.changeLayout);

    }
}
