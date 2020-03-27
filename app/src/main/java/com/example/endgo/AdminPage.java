package com.example.endgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminPage extends AppCompatActivity {


    TextView location1;
    TextView description1;
    Button updateLocation1;
    DatabaseReference fDB;
    StorageReference storageRef;
    String locationName;
    String descriptionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        location1 = findViewById(R.id.location1);
        description1 = findViewById(R.id.description1);
        fDB = FirebaseDatabase.getInstance().getReference("Objectives");
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        fDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationName = dataSnapshot.child("test2").child("name").getValue().toString().trim();
                location1.setText(locationName);

                descriptionName = dataSnapshot.child("test2").child("description").getValue().toString().trim();
                description1.setText(descriptionName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPage.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        updateLocation1 = (Button) findViewById(R.id.updateLocation1);
        updateLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD =  description1.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("test2").child("description").setValue(newD);
            }
        });

    }
}
