package com.example.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tamn.app2uproject.Fragments.AboutFragment;
import com.example.tamn.app2uproject.Fragments.CommentFragment;
import com.example.tamn.app2uproject.Fragments.EventsFragment;
import com.example.tamn.app2uproject.Fragments.UploadEventsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    Toolbar toolbar;

    // Dialog to send data to firebase
    AlertDialog dialogSendData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new EventsFragment())
                .commit();

        checkIsUserLogin();
        addNavigationDrawer();

    }

    private void addNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * check if user is login else will send him to login activity
     * for signIn/signUp
     */
    private void checkIsUserLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        /*
        // can write the two line above in one line:
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        */

        if (user == null){
            //Go to Login
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }else{
            //we have a user//user.getEmail()
            //Initialize this screen
            initLayout();
            initEvents();
        }
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCustomDialog();
            }
        });
    }

  /*  private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_event_upload,null,false);
        final EditText etDialogTitle = (EditText) dialogView.findViewById(R.id.etDialogTitle);
        final EditText etItemContent = (EditText) dialogView.findViewById(R.id.etItemContent);
        Button btnItemSend = (Button) dialogView.findViewById(R.id.btnItemSend);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        btnItemSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etDialogTitle.getText().toString();
                String content = etItemContent.getText().toString();

                uploadDataToFirebase(title,content);
            }
        });

        //Dismiss Dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSendData.dismiss();
            }
        });

        builder.setView(dialogView);
        dialogSendData = builder.show();

    }
    //send data to Database
    private void uploadDataToFirebase(String title, String content) {
        MessageItem item = new MessageItem(title,content);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("messages");

        reference.push().setValue(item).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Your Event has been Uploaded !!!", Toast.LENGTH_SHORT).show();
                dialogSendData.dismiss();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void initLayout() {
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_off) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            return true;
        }else if (id == R.id.action_upload){
            //Move to UploadActivity
            startActivity(new Intent(MainActivity.this,UploadActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upload_event) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new UploadEventsFragment())
                    .commit();
        } else if (id == R.id.nav_events) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new EventsFragment())
                    .commit();

        } else if (id == R.id.nav_slideshow) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new CommentFragment())
                    .commit();

        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new AboutFragment())
                    .commit();

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
