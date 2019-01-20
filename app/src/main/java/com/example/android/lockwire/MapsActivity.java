package com.example.android.lockwire;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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

    public void updateBikePosition(Location loc){
        bikePosition.lat = loc.lat;
        bikePosition.longC = loc.longC;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Database Ref Setup
        cordRef=FirebaseDatabase.getInstance().getReference("location");
        cordRef.addValueEventListener(valueEventListener);

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {

                updateBikePosition(userSnapshot.getValue(Location.class));
                System.out.print(bikePosition.lat);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
   /** ValueEventListener valueEvventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                Log.i("SINGLE VALUE EVENT", userSnapshot.child("long").getValue(String.class));
                double longCord = (double) userSnapshot.getValue();
                longCoordinates.add(longCord);



            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; **/





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
        float defaultZoom = 20;
        LatLng sydney = new LatLng(36.995171,-122.064213);
        mMap.setMaxZoomPreference(defaultZoom);
        mMap.getMaxZoomLevel();
        mMap.addMarker(new MarkerOptions().position(sydney).title("The Wedge"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
