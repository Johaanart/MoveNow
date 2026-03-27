package com.example.movenow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    // Vistas
    private TextView tvGreeting;
    private MaterialButton btnStart, btnProgreso;
    private LinearProgressIndicator progressDaily;
    private TextInputLayout menuGoals;
    private AutoCompleteTextView autoCompleteTextViewGoals;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;

    private int progress = 0; // 0–100 diario

    private final String[] predefinedGoals = {
            "Completar mi rutina",
            "Hidratarse 8 vasos de agua",
            "Meditar 10 minutos",
            "Hacer 15 minutos de estiramientos",
            "Leer 10 páginas de un libro"
    };

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Notificaciones
        NotificationScheduler.startNotifications(requireContext());

        // Referencias UI
        tvGreeting = view.findViewById(R.id.tvGreeting);
        btnStart = view.findViewById(R.id.btnStart);
        btnProgreso = view.findViewById(R.id.btnProgreso);
        progressDaily = view.findViewById(R.id.progressDaily);
        menuGoals = view.findViewById(R.id.menuGoals);
        autoCompleteTextViewGoals = view.findViewById(R.id.autoCompleteTextViewGoals);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();

            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                tvGreeting.setText("¡Hola, " + user.getDisplayName() + "!");
            } else {
                db.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener(doc -> {
                            if (doc != null && doc.contains("name")) {
                                tvGreeting.setText("¡Hola, " + doc.getString("name") + "!");
                            } else {
                                tvGreeting.setText("¡Hola, bienvenido a MoveNow!");
                            }
                        });
            }
        }

        initGoalMenu();
        loadUserDataAndUpdateUI();

        // Botón ver progreso
        btnProgreso.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Tu progreso actual es " + progress + "%",
                        Toast.LENGTH_SHORT).show()
        );

        // Navegar a rutinas
        btnStart.setOnClickListener(v -> {
            if (userId == null) return;

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        NavController navController =
                                NavHostFragment.findNavController(HomeFragment.this);

                        if (doc.exists() && doc.contains("yaClasificado")
                                && doc.getBoolean("yaClasificado")) {

                            navController.navigate(R.id.action_homeFragment_to_startFragment);

                        } else {
                            navController.navigate(R.id.action_homeFragment_to_questionnaireFragment);
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(),
                                    "Error verificando tu perfil",
                                    Toast.LENGTH_SHORT).show());
        });
    }

    private void initGoalMenu() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                predefinedGoals
        );
        autoCompleteTextViewGoals.setAdapter(adapter);

        autoCompleteTextViewGoals.setOnItemClickListener((parent, view, position, id) -> {
            saveUserGoal(predefinedGoals[position]);
        });
    }

    private void saveUserGoal(String goal) {
        if (userId == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("goal", goal);
        data.put("userId", userId);
        data.put("progress", 0);
        data.put("lastUpdated", new Date());
        data.put("history", new HashMap<String, Object>());
        data.put("advice", new ArrayList<String>());

        db.collection("userGoals").document(userId)
                .set(data)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(),
                                "Meta seleccionada: " + goal,
                                Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Error guardando meta",
                                Toast.LENGTH_SHORT).show());
    }

    // -----------------------
    // 🔥 PROGRESO DIARIO
    // -----------------------
    private void loadUserDataAndUpdateUI() {
        if (userId == null) return;

        // Cargar meta seleccionada
        db.collection("userGoals").document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.contains("goal")) {
                        autoCompleteTextViewGoals.setText(doc.getString("goal"), false);
                    }
                });

        // ---- Cargar Progreso Diario ----
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());

        db.collection("dailyGoals").document(userId)
                .get()
                .addOnSuccessListener(doc -> {

                    // Si NO existe → crear documento base
                    if (!doc.exists()) {
                        Map<String, Object> init = new HashMap<>();
                        init.put("date", today);
                        init.put("progress", 0);
                        init.put("goal", autoCompleteTextViewGoals.getText().toString());

                        db.collection("dailyGoals").document(userId).set(init);

                        progress = 0;
                        progressDaily.setProgress(progress, true);
                        return;
                    }

                    String savedDate = doc.getString("date");

                    // Día igual → cargar progreso
                    if (today.equals(savedDate) && doc.contains("progress")) {
                        progress = doc.getLong("progress").intValue();
                    } else {
                        // Día diferente → Reiniciar
                        progress = 0;

                        Map<String, Object> reset = new HashMap<>();
                        reset.put("date", today);
                        reset.put("progress", 0);
                        if (doc.contains("goal")) reset.put("goal", doc.get("goal"));

                        db.collection("dailyGoals").document(userId).set(reset);
                    }

                    progressDaily.setProgress(progress, true);

                })
                .addOnFailureListener(e -> {
                    progress = 0;
                    progressDaily.setProgress(progress, true);
                });
    }

}
