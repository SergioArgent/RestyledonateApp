package com.example.restyledonate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restyledonate.databinding.ActivityPrincipalBinding;
import com.example.restyledonate.ui.catalogo.CatalogoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PrincipalActivity extends AppCompatActivity {

    private FrameLayout bottomNavContainer;
    private FrameLayout navDrawerContainer;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar los contenedores
        bottomNavContainer = findViewById(R.id.bottomNavContainer);
        navDrawerContainer = findViewById(R.id.navDrawerContainer);

        // Configurar la actividad
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, DonacionActivity.class));
            }
        });

        // Configurar el sistema de navegación y el cajón de navegación
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navViewDrawer;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_donaciones, R.id.nav_atencion, R.id.nav_educacion)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configurar la navegación inferior
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_catalogo, R.id.navigation_mensajes)
                .build();
        NavController bottomNavController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, bottomNavController);

        mostrarInformacionDelUsuario();
    }

    private void mostrarInformacionDelUsuario() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Obtener la información del usuario desde Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nombreCompleto = dataSnapshot.child("name").getValue(String.class);
                        String correo = dataSnapshot.child("email").getValue(String.class);
                        String imageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                        // Obtener las referencias de las vistas en nav_header_main.xml
                        ImageView fotoPerfilImageView = findViewById(R.id.imageView);
                        TextView nombreCompletoTextView = findViewById(R.id.NombreCompleto);
                        TextView correoTextView = findViewById(R.id.Correo);

                        // Establecer la información del usuario en las vistas
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            // Cargar la foto de perfil si está disponible
                            Picasso.get().load(imageUrl).into(fotoPerfilImageView);
                        }
                        nombreCompletoTextView.setText(nombreCompleto);
                        correoTextView.setText(correo);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                    Toast.makeText(PrincipalActivity.this, "Error al obtener la información del usuario", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // El usuario no ha iniciado sesión, puedes manejarlo de acuerdo a tu flujo de la aplicación.
            // Por ejemplo, redirigirlo a la pantalla de inicio de sesión.
            // Esto es solo un ejemplo, asegúrate de implementar tu lógica adecuadamente.
            Toast.makeText(this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar el código para redirigir al usuario a la pantalla de inicio de sesión si lo deseas.
        }
    }

    // Agregar un método para cambiar la visibilidad de los contenedores
    private void setContainersVisibility(boolean bottomNavVisible, boolean navDrawerVisible) {
        bottomNavContainer.setVisibility(bottomNavVisible ? View.VISIBLE : View.GONE);
        navDrawerContainer.setVisibility(navDrawerVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
