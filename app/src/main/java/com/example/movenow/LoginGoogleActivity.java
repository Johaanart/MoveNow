package com.example.movenow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginGoogleActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // ✅ Si ya hay un usuario logueado, decidir adónde enviarlo
        if (currentUser != null) {
            String provider = "password"; // valor por defecto
            if (currentUser.getProviderData().size() > 1) {
                provider = currentUser.getProviderData().get(1).getProviderId();
            }

            if (provider.equals("google.com")) {
                // Cuenta Google → correo ya está verificado
                goToMain();
                return;
            }

            // Usuario con email y contraseña
            currentUser.reload().addOnCompleteListener(task -> {
                if (currentUser.isEmailVerified()) {
                    goToMain();
                } else {
                    // 🚀 Si aún no verifica su correo, lo mandamos a VerifyEmailActivity
                    Intent intent = new Intent(this, VerifyEmailActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return;
        }

        // Configuración de Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // -------------------------------
        // VISTAS Y EVENTOS
        // -------------------------------
        Button btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);
        Button btnRegister = findViewById(R.id.startButton);
        TextView txtGoToLoginEmail = findViewById(R.id.txtGoToLoginEmail);

        // Iniciar sesión con Google
        btnGoogleSignIn.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // Ir a registro manual
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, FirstTimeLoginActivity.class)));

        // Ir a login manual
        txtGoToLoginEmail.setOnClickListener(v -> startActivity(new Intent(this, LoginEmailActivity.class)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Error al iniciar sesión con Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (acct == null) {
            Toast.makeText(this, "No se pudo obtener la cuenta de Google", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    goToMain();
                }
            } else {
                Toast.makeText(this, "Falló la autenticación con Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(LoginGoogleActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
