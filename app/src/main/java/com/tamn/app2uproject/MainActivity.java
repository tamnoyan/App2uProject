package com.tamn.app2uproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamn.app2uproject.Fragments.AboutFragment;
import com.tamn.app2uproject.Fragments.AddAdminFragment;
import com.tamn.app2uproject.Fragments.ContactUsFragment;
import com.tamn.app2uproject.Fragments.EventsFragment;
import com.tamn.app2uproject.Fragments.PushNotificationFragment;
import com.tamn.app2uproject.Fragments.SettingsFragment;
import com.tamn.app2uproject.Fragments.UploadEventsFragment;
import com.tamn.app2uproject.Model.UserDetails;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    FirebaseUser currentUser;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //navigation
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //header
    ImageView ivUser, ivSetting;
    TextView profileName;
    TextView profileEmail;

    //subMenu
    String uploadEvent;
    String addAdmin;
    String pushNotication;

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

        addNavigationDrawer();
        initLayout();
        initEvents();
        gettingHeaderDetails();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
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
        ivSetting= (ImageView) headerView.findViewById(R.id.ivSetting);
    }

    private void clearHeader(){
        profileEmail.setText("נקודת צמיחה");
        ivUser.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        profileName.setText("nekoda@gmail.com");
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
                final DatabaseReference ref = database.getReference(Constants.USERS).child(currentUser.getUid());

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
                            Toast.makeText(MainActivity.this, getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        ref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
            getAdminsGroup();
        }
    }


    /**
     * check if user is login else will send him to login activity
     * for signIn/signUp
     */
    private void checkIsUserLogin() {
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            //Go to Login
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            gettingHeaderDetails();
        }else{
            //we have a user
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
                    if (adminsHashMap != null){
                    if(adminsHashMap.size() >0 ) {
                        for (String admin : adminsHashMap.keySet()) {
                            String value = adminsHashMap.get(admin);
                            //Key               //Value
                            if (email.equals(value)) {
                                //Change the Menu
                                addSubMenu();
                            }
                        }
                    }
                }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * Add Sub Menu
     */
    private void addSubMenu(){
        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu(getResources().getString(R.string.admin));
        pushNotication = getResources().getString(R.string.push_notification);
        subMenu.setIcon(getResources().getDrawable(R.drawable.ic_add_a_photo));
        subMenu.add(pushNotication);
        uploadEvent = getResources().getString(R.string.upload_event);
        subMenu.add(uploadEvent);
        addAdmin = getResources().getString(R.string.add_admin);
        subMenu.add(addAdmin);

        //On Draw
        navigationView.invalidate();
    }

    private void initEvents() {
        currentUser = auth.getCurrentUser();
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SettingsFragment())
                        .commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }


    private void initLayout() {
    }

    @Override
    public void onBackPressed() {
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

        } else if (id == R.id.nav_give_take) {
            startActivity(new Intent(MainActivity.this,GiveAndTakeActivity.class));

        }  else if (id == R.id.nav_questions) {
                startActivity(new Intent(MainActivity.this,GeneralQuestionsActivity.class));

        }  else if (id == R.id.nav_contactUs) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new ContactUsFragment())
                        .commit();
        }
        //admin
        else if (item.getTitle().equals(uploadEvent)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new UploadEventsFragment())
                    .commit();

        } else if (item.getTitle().equals(pushNotication)){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new PushNotificationFragment())
                    .commit();

        } else if (item.getTitle().equals(addAdmin)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new AddAdminFragment())
                    .commit();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
