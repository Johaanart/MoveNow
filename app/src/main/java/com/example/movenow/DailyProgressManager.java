package com.example.movenow;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DailyProgressManager {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String getUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return null;
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public void addProgress(int amount) {

        String userId = getUserId();
        if (userId == null) {
            Log.e("DailyProgress", "userId es null, no se puede guardar progreso");
            return;
        }

        String today = dateFormat.format(new Date());

        db.collection("dailyGoals").document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    // --- INICIO DEL CÓDIGO CORREGIDO ---

                    int currentProgress = 0;
                    if (doc != null && doc.exists()) {
                        String savedDate = doc.getString("date");
                        // Si la fecha guardada es de hoy, usamos el progreso actual
                        if (today.equals(savedDate) && doc.contains("progress")) {
                            currentProgress = doc.getLong("progress").intValue();
                        }
                    }
                    // Si no, currentProgress se queda en 0 (porque es un nuevo día)

                    // Calculamos el nuevo progreso, asegurando que no pase de 100
                    int newProgress = Math.min(currentProgress + amount, 100);

                    Map<String, Object> update = new HashMap<>();
                    update.put("date", today);
                    update.put("progress", newProgress);

                    // Guardamos los datos en Firestore usando merge para no borrar otros campos
                    db.collection("dailyGoals").document(userId)
                            .set(update, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> Log.d("DailyProgress", "Progreso actualizado a: " + newProgress))
                            .addOnFailureListener(e -> Log.e("DailyProgress", "Error al guardar progreso", e));

                    // --- FIN DEL CÓDIGO CORREGIDO ---
                })
                .addOnFailureListener(e -> {
                    Log.e("DailyProgress", "Error al obtener el documento de progreso", e);
                });
    }
}
