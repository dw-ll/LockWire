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
import com.google.firebase.database.ValueEventListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleMapOptions options = new GoogleMapOptions();
    FirebaseApp app;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DataSnapshot snapshot;
    ValueEventListener listener;
    double latCord=0, longCord=0;
    private final String TAG = "MapsActivity";

    DatabaseReference cordRef = database.getReference().child("location");









    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ValueEventListener latListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                DataSnapshot latx = dataSnapshot.child("lat");
                latCord=latx.getValue(double.class);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d(TAG,"wrong.");

            }
        };
        ValueEventListener longListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                DataSnapshot longx = dataSnapshot.child("long");
                longCord=longx.getValue(double.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        cordRef.addValueEventListener(latListener);
        cordRef.addValueEventListener(longListener);
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
        float defaultZoom = 20;
       // getCords(cordRef,latCord,longCord);
        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(latCord,longCord);
        mMap.setMaxZoomPreference(defaultZoom);
        mMap.getMaxZoomLevel();
        mMap.addMarker(new MarkerOptions().position(sydney).title("The Wedge"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
