package com.example.eindhovengo;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Provider;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Check if we have permission to use user location
        // otherwise ask for it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                // enable button to focus camera on user location
                mMap.setMyLocationEnabled(true);

                // we ask for the best provider using a default criteria
                String provider = locationManager.getBestProvider(new Criteria(), true);

                // when we can ge the location we focus on it, otherwise we don't do
                // anything
                if (provider != null) {
                    double lat = locationManager.getLastKnownLocation(provider).getLatitude();
                    double lng = locationManager.getLastKnownLocation(provider).getLongitude();
                    LatLng location = new LatLng(lat, lng);


                    // focus on user location when map screen opened and zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    // Will add a transparent green circle to the map at given coordinates
    // with specified radius
    public void addCircle(GoogleMap map, LatLng center, float radius) {
        CircleOptions cOptions = new CircleOptions();
        cOptions.center(center);
        cOptions.radius(radius);
        cOptions.fillColor(Color.argb(0.5f, 0f, 1f, 0f));
        cOptions.strokeWidth(0);
        map.addCircle(cOptions);
    }

}
