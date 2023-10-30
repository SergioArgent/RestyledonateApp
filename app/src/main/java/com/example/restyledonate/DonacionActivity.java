package com.example.restyledonate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DonacionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerArticulo;
    private Button btn_registrar;
    private EditText edtNombreArticulo;
    private EditText edtDescripcion;
    private EditText edtCantidad;
    private EditText edtHorario;
    private EditText edtPrecio; // Nuevo campo de precio
    private ImageView imageView;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private LinearLayout layoutAdicional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);
        layoutAdicional= findViewById(R.id.layoutCategoriaAdicional);

        spinnerArticulo = findViewById(R.id.spinner3);
        edtNombreArticulo = findViewById(R.id.edtNombreArticulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtCantidad = findViewById(R.id.edtCantidad);
        edtHorario = findViewById(R.id.edtHorario);
        edtPrecio = findViewById(R.id.edtPrecio); // Inicializar el campo de precio
        imageView = findViewById(R.id.SubirImagen);
        btn_registrar= findViewById(R.id.btnRegistrar);

        databaseReference = FirebaseDatabase.getInstance().getReference("donaciones");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_articulo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticulo.setAdapter(adapter);
        spinnerArticulo.setOnItemSelectedListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarDonacion();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        if (selectedItem.equals("Re-venta")) {
            // Si se selecciona "Re-venta", muestra el campo de precio
            layoutAdicional.setVisibility(View.VISIBLE);
        } else {
            // En caso contrario, oculta el campo de precio
            layoutAdicional.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No se necesita una acción específica si no se selecciona nada
    }

    public void registrarDonacion() {
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
                        String apellido = dataSnapshot.child("lastname").getValue(String.class);
                        String telefono = dataSnapshot.child("telefon").getValue(String.class);
                        String correo = dataSnapshot.child("email").getValue(String.class);
                        String image = dataSnapshot.child("profileImage").getValue(String.class);
                        String latitu = dataSnapshot.child("latitud").getValue().toString().trim();
                        String longitu = dataSnapshot.child("longitud").getValue().toString().trim();
                        String nombreArticulo = edtNombreArticulo.getText().toString().trim();
                        String descripcion = edtDescripcion.getText().toString().trim();
                        String cantidad = edtCantidad.getText().toString().trim();
                        String horario = edtHorario.getText().toString().trim();
                        String tipoArticulo = spinnerArticulo.getSelectedItem().toString();
                        String precio = edtPrecio.getText().toString().trim(); // Nuevo campo de precio

                        if (nombreArticulo.isEmpty() || descripcion.isEmpty() || cantidad.isEmpty() || horario.isEmpty() || imageUri == null) {
                            Toast.makeText(DonacionActivity.this, "Por favor, complete todos los campos y seleccione una imagen", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Crear un mapa para almacenar los datos de la donación
                        Map<String, Object> donacion = new HashMap<>();
                        donacion.put("nombreArticulo", nombreArticulo);
                        donacion.put("descripcion", descripcion);
                        donacion.put("cantidad", cantidad);
                        donacion.put("horario", horario);
                        donacion.put("tipoArticulo", tipoArticulo);
                        donacion.put("nombreCompleto", nombreCompleto);
                        donacion.put("apellido", apellido);
                        donacion.put("telefono", telefono);
                        donacion.put("correo", correo);
                        donacion.put("image", image);
                        donacion.put("latitud", latitu);
                        donacion.put("longitud", longitu);
                        donacion.put("precio", precio); // Agregar precio al mapa

                        // Si se seleccionó una imagen, cargarla en Firebase Storage y guardar la URL
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("imagenes_donaciones");
                        final StorageReference imageReference = storageReference.child(UUID.randomUUID().toString() + ".jpg");

                        imageReference.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                donacion.put("imagenUrl", imageUrl);
                                                guardarDonacionEnFirebase(donacion);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DonacionActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                    Toast.makeText(DonacionActivity.this, "Error al publicar donación", Toast.LENGTH_SHORT).show();
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

    private void guardarDonacionEnFirebase(Map<String, Object> donacion) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = databaseReference.child(userId);
        userReference.push().setValue(donacion);

        Toast.makeText(this, "Donación registrada con éxito", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DonacionActivity.this, PrincipalActivity.class));
        finish();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
