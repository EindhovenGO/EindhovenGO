package com.example.endgo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
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

        // hide most of the point of interest indicators
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

        // Check if we have permission to use user location
        // otherwise ask for it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                focusUserLocation();
            }
        } else {
            // TODO: fix the case where user denies app permissions again
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            focusUserLocation();
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

    public void focusUserLocation() throws SecurityException{
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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

            // just show a circle right now, to show it works
            addCircle(mMap, new LatLng(lat, lng), 3000);
        }
    }


}
