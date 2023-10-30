package com.example.restyledonate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registrarTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.contraseña);
        loginButton = findViewById(R.id.btn_Ingresar);
        registrarTextView = findViewById(R.id.Registrar);

        // Agrega un OnClickListener al botón de ingreso
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Agrega un OnClickListener al texto "REGISTRARSE"
        registrarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirige al usuario a la actividad de registro (Registrarse.java)
                Intent intent = new Intent(Login.this, Ubicacion.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Verifica que los campos de correo electrónico y contraseña no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El inicio de sesión fue exitoso, redirige al usuario a la actividad principal (PrincipalActivity.java)
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(Login.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad actual
                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje de error
                        Toast.makeText(this, "Credenciales Inválidas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
