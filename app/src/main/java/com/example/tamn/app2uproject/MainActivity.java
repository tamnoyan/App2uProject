package com.example.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamn.app2uproject.Fragments.AboutFragment;
import com.example.tamn.app2uproject.Fragments.AddAdminFragment;
import com.example.tamn.app2uproject.Fragments.EventsFragment;
import com.example.tamn.app2uproject.Fragments.UploadEventsFragment;
import com.example.tamn.app2uproject.Model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    Toolbar toolbar;

    FirebaseUser currentUser;
    FirebaseAuth auth = FirebaseAuth.getInstance();



    //navigation
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //header
    ImageView ivUser;
    TextView profileName;
    TextView profileEmail;

    //subMenu
    String uploadEvent;
    String addAdmin;

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

        //checkIsUserLogin();
        addNavigationDrawer();
        initLayout();
        initEvents();
        gettingHeaderDetails();

    }

    private void addNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigation drawer
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
         ivUser = (ImageView) headerView.findViewById(R.id.ivUser);
         profileName = (TextView) headerView.findViewById(R.id.profileName);
         profileEmail = (TextView) headerView.findViewById(R.id.profileEmail);


    }

    /****
     * Navigation header
     */
    private void gettingHeaderDetails() {
        if(currentUser != null) {
            profileEmail.setText(currentUser.getEmail());

            if (currentUser.getPhotoUrl() != null) {
                Picasso.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(ivUser);
                profileName.setText(currentUser.getDisplayName());

            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("users").child(currentUser.getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                        try {
                            if (userDetails.getUsername() != null) {
                                profileName.setText(userDetails.getUsername());
                            }
                            if (userDetails.getImageUrl() != null) {
                                Picasso.with(getApplicationContext()).load(userDetails.getImageUrl()).into(ivUser);
                            }
                        } catch (Exception e) {

                            Log.d("TammmmHeaderDetails", e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); //todo: remove all listener  ref.removeEventListener();
            }
            getAdminsGroup();
        }

    }


    /**
     * check if user is login else will send him to login activity
     * for signIn/signUp
     */
    private void checkIsUserLogin() {
        /*FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();*/
        /*
        // can write the two line above in one line:
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        */

        if (currentUser == null){
            //Go to Login
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            //gettingHeaderDetails();
        }else{
            //we have a user//user.getEmail()

            gettingHeaderDetails();
            getAdminsGroup();
        }
    }


    /***
     * if user is admin, add admin subMenu
     */
    public void getAdminsGroup(){
        //Get Current User Email
        if (currentUser !=null) {
            final String email = currentUser.getEmail();

            //Query the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference admins = database.getReference(Constants.ADMINDS);

            admins.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String,String> adminsHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                    for (String admin : adminsHashMap.keySet()) {
                        String value  = adminsHashMap.get(admin);
                        //Key               //Value
                        Log.d("DDebug",admin + ":" + value);
                        if (email.equals(value)){
                            //Change the Menu
                            addSubMenu();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void addSubMenu(){
        /**
         * Add Sub Menu
         */
        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu("Admins Dashboard");
        subMenu.add("push notification");
        uploadEvent = getResources().getString(R.string.upload_event);
        subMenu.add(uploadEvent);
        addAdmin = getResources().getString(R.string.add_admin);
        subMenu.add(addAdmin);

        //On Draw
        navigationView.invalidate();
    }

    private void initEvents() {
        currentUser = auth.getCurrentUser();
    }


    private void initLayout() {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_connect) {
            checkIsUserLogin();

        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new AboutFragment())
                    .commit();

        } else if (id == R.id.nav_events) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new EventsFragment())
                    .commit();

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();



        }  else if (item.getTitle().equals(uploadEvent)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new UploadEventsFragment())
                    .commit();

        } else if (item.getTitle().equals("push notification")){
            Toast.makeText(MainActivity.this, "WORKING NOTIFIACTION", Toast.LENGTH_SHORT).show();
            //getSupportFragmentManager().beginTransaction().replace()

        } else if (item.getTitle().equals(addAdmin)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new AddAdminFragment())
                    .commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
