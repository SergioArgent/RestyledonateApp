package com.example.restyledonate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.restyledonate.databinding.ActivitySolicitarBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Solicitar extends FragmentActivity implements OnMapReadyCallback {

    TextView txtnombres, txtpropietario, txtapellido, txtprecio, txttelefono, txttipo;
    Button btnSolicitar;
    ImageView ImageArticulo;
    Double latitud, longitud;

    private  static  final int REQUEST_PERMISSION_CAMERA=101;
    private  static  final int REQUEST_IMAGE_CAMERA=101;

    private GoogleMap mMap;
    private ActivitySolicitarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySolicitarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
        txtnombres = findViewById(R.id.txtNombre);
        txtapellido = findViewById(R.id.txtapellidoprop);
        txtpropietario = findViewById(R.id.txtnombreprop);
        txttelefono = findViewById(R.id.txttelefono);
        txttipo = findViewById(R.id.txttipo);
        txtprecio = findViewById(R.id.txtprecio);
        ImageArticulo = findViewById(R.id.imgArticulo);

        btnSolicitar = findViewById(R.id.btnSolicitar);

        Picasso.get().load(getIntent().getStringExtra("imagenarticulo"))
                .placeholder(R.drawable.ic_dashboard_black_24dp).into(ImageArticulo);


        Bundle extrasp = getIntent().getExtras();

        latitud = extrasp.getDouble("latitud");
        longitud = extrasp.getDouble("longitud");

        txtnombres.setText(getIntent().getStringExtra("txtnombrearticulo"));
        txtpropietario.setText(getIntent().getStringExtra("txtnombrecompleto"));
        txtapellido.setText(getIntent().getStringExtra("txtapellidos"));
        txttelefono.setText(getIntent().getStringExtra("txttelefono"));
        txttipo.setText(getIntent().getStringExtra("txttipo"));
        txtprecio.setText(getIntent().getStringExtra("txtprecio"));

        mMap = googleMap;




        // Add a marker in Sydney and move the camera
        LatLng Lugar = new LatLng(latitud, longitud);
        mMap.addMarker(
                new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubi))
                        .position(Lugar)
                        .title("Destino de Pedido"));

        CameraPosition cameraPosition = CameraPosition.builder().target(Lugar).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Toast.makeText(this, "Coordenadas Pedido "+latitud+"<>"+longitud, Toast.LENGTH_SHORT).show();


        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Solicitar.this, Login.class);
                startActivity(i);
            }
        });
    }
}