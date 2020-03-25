package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HintFragment.OnListFragmentInteractionListener  {

    String username;
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //ScrollView scrollView = findViewById(R.id.objective_list);
        Fragment newFragment = new LocationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace the view with the hintfragment
        transaction.add(R.id.objective_contents, newFragment);
        transaction.commit();
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
            //to profile
            Intent i = new Intent(MainMenu.this, UserProfile.class);
            startActivity(i);
        } else if (id == R.id.nav_friends) {
        } else if (id == R.id.nav_logout) {
            //TODO prevent back button operation
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
        username = fUser.getDisplayName();
        points = getPoints();

        //Fetch current locations
        /*
        List<Integer> currentLocations = new ArrayList<>(); //TODO fetch locations from DB
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Objectives");
        db.orderByChild("name");

        //Fetch completed locations
        List<Integer> completedLocations = new ArrayList<>(); //TODO fetch locations from DB

        //Filter locations
        for ( int i = 0; i < completedLocations.size(); i++ ) {
            for ( int j = 0; j < currentLocations.size(); j++ ) {
                if ( completedLocations.get(i).equals( currentLocations.get(j) ) ) {
                    currentLocations.remove(j);
                    break; //break to save performance, assuming all locations are unique
                }
            }
        }
         */
    }

    /**
     * Fetch user points from DB
     */
    private int getPoints() {
        if (true) {
            return 1000;
        }
        //TODO Fetch user points from DB
        return -1;
    }

    @Override
    public void onListFragmentInteraction(HintList.HintItem item) {
        (new GSettingsActivity() ).onListFragmentInteraction(item);
    }
}
