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

public class LoginEmailActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private ImageView btnTogglePassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView txtGoToRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility(inputPassword, btnTogglePassword));

        btnLogin.setOnClickListener(v -> {
            btnLogin.setEnabled(false);
            loginUser();
        });

        txtGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, FirstTimeLoginActivity.class));
            finish();
        });
    }

    private void loginUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError("El correo es obligatorio");
            inputEmail.requestFocus();
            btnLogin.setEnabled(true);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Correo no válido");
            inputEmail.requestFocus();
            btnLogin.setEnabled(true);
            return;
        }

        if (password.isEmpty()) {
            inputPassword.setError("La contraseña es obligatoria");
            inputPassword.requestFocus();
            btnLogin.setEnabled(true);
            return;
        }

        setLoadingState(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    setLoadingState(false);
                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                Log.i("EmailLogin", "✅ Inicio de sesión correcto para " + email);
                                Toast.makeText(this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                // 🚨 NO cerramos sesión aquí
                                Log.w("EmailLogin", "⚠️ Correo no verificado para " + email);
                                Toast.makeText(this, "Verifica tu correo antes de continuar", Toast.LENGTH_LONG).show();

                                // Enviamos al usuario directamente a la pantalla de verificación
                                Intent intent = new Intent(this, VerifyEmailActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        Log.e("EmailLogin", "❌ Error al iniciar sesión: ", task.getException());
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
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
