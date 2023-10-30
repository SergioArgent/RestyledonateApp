package com.example.restyledonate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {
    Button btnregistrar;
    RelativeLayout relativeLayout;
    TextView nombres, apellidos, telefono, correo, contraseña;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    private boolean imageSelected = false;

    // Constante para solicitar permiso para acceder a la galería
    private static final int REQUEST_GALLERY_PERMISSION = 1001;
    // Constante para seleccionar una imagen de la galería
    private static final int REQUEST_PICK_IMAGE = 1002;

    // Uri para almacenar la URI de la imagen seleccionada
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        relativeLayout = findViewById(R.id.relative);

        btnregistrar = findViewById(R.id.btnRegistrar);
        nombres = findViewById(R.id.edtNombres);
        apellidos = findViewById(R.id.edtApellidos);
        telefono = findViewById(R.id.edtTelefono);
        correo = findViewById(R.id.edtCorreo);
        contraseña = findViewById(R.id.edtContraseña);

        // Agregar un OnClickListener al ImageView para seleccionar una imagen
        ImageView imagenPerfil = findViewById(R.id.SubirImagen);
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprobar y solicitar permiso para acceder a la galería si no está otorgado
                if (ContextCompat.checkSelfPermission(Registrarse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Registrarse.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_GALLERY_PERMISSION);
                } else {
                    // Si el permiso está otorgado, abre la galería
                    openGallery();
                }
            }
        });

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombres.getText().toString();
                String apellido = apellidos.getText().toString();
                String tel = telefono.getText().toString();
                String cor = correo.getText().toString();
                String con = contraseña.getText().toString();

                if (!nombre.isEmpty() && !apellido.isEmpty() && !tel.isEmpty() && !cor.isEmpty() && !con.isEmpty()){
                    if(con.length() > 6){
                        if(imageSelected==true){
                            if(tel.length() == 9){
                                registerUser(nombre, apellido, tel, cor, con);
                            }else{
                                Toast.makeText(Registrarse.this, "Debe ingresar un número de celular válido", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Registrarse.this, "Debe seleccionar una foto de perfil", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Registrarse.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Registrarse.this, "Debe Completar T odos los Campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Método para abrir la galería y seleccionar una imagen
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // Manejar la respuesta de la selección de imagen y permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, abrir la galería
                openGallery();
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede acceder a la galería.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Manejar el resultado de la selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageSelected = true;
            ImageView imagenPerfil = findViewById(R.id.SubirImagen);
            imagenPerfil.setImageURI(selectedImageUri);
        }
    }

    // Método para registrar al usuario y guardar la imagen en Firebase
    private void registerUser(String nombre, String apellido, String tel, String cor, String con) {
        mAuth.createUserWithEmailAndPassword(cor, con)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Obtén el ID del usuario registrado
                            String userId = mAuth.getCurrentUser().getUid();

                            // Crea un mapa para almacenar los datos del usuario
                            Map<String, Object> userMap = new HashMap<>();
                            Bundle extras = getIntent().getExtras();
                            Double la1 = extras.getDouble("LAT1");
                            Double lo1 = extras.getDouble("LON1");
                            userMap.put("name", nombre);
                            userMap.put("lastname", apellido);
                            userMap.put("telefon", tel);
                            userMap.put("email", cor);
                            userMap.put("password", con);
                            userMap.put("latitud",la1);
                            userMap.put("longitud",lo1);

                            // Guarda los datos del usuario en la base de datos
                            mDatabase.child("Users").child(userId).setValue(userMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if (task2.isSuccessful()) {
                                                // Si los datos se guardan correctamente, sube la imagen
                                                uploadImage(userId);
                                            } else {
                                                Toast.makeText(Registrarse.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Manejar el caso en que el correo electrónico ya existe
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Registrarse.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Registrarse.this, "No se pudo registrar este Usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // Método para subir la imagen a Firebase Storage
    private void uploadImage(String userId) {
        if (selectedImageUri != null) {
            // Obtiene una referencia al almacenamiento de Firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Crea una referencia para la imagen del usuario
            StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");

            // Sube la imagen a Firebase Storage
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtiene la URL de descarga de la imagen
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    // Actualiza la URL de la imagen en la base de datos
                                    mDatabase.child("Users").child(userId).child("profileImage").setValue(imageUrl);

                                    // Redirige al usuario a la pantalla de inicio de sesión
                                    startActivity(new Intent(Registrarse.this, Login.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Manejar errores al obtener la URL de descarga
                                    Toast.makeText(Registrarse.this, "Error al obtener la URL de descarga de la imagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Manejar errores en la carga de la imagen
                            Toast.makeText(Registrarse.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
