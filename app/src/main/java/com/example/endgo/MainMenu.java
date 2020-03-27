package com.example.endgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.text.Layout;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HintFragment.OnListFragmentInteractionListener  {

    String username;
    String email;
    int points;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDatabase;
    DatabaseReference fDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.menu_toSettings, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainMenu.this, GSettingsActivity.class);
                        startActivity(i);
                    }
                }, 500);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton helpButton = findViewById(R.id.floatingHelpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.menu_toHelp, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(i);
                    }
                }, 500);
            }
        });

        // Update drawer Username and points
        getUserInfo();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Update textview
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.append("\nCurrently logged in as: " + username);
        welcomeText.append("\nEmail: " + email );

        //Firebase get objectives
        fDB = FirebaseDatabase.getInstance().getReference("Objectives");
        fDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ObjectiveList.clear();
                for ( DataSnapshot obj : dataSnapshot.getChildren() ) {
                    ObjectiveList.write( obj.getValue(ObjectiveData.class) );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ObjectiveList.clear();
            }
        });

        // TODO: loading drawable as placeholder
        //Wait 1 second before updating the Objective list
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment newFragment = new LocationFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the view with the hintfragment
                transaction.add(R.id.objective_contents, newFragment);
                transaction.commit();
            }
        }, 3000); //Delay before locations load

    }

    @Override
    protected void onStart() {
        super.onStart();

        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        TextView user = header.findViewById(R.id.drawer_username);
        if (user == null) {} else {
            user.setText(username);
        }

        ImageView userPfp = header.findViewById(R.id.user_pfp);
        if (userPfp == null) {} else {
            userPfp.setImageURI(fUser.getPhotoUrl());
        }


    }

    @Override
    public void onBackPressed() {

        /**Drawer menu*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Drawer menu functions
     */
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // To Profile
            Intent i = new Intent(MainMenu.this, UserProfile.class);
            i.putExtra("points", points);
            i.putExtra("username", username);
            i.putExtra("email", email);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            // Logout
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainMenu.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else if (id == R.id.nav_achievements) {
            Intent intent = new Intent(getApplicationContext(), AchievementsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Obtains user info. (Name, points and completed objective id's)
     * Obtains current objectives.
     * Checks what objectives are not completed.
     * Filters out completed objectives.
     */
    private void getUserInfo() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        Intent i = getIntent();
        if (i == null) {
            username = fUser.getDisplayName();
            email = fUser.getEmail();
        } else {
            username = i.getStringExtra("username");
            email = i.getStringExtra("email");
        }

        getPoints();
    }

    /**
     * Fetch user points from DB
     */
    private void getPoints() {
        points = -1;
        FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int p;
                if (dataSnapshot.child("points").getValue(Integer.class) == null) {
                    p = 0;
                } else {
                    p = dataSnapshot.child("points").getValue(Integer.class);

                    // Update drawer info
                    View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
                    TextView userPoints = header.findViewById(R.id.drawer_points);
                    if (userPoints == null) {} else {
                        String pointz = userPoints.getText() +"" + p;
                        userPoints.setText(pointz);
                    }
                }
                points = p;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(HintList.HintItem item) {
        // On clicking on a list fragment, the location should be sent WITH the intent
        String objectiveName = item.content;

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("Name", objectiveName);
        startActivity(intent);
    }
}
