package com.example.endgo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class About extends AppCompatActivity {
    String username;
    int points;
    FirebaseUser fUser;
    //TODO The info/about activity


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acheivements);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        username = fUser.getDisplayName();
        points = getIntent().getIntExtra("points", -1);

        TextView user = findViewById(R.id.username);
        user.setText(username);

        TextView userPoints = findViewById(R.id.points);
        userPoints.append(" "+points);

        ImageView userPfp = findViewById(R.id.user_pfp);
        userPfp.setImageURI(fUser.getPhotoUrl());
    }
}
