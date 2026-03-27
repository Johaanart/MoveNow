package com.example.movenow;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirstTimeLoginActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    private Button btnRegister;
    private TextView txtGoToLogin;
    private ImageView btnTogglePassword, btnToggleConfirm;
    private ProgressBar progressBar;

    // 1. Declarar la flecha de retroceso
    private ImageView btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- CAMBIO REALIZADO AQUÍ ---
        // La llamada a super.onCreate() DEBE ser lo primero.
        super.onCreate(savedInstanceState);

        // Ahora sí, puedes hacer verificaciones y continuar con la inicialización.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getProviderData().size() > 1) {
            // Ya tiene sesión de Google → no debe ver esta pantalla.
            // Es mejor redirigir a MainActivity.
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return; // Termina la ejecución de onCreate para que no continúe.
        }

        // El resto del código permanece igual.
        setContentView(R.layout.activity_first_time_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirm = findViewById(R.id.btnToggleConfirm);
        progressBar = findViewById(R.id.progressBar);

        // Flecha atrás
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        btnRegister.setOnClickListener(v -> {
            btnRegister.setEnabled(false);
            validateAndRegisterUser();
        });

        btnTogglePassword.setOnClickListener(v ->
                togglePasswordVisibility(inputPassword, btnTogglePassword));

        btnToggleConfirm.setOnClickListener(v ->
                togglePasswordVisibility(inputConfirmPassword, btnToggleConfirm));

        txtGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginGoogleActivity.class));
            finish();
        });

        inputName.requestFocus();
    }


    private void validateAndRegisterUser() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            inputName.setError("El nombre es obligatorio");
            inputName.requestFocus();
            btnRegister.setEnabled(true);
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Introduce un correo válido");
            inputEmail.requestFocus();
            btnRegister.setEnabled(true);
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("La contraseña debe tener al menos 6 caracteres");
            inputPassword.requestFocus();
            btnRegister.setEnabled(true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            inputConfirmPassword.setError("Las contraseñas no coinciden");
            inputConfirmPassword.requestFocus();
            inputPassword.setText("");
            inputConfirmPassword.setText("");
            btnRegister.setEnabled(true);
            return;
        }

        registerUser(name, email, password);
    }

    private void registerUser(String name, String email, String password) {
        setLoadingState(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Register", "✅ Usuario creado correctamente.");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserData(firebaseUser, name, email);
                        }
                    } else {
                        Log.e("Register", "❌ Error creando usuario: ", task.getException());
                        Toast.makeText(this, "Error al crear usuario: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        setLoadingState(false);
                    }
                });
    }

    private void saveUserData(FirebaseUser firebaseUser, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        // He quitado los campos vacíos, es mejor añadirlos cuando el usuario los rellene
        // userData.put("age", "");
        // userData.put("height", "");
        // userData.put("weight", "");
        // userData.put("gender", "");

        String userId = firebaseUser.getUid();

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.i("Firestore", "👤 Datos guardados correctamente para: " + userId);
                    sendEmailVerification(firebaseUser);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "❌ Error guardando datos: ", e);
                    Toast.makeText(this, "Error al guardar tus datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    setLoadingState(false);
                });
    }

    private void sendEmailVerification(FirebaseUser firebaseUser) {
        Log.d("EmailVerification", "Enviando correo de verificación a: " + firebaseUser.getEmail());

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    setLoadingState(false);
                    if (task.isSuccessful()) {
                        Log.i("EmailVerification", "✉️ Correo de verificación enviado.");
                        Intent intent = new Intent(this, VerifyEmailActivity.class);
                        intent.putExtra("email", firebaseUser.getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("EmailVerification", "❌ Error al enviar correo: ", task.getException());
                        Toast.makeText(this, "Error al enviar correo: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.animate().alpha(1f).setDuration(200).start();
            btnRegister.setEnabled(false);
        } else {
            progressBar.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
            }).start();
        }
    }

    private void togglePasswordVisibility(EditText editText, ImageView icon) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(null);
            icon.setImageResource(R.drawable.ic_eye);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            icon.setImageResource(R.drawable.ic_eye_off);
        }
        editText.setSelection(editText.getText().length());
    }
}
