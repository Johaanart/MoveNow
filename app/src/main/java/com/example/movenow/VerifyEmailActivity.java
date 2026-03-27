package com.example.movenow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button btnCheckVerification;
    private TextView tvResendEmail, tvUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btnCheckVerification = findViewById(R.id.btnCheckVerification);
        tvResendEmail = findViewById(R.id.tvResendEmail);
        tvUserEmail = findViewById(R.id.tvUserEmail);

        if (currentUser == null) {
            // ⚠️ Si algo raro pasa, volvemos al login
            Toast.makeText(this, "Por favor inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginEmailActivity.class));
            finish();
            return;
        }

        tvUserEmail.setText(currentUser.getEmail());

        // 🚀 Verificación automática
        currentUser.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful() && currentUser.isEmailVerified()) {
                Toast.makeText(this, "✅ Correo verificado correctamente.", Toast.LENGTH_SHORT).show();
                goToMain();
            }
        });

        btnCheckVerification.setOnClickListener(v -> checkVerification());
        tvResendEmail.setOnClickListener(v -> resendVerificationEmail());
    }

    private void checkVerification() {
        currentUser.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (currentUser.isEmailVerified()) {
                    Toast.makeText(this, "✅ ¡Correo verificado correctamente!", Toast.LENGTH_SHORT).show();
                    goToMain();
                } else {
                    Toast.makeText(this, "⚠️ Aún no has verificado tu correo.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error al comprobar la verificación.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendVerificationEmail() {
        currentUser.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "📨 Correo de verificación reenviado. Revisa tu bandeja.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Error al reenviar correo: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
