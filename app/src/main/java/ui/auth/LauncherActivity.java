package ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Log.d(TAG, "No hay usuario, redirigiendo a LoginGoogleActivity.");
            go(LoginGoogleActivity.class);
            return;
        }

        currentUser.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String providerId = "password";
                for (UserInfo profile : currentUser.getProviderData()) {
                    if (profile.getProviderId().equals("google.com")) {
                        providerId = "google.com";
                        break;
                    }
                }

                if (providerId.equals("google.com")) {
                    checkUserProfile(currentUser, true);
                } else {
                    checkUserProfile(currentUser, currentUser.isEmailVerified());
                }
            } else {
                Log.e(TAG, "Fallo al recargar el usuario.", task.getException());
                auth.signOut();
                go(LoginGoogleActivity.class);
            }
        });
    }

    private void checkUserProfile(FirebaseUser user, boolean isEmailVerified) {
        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    // Verifica si el documento del usuario existe en Firestore
                    if (!doc.exists()) {
                        Log.d(TAG, "Perfil no encontrado. Redirigiendo a FirstTimeLoginActivity.");
                        go(FirstTimeLoginActivity.class);
                    } else if (!isEmailVerified) {
                        Log.d(TAG, "Email no verificado. Redirigiendo a VerifyEmailActivity.");
                        go(VerifyEmailActivity.class);
                    } else {
                        // El documento existe y el email está verificado (si aplica).
                        // Ahora, comprueba si el cuestionario fue completado.
                        Boolean questionnaireCompleted = doc.getBoolean("questionnaireCompleted");
                        if (questionnaireCompleted != null && questionnaireCompleted) {
                            // Si el cuestionario está completado, va a la pantalla principal.
                            Log.d(TAG, "Usuario verificado y con cuestionario completado. Redirigiendo a MainActivity.");
                            go(MainActivity.class);
                        } else {
                            // Si el campo es nulo o false, debe completar el cuestionario.
                            Log.d(TAG, "Cuestionario no completado. Redirigiendo a FirstTimeLoginActivity.");
                            go(FirstTimeLoginActivity.class);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener perfil de Firestore.", e);
                    auth.signOut();
                    go(LoginGoogleActivity.class);
                });
    }

    private void go(Class<?> target) {
        Intent intent = new Intent(this, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
