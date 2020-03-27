package com.example.endgo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_profile);
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
                        Intent i = new Intent(UserProfile.this, GSettingsActivity.class);
                        startActivity(i);
                    }
                }, 500);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Update drawer Username and points
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent i = getIntent();
        points = i.getIntExtra("points", -1);
        TextView user = findViewById(R.id.username);
        assert fUser != null;
        String userText = user.getText() + i.getStringExtra("username");
        user.setText( userText );

        TextView userPoints = findViewById(R.id.points);
        //userPoints.append(""+points);
        String p = userPoints.getText() +""+ points;
        userPoints.setText( p );

        ImageView userPfp = findViewById(R.id.profile_picture);
        userPfp.setImageURI(fUser.getPhotoUrl());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(UserProfile.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else if (id == R.id.nav_achievements) {
            Intent intent = new Intent(getApplicationContext(), AchievementsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
