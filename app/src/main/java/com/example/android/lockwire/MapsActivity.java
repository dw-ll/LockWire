package com.example.android.lockwire;

import android.app.AlertDialog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // google maps api setup
    private GoogleMap mMap;
    GoogleMapOptions options = new GoogleMapOptions();

    // firebase api setup
    FirebaseApp app;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // console print
    private final String TAG = "MapsActivity";

    private ValueEventListener mCordListener;
    private ValueEventListener mWarnListener;

    public static class Location {
        double lat = 0;
        double longC = 0;

        public Location(){

        }
        public double getLat(){
            return this.lat;


        }
        public double getLongC(){
            return this.longC;

        }

        public void setLat(double lat){
            this.lat = lat;

        }
        public void setLongC(double longC){
            this.longC = longC;
        }
    }

    Location bikePosition = new Location();

    DatabaseReference cordRef;
    DatabaseReference warnRef;

    public void updateBikePosition(Location loc){
        bikePosition.lat = loc.lat;
        bikePosition.longC = loc.longC;
        onMapReady(mMap);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Database Ref Setup
        cordRef=FirebaseDatabase.getInstance().getReference("location");
        warnRef=FirebaseDatabase.getInstance().getReference("movement");
        //cordRef.addValueEventListener(valueEventListener);

        /**

        //Query
        Query latQuery = FirebaseDatabase.getInstance().getReference("location")
                        .equalTo(0)
                        .orderByChild("lat");
        //double latCoordinate = FirebaseDatabase.getInstance().getReference("location");

        //latQuery.addListenerForSingleValueEvent(valueEventListener);

        /**Query longQuery = FirebaseDatabase.getInstance().getReference("location")
                .equalTo(0)
                .orderByChild("long");
        latQuery.addListenerForSingleValueEvent(valueEvventListener);

        **/




    }


    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener cordListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    updateBikePosition(userSnapshot.getValue(Location.class));

                    System.out.print(bikePosition.lat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        };

        ValueEventListener warnListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    int movement = userSnapshot.child("amt").getValue(Integer.class);
                    String w;
                    if(movement > 5) {
                        w = "Warning!!";
                        System.out.println(w);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        cordRef.addValueEventListener(cordListener);
        warnRef.addValueEventListener(warnListener);

        mCordListener = cordListener;
        mWarnListener = warnListener;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float defaultZoom = 20;
        LatLng marker = new LatLng(bikePosition.getLat(),bikePosition.getLongC());
        mMap.setMaxZoomPreference(defaultZoom);
        mMap.getMaxZoomLevel();
        mMap.addMarker(new MarkerOptions().position(marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
    }
}