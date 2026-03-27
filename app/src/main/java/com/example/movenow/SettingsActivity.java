package com.example.movenow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity {

    private Button btnSoporte, btnProgreso, btnComunidad, btnCerrarSesion;
    private Button btnCambiarCategoria; // ✅ Nuevo botón
    private ImageButton btnBack;
    private SwitchCompat switchNotifications;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final String[] niveles = {"Principiante", "Intermedio", "Avanzado", "Flexibilidad"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // --- Referencias ---
        btnSoporte = findViewById(R.id.btnSoporte);
        btnProgreso = findViewById(R.id.btnProgreso);
        btnComunidad = findViewById(R.id.btnComunidad);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCambiarCategoria = findViewById(R.id.btnCambiarCategoria); // ✅ NUEVO
        btnBack = findViewById(R.id.btnBack);
        switchNotifications = findViewById(R.id.switchNotifications);

        // 🔄 Cargar estado del switch
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        // ✅ Listener de notificaciones
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();

            if (isChecked) {
                Toast.makeText(this, "Notificaciones activadas ✅", Toast.LENGTH_SHORT).show();
                NotificationScheduler.startNotifications(this); // ✅ Nuevo scheduler unificado
            } else {
                Toast.makeText(this, "Notificaciones desactivadas ❌", Toast.LENGTH_SHORT).show();
                WorkManager.getInstance(this).cancelUniqueWork("MoveNow_Notifications");
            }
        });

        // ✅ Botón cambiar categoría
        btnCambiarCategoria.setOnClickListener(v -> mostrarSelectorNivel());

        // 🔙 Volver
        btnBack.setOnClickListener(v -> finish());

        // 🧰 Botón soporte
        btnSoporte.setOnClickListener(v ->
                Toast.makeText(this, "Redirigiendo a soporte...", Toast.LENGTH_SHORT).show()
        );

        btnProgreso.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new ProgressFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnComunidad.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://www.facebook.com/profile.php?id=61584373739606"));
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Sesión cerrada 👋", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginGoogleActivity.class));
            finish();
        });
    }

    // ✅ Mostrar popup con selector de nivel
    private void mostrarSelectorNivel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar nivel");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, niveles);

        builder.setAdapter(adapter, (dialog, index) -> {
            String nivelSeleccionado = niveles[index];
            guardarNivelEnFirestore(nivelSeleccionado);
        });

        builder.show();
    }

    // ✅ Guardar en Firestore
    private void guardarNivelEnFirestore(String nivel) {
        String userId = mAuth.getUid();
        if (userId == null) return;

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("nivel", nivel);
        perfil.put("yaClasificado", true); // Para que StartFragment lo reconozca

        db.collection("usuarios").document(userId).set(perfil)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Nivel actualizado a " + nivel, Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar nivel", Toast.LENGTH_SHORT).show()
                );
    }
}
