package com.example.endgo;

import android.app.Application;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInCheck extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fUser != null) {
            startActivity(new Intent(LoggedInCheck.this, MainActivity.class));
        }
    }
}
