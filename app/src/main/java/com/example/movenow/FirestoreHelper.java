package com.example.movenow;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions; // Importar SetOptions

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {

    private static final String TAG = "FirestoreHelper";
    private final FirebaseFirestore db;
    private final String userId;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        // Cambié "usuarios" por "users" para que coincida con el resto de tu código
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

    private CollectionReference getUserActivitiesCollection() {
        if (userId == null) {
            Log.e(TAG, "Usuario no autenticado");
            return null;
        }
        // Cambié "usuarios" por "users"
        return db.collection("users").document(userId).collection("actividades");
    }

    // ... (El resto de tus métodos de actividades y progreso se quedan igual)
    public void saveActivity(String titulo, String descripcion, int frecuenciaMinutos, boolean completada) {
        CollectionReference actividadesRef = getUserActivitiesCollection();
        if (actividadesRef == null) return;

        Map<String, Object> actividad = new HashMap<>();
        actividad.put("titulo", titulo);
        actividad.put("descripcion", descripcion);
        actividad.put("timestamp", System.currentTimeMillis());
        actividad.put("completada", completada);
        actividad.put("frecuenciaMinutos", frecuenciaMinutos);

        actividadesRef.add(actividad)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "Actividad guardada con ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error guardando actividad", e));
    }

    public void markActivityCompleted(String actividadId) {
        CollectionReference actividadesRef = getUserActivitiesCollection();
        if (actividadesRef == null) return;

        DocumentReference docRef = actividadesRef.document(actividadId);
        docRef.update("completada", true)
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "Actividad marcada como completada"))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error al actualizar actividad", e));
    }

    public CollectionReference getActivitiesHistory() {
        return getUserActivitiesCollection();
    }


    // ------------------------------
    // ✅ PERFIL DEL USUARIO (MODIFICADO)
    // ------------------------------
    public void saveUserProfile(String nivel, String objetivo, boolean dolor, boolean yaClasificado) {
        if (userId == null) {
            Log.e(TAG, "Usuario no autenticado al guardar perfil");
            return;
        }

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("nivel", nivel);
        perfil.put("objetivo", objetivo);
        perfil.put("tieneDolor", dolor);
        perfil.put("yaClasificado", yaClasificado); // <-- Aquí se establece el campo clave

        // AHORA GUARDA EN LA RUTA CORRECTA: "users/{userId}"
        // Usamos SetOptions.merge() para no borrar otros campos que puedan existir en el documento.
        db.collection("users")
                .document(userId)
                .set(perfil, SetOptions.merge()) // Usa merge para añadir/actualizar sin borrar lo demás
                .addOnSuccessListener(a ->
                        Log.d(TAG, "Perfil guardado correctamente en users/" + userId))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error guardando perfil", e));
    }

    public void getUserProfile(OnUserProfileLoaded callback) {
        if (userId == null) {
            Log.e(TAG, "Usuario no autenticado al cargar perfil");
            callback.onLoaded(null);
            return;
        }

        // AHORA LEE DESDE LA RUTA CORRECTA: "users/{userId}"
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        callback.onLoaded(doc.getData());
                    } else {
                        callback.onLoaded(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error cargando perfil", e);
                    callback.onLoaded(null);
                });
    }

    public interface OnUserProfileLoaded {
        void onLoaded(Map<String, Object> perfil);
    }

    // ------------------------------
    // ✅ PROGRESO DE LA RUTINA (Sin cambios, pero revisa la ruta si es necesario)
    // ------------------------------
    public void saveCompletedSession() {
        if (userId == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("lastCompleted", System.currentTimeMillis());

        // Ojo: Esto también guarda en una subcolección "progreso" dentro de "users"
        db.collection("users")
                .document(userId)
                .collection("progreso")
                .document("rutina")
                .set(data);
    }

    public void getLastCompletedSession(OnUserSessionLoaded callback) {
        if (userId == null) {
            callback.onLoaded(0);
            return;
        }

        db.collection("users")
                .document(userId)
                .collection("progreso")
                .document("rutina")
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.contains("lastCompleted")) {
                        Long last = doc.getLong("lastCompleted");
                        callback.onLoaded(last != null ? last : 0);
                    } else {
                        callback.onLoaded(0);
                    }
                })
                .addOnFailureListener(e -> callback.onLoaded(0));
    }

    public interface OnUserSessionLoaded {
        void onLoaded(long timestamp);
    }
}
