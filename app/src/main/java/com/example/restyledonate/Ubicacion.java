package com.example.restyledonate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.restyledonate.databinding.ActivityUbicacionBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;


import org.jetbrains.annotations.NotNull;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUbicacionBinding binding;
    LatLng LugarInicial = new LatLng(-12.048012644540275, -75.19988337525803);
    LatLng LugarDestino;
    double mLat=-12.048012644540275, mLong=-75.19988337525803;
    LatLng Lugar;
    double lat1=0.0, long1 =0.0;
    double distance;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUbicacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase db = FirebaseDatabase.getInstance();// utilizapara conctarse
        mDatabase = db.getReference();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //return;
        }
        Lugar = new LatLng(mLat, mLong);

        mMap.addMarker(new MarkerOptions().position(Lugar).title("ORIENTAL FOOD"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Lugar));
        CameraPosition cameraPosition = CameraPosition.builder().target(Lugar).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull LatLng latLng) {
                mLat = latLng.latitude;
                mLong = latLng.longitude;
                lat1=mLat;
                long1=mLong;
                LugarDestino = new LatLng(lat1, long1);
                String nompe = getIntent().getStringExtra("nompe");
                String precpe = getIntent().getStringExtra("precpe");
                String tiempe = getIntent().getStringExtra("tiempe");
                distance = SphericalUtil.computeDistanceBetween(LugarInicial, LugarDestino);
                if(distance<4000){
                    Intent i = new Intent(Ubicacion.this, Registrarse.class);
                    i.putExtra("LAT1",lat1);
                    i.putExtra("LON1",long1);
                    i.putExtra("dis",distance);
                    i.putExtra("nomped", nompe);
                    i.putExtra("precped", precpe);
                    i.putExtra("tiemped", tiempe);

                    startActivity(i);
                }
            }
        });
    }
}