package com.example.endgo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String currentObjectiveName;
    private ObjectiveData objective;
    private Circle objectiveCircle;
    private Location objectiveLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseUser fUser =  fAuth.getCurrentUser();
    LocationCallback callback;
    int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Objective name taken from Main menu
        if (null != getIntent()) {
            Intent incomingIntent = getIntent();
            currentObjectiveName =  incomingIntent.getStringExtra("Name");
        }
        // on creation we ask for permission, no use in creating the map if
        // we have no permission
        handlePermissions();
    }

    public void handlePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // if permission we can move on
            initMap();
        } else {
            // if not we ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }
    }

    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        // ask for the location every 2 seconds
        LocationRequest request = LocationRequest.create();
        request.setInterval(2000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // the locationclient we will use to get the location of the user
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // if no objectiveLocation just do nothing
                    if (objectiveLocation == null) {
                        return;
                    }
                    // if we have a location we can compute how close the user is to the objective
                    // if the user is closer than the treshold they win
                    double distance = location.distanceTo(objectiveLocation);
                    if (distance <= objectiveCircle.getRadius()) {
                        // we create a popup saying that you won
                        createWinPopup();
                        // query the database for the current user's points
                        Query query = db.child("Users").child(fUser.getUid());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //put the queried data into a LocationData object
                                int userPoints = dataSnapshot.child("points").getValue(int.class);
                                userPoints += objective.difficulty * 100;
                                dataSnapshot.getRef().child("points").setValue(userPoints);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        // disable location updates, otherwise we would keep adding points to the user
                        fusedLocationClient.removeLocationUpdates(callback);
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(request,
                callback,
                Looper.getMainLooper());

        // after all is initialized we can load the objective from the server
        mapFragment.getMapAsync(this);
        loadObjective();

    }

    public void focusUserLocation() throws SecurityException {
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // when we have found the location we zoom in on it
                if (task.isComplete()) {
                    Location location = task.getResult();
                    // if location off do nothing, the map will just tsay zoomed out
                    if (location == null) {
                        return;
                    }
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // hide most of the point of interest indicators
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

        // enable user location on the map and focus on it
        mMap.setMyLocationEnabled(true);
        focusUserLocation();
    }

    // Will add a transparent green circle to the map at given coordinates
    // with specified radius
    public void addCircle(GoogleMap map, LatLng center, float radius) {
        CircleOptions cOptions = new CircleOptions();
        cOptions.center(center);
        cOptions.radius(radius);
        cOptions.fillColor(Color.argb(0.5f, 0f, 1f, 0f));
        cOptions.strokeWidth(0);
        objectiveCircle = map.addCircle(cOptions);
    }


    public void loadObjective() {
        Query query = db.child("Objectives").child(currentObjectiveName);
        // we add a valueeventlistener so we udate the circle on the map on a database change
        // this means the user can see live changes of objective coordinates
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (objectiveCircle != null) {
                    objectiveCircle.remove();
                }

                //put the queried data into a LocationData object
                objective = dataSnapshot.getValue(ObjectiveData.class);
                objectiveLocation = new Location("");
                objectiveLocation.setLatitude(objective.latitude);
                objectiveLocation.setLongitude(objective.longitude);

                int radius;
                if (objective.difficulty == 0) { radius = 1000; } else {
                    radius = 500 / objective.difficulty;
                }
                // we can create a circle using the given data
                addCircle(mMap, new LatLng(objective.latitude, objective.longitude), radius);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void createWinPopup() {
        // we create a popupWindow that displays the hintContent view
        DimPopup popupWindow = new DimPopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        ConstraintLayout layout = findViewById(R.id.mapslayout);
        View hintContent = popupWindow.getContentView();

        TextView text = hintContent.findViewById(R.id.text_hint);

        //display the popup window
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.dimBackground();

        // set the popupWindow text to the hint details
        text.setText("You have found the location!\n" + objective.difficulty * 100 + " points have been added to your account");
        Button button = new Button(this);
        button.setText("Back");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout popLayout = (LinearLayout)popupWindow.getContentView().findViewById(R.id.poplayout);
        popLayout.addView(button);
    }

    public void createAlertPopup() {
        // we create a popupWindow that displays the hintContent view
        final DimPopup popupWindow = new DimPopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        ConstraintLayout layout = findViewById(R.id.mapslayout);
        View hintContent = popupWindow.getContentView();

        TextView text = hintContent.findViewById(R.id.text_hint);

        //display the popup window
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.dimBackground();

        // set the popupWindow text to the hint details
        text.setText("Please give us permission to use your location!");
        Button backButton = new Button(this);
        backButton.setText("Back");
        // if back button pressed go back to main menu
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button permissionButton = new Button(this);
        permissionButton.setText("set permission");
        // if permission button pressed ask for location permission
        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePermissions();
                popupWindow.dismiss();
            }
        });

        LinearLayout popLayout = (LinearLayout)popupWindow.getContentView().findViewById(R.id.poplayout);
        popLayout.addView(backButton);
        popLayout.addView(permissionButton);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // if the requestcode is right we have permission
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                // otherwise create alert to ask user again for permission
                createAlertPopup();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
