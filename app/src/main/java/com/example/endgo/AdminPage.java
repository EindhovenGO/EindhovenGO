package com.example.endgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminPage extends AppCompatActivity {

    TextView location1;
    TextView description1;
    Button updateLocation1;
    String locationName1;
    String descriptionName1;
    TextView location2;
    TextView description2;
    Button updateLocation2;
    String locationName2;
    String descriptionName2;
    TextView location3;
    TextView description3;
    Button updateLocation3;
    String locationName3;
    String descriptionName3;
    TextView location4;
    TextView description4;
    Button updateLocation4;
    String locationName4;
    String descriptionName4;
    TextView location5;
    TextView description5;
    Button updateLocation5;
    String locationName5;
    String descriptionName5;
    DatabaseReference fDB;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        location1 = findViewById(R.id.location1);
        description1 = findViewById(R.id.description1);
        updateLocation1 = findViewById(R.id.updateLocation1);

        location2 = findViewById(R.id.location2);
        description2 = findViewById(R.id.description2);
        updateLocation2 = findViewById(R.id.updateLocation2);

        location3 = findViewById(R.id.location3);
        description3 = findViewById(R.id.description3);
        updateLocation3 = findViewById(R.id.updateLocation3);

        location4 = findViewById(R.id.location4);
        description4 = findViewById(R.id.description4);
        updateLocation4 = findViewById(R.id.updateLocation4);

        location5 = findViewById(R.id.location5);
        description5 = findViewById(R.id.description5);
        updateLocation5 = findViewById(R.id.updateLocation5);

        fDB = FirebaseDatabase.getInstance().getReference("Objectives");
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        fDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationName1 = dataSnapshot.child("Aurora").child("name").getValue().toString().trim();
                location1.setText(locationName1);
                descriptionName1 = dataSnapshot.child("Aurora").child("description").getValue().toString().trim();
                description1.setText(descriptionName1);

                locationName2 = dataSnapshot.child("Flux").child("name").getValue().toString().trim();
                location2.setText(locationName2);
                descriptionName2 = dataSnapshot.child("Flux").child("description").getValue().toString().trim();
                description2.setText(descriptionName2);

                locationName3 = dataSnapshot.child("Luna").child("name").getValue().toString().trim();
                location3.setText(locationName3);
                descriptionName3 = dataSnapshot.child("Luna").child("description").getValue().toString().trim();
                description3.setText(descriptionName3);

                locationName4 = dataSnapshot.child("Vertigo Parking").child("name").getValue().toString().trim();
                location4.setText(locationName4);
                descriptionName4 = dataSnapshot.child("Vertigo Parking").child("description").getValue().toString().trim();
                description4.setText(descriptionName4);

                locationName5 = dataSnapshot.child("Weird glass blob").child("name").getValue().toString().trim();
                location5.setText(locationName5);
                descriptionName5 = dataSnapshot.child("Weird glass blob").child("description").getValue().toString().trim();
                description5.setText(descriptionName5);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPage.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        updateLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD1 =  description1.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("Aurora").child("description").setValue(newD1);
            }
        });

        updateLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD2 =  description2.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("Flux").child("description").setValue(newD2);
            }
        });

        updateLocation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD3 =  description3.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("Luna").child("description").setValue(newD3);
            }
        });

        updateLocation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD4 =  description4.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("Vertigo Parking").child("description").setValue(newD4);
            }
        });

        updateLocation5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newD5 =  description5.getText().toString();

                FirebaseDatabase.getInstance().getReference("Objectives")
                        .child("Weird glass blob").child("description").setValue(newD5);
            }
        });

    }
}
