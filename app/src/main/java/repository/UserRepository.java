package repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private String getUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    // Guarda el perfil del usuario (viene de FirestoreHelper.saveUserProfile)
    public void saveUserProfile(String nivel, String objetivo, boolean dolor, boolean yaClasificado) {
        String userId = getUserId();
        if (userId == null) {
            Log.e(TAG, "Usuario no autenticado");
            return;
        }

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("nivel", nivel);
        perfil.put("objetivo", objetivo);
        perfil.put("tieneDolor", dolor);
        perfil.put("yaClasificado", yaClasificado);

        db.collection("users").document(userId)
                .set(perfil, SetOptions.merge())
                .addOnSuccessListener(a -> Log.d(TAG, "Perfil guardado"))
                .addOnFailureListener(e -> Log.e(TAG, "Error guardando perfil", e));
    }

    // Carga el perfil como LiveData (reactivo, se actualiza automáticamente)
    public LiveData<User> getUserProfile() {
        MutableLiveData<User> liveData = new MutableLiveData<>();
        String userId = getUserId();
        if (userId == null) {
            liveData.setValue(null);
            return liveData;
        }

        db.collection("users").document(userId)
                .addSnapshotListener((doc, e) -> {
                    if (e != null || doc == null || !doc.exists()) {
                        liveData.postValue(null);
                        return;
                    }
                    User user = new User();
                    user.setUid(userId);
                    user.setNombre(doc.getString("nombre"));
                    user.setEmail(doc.getString("email"));
                    user.setNivel(doc.getString("nivel"));
                    user.setObjetivo(doc.getString("objetivo"));
                    Boolean dolor = doc.getBoolean("tieneDolor");
                    user.setTieneDolor(dolor != null && dolor);
                    Boolean clasificado = doc.getBoolean("yaClasificado");
                    user.setYaClasificado(clasificado != null && clasificado);
                    liveData.postValue(user);
                });

        return liveData;
    }

    // Verifica si el usuario ya completó el cuestionario
    public LiveData<Boolean> isUserClassified() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        String userId = getUserId();
        if (userId == null) {
            liveData.setValue(false);
            return liveData;
        }

        db.collection("users").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Boolean clasificado = doc.getBoolean("yaClasificado");
                        liveData.setValue(clasificado != null && clasificado);
                    } else {
                        liveData.setValue(false);
                    }
                })
                .addOnFailureListener(e -> liveData.setValue(false));

        return liveData;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }
}