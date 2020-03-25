package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
                Snackbar.make(view, "Game settings I guess", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switchActivity(new GSettingsActivity() );
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
            // Handle the camera action
        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_achievements) {
            Intent intent = new Intent(getApplicationContext(), AcheivementsActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Used to switch to another activity {@code a}
     * @param a the activity to go to
     */
    private void switchActivity(Activity a) {
        Intent i = new Intent(MainMenu.this, a.getClass() );
        startActivity(i);
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
        List<Integer> currentLocations = new ArrayList<>(); //TODO fetch locations from DB

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
}
