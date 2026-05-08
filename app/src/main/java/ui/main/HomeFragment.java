package ui.main;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.movenow.R;
import viewmodel.HomeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.notifications.NotificationScheduler;

public class HomeFragment extends Fragment {

    private TextView tvGreeting;
    private MaterialButton btnStart, btnProgreso;
    private LinearProgressIndicator progressDaily;
    private TextInputLayout menuGoals;
    private AutoCompleteTextView autoCompleteTextViewGoals;

    private HomeViewModel viewModel;
    private FirebaseFirestore db;
    private String userId;

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
        util.notifications.NotificationScheduler
                .startNotifications(requireContext());

        // ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        db = FirebaseFirestore.getInstance();

        // Referencias UI
        tvGreeting = view.findViewById(R.id.tvGreeting);
        btnStart = view.findViewById(R.id.btnStart);
        btnProgreso = view.findViewById(R.id.btnProgreso);
        progressDaily = view.findViewById(R.id.progressDaily);
        menuGoals = view.findViewById(R.id.menuGoals);
        autoCompleteTextViewGoals = view.findViewById(R.id.autoCompleteTextViewGoals);

        // Observar perfil del usuario
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userId = user.getUid();
                String nombre = user.getNombre();
                if (nombre != null && !nombre.isEmpty()) {
                    tvGreeting.setText("¡Hola, " + nombre + "!");
                } else {
                    tvGreeting.setText("¡Hola, bienvenido a MoveNow!");
                }
            }
        });

        // Observar progreso del día
        viewModel.getTodayProgress().observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                progressDaily.setProgress(progress, true);
                btnProgreso.setOnClickListener(v ->
                        Toast.makeText(getContext(),
                                "Tu progreso actual es " + progress + "%",
                                Toast.LENGTH_SHORT).show());
            }
        });

        initGoalMenu();

        // Navegar a rutinas
        btnStart.setOnClickListener(v -> {
            viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
                if (user == null) return;
                NavController navController =
                        NavHostFragment.findNavController(HomeFragment.this);
                if (user.isYaClasificado()) {
                    navController.navigate(R.id.action_homeFragment_to_startFragment);
                } else {
                    navController.navigate(R.id.action_homeFragment_to_questionnaireFragment);
                }
            });
        });
    }

    private void initGoalMenu() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                predefinedGoals
        );
        autoCompleteTextViewGoals.setAdapter(adapter);
        autoCompleteTextViewGoals.setOnItemClickListener((parent, v, position, id) ->
                saveUserGoal(predefinedGoals[position]));

        // Cargar meta guardada
        if (userId != null) {
            db.collection("userGoals").document(userId).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists() && doc.contains("goal")) {
                            autoCompleteTextViewGoals.setText(doc.getString("goal"), false);
                        }
                    });
        }
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

        db.collection("userGoals").document(userId).set(data)
                .addOnSuccessListener(u -> Toast.makeText(getContext(),
                        "Meta seleccionada: " + goal, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Error guardando meta", Toast.LENGTH_SHORT).show());
    }
}