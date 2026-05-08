package ui.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.movenow.DailyProgressManager;
import com.example.movenow.FirestoreHelper;
import com.example.movenow.R;
import com.example.movenow.RoutineManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StartFragment extends Fragment {

    private ImageButton btnBack;
    private Button btnDoAction, btnStopRoutine;
    private TextView tvMainTitle, tvStartDescription, tvTimer;
    private ImageView imgRoutine;

    private CountDownTimer currentRoutineTimer;
    private boolean isRoutineRunning = false;
    private boolean isPaused = false;

    private long timeRemaining = 0;
    private int currentRoutineIndex = -1;

    private List<RoutineManager.Routine> allRoutines;
    private List<RoutineManager.Routine> remainingRoutines;
    private List<RoutineManager.Routine> recommendedRoutines;

    private final long ROUTINE_DURATION = 60 * 1000;

    private boolean loadedFromProfile = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_start, container, false);

        btnBack = root.findViewById(R.id.btnBack);
        btnDoAction = root.findViewById(R.id.btnDoAction);
        btnStopRoutine = root.findViewById(R.id.btnStopRoutine);
        tvMainTitle = root.findViewById(R.id.tvMainTitle);
        tvStartDescription = root.findViewById(R.id.tvStartDescription);
        tvTimer = root.findViewById(R.id.tvTimer);
        imgRoutine = root.findViewById(R.id.imgRoutine);

        allRoutines = RoutineManager.getAllRoutines();

        FirestoreHelper helper = new FirestoreHelper();
        helper.getUserProfile(perfil -> {

            if (perfil != null && perfil.containsKey("yaClasificado") && (boolean) perfil.get("yaClasificado")) {
                loadedFromProfile = true;

                String nivel = perfil.get("nivel").toString();
                String objetivo = perfil.get("objetivo").toString();
                boolean dolor = (boolean) perfil.get("tieneDolor");

                boolean hasExercised = nivel.equals("Intermedio") || nivel.equals("Avanzado");
                String intensity = nivel.equals("Avanzado") ? "Intenso" : "Suave";

                this.recommendedRoutines =
                        recommendRoutines(hasExercised, intensity, dolor, objetivo);

                resetRoutineStateWithRecommendations();

            } else {
                loadedFromProfile = false;
                loadFromQuestionnaire();
            }
        });

        btnDoAction.setOnClickListener(v -> {
            if (!isRoutineRunning) {
                startRandomRoutine();
            } else {
                changeToNextRoutine(); // PROGRESO +20% AQUÍ
            }
        });

        btnStopRoutine.setOnClickListener(v -> togglePauseResume());

        btnBack.setOnClickListener(v -> {
            if (currentRoutineTimer != null) currentRoutineTimer.cancel();
            Navigation.findNavController(v).popBackStack();
        });

        return root;
    }


    private void loadFromQuestionnaire() {
        boolean userHasExercised = getArguments() != null ? getArguments().getBoolean("USER_HAS_EXERCISED") : false;
        boolean userHasPain = getArguments() != null ? getArguments().getBoolean("USER_HAS_PAIN") : false;
        String userIntensity = getArguments() != null ? getArguments().getString("USER_INTENSITY", "Suave") : "Suave";
        String userGoal = getArguments() != null ? getArguments().getString("USER_GOAL", "Fuerza") : "Fuerza";

        this.recommendedRoutines =
                recommendRoutines(userHasExercised, userIntensity, userHasPain, userGoal);

        resetRoutineStateWithRecommendations();
    }


    private List<RoutineManager.Routine> recommendRoutines(boolean hasExercised, String intensity,
                                                           boolean hasPain, String goal) {

        List<RoutineManager.RoutineCategory> targetCategories = new ArrayList<>();

        if (hasPain) {
            targetCategories.add(RoutineManager.RoutineCategory.PRINCIPIANTE);
            targetCategories.add(RoutineManager.RoutineCategory.FLEXIBILIDAD);
        } else if (hasExercised) {
            if (intensity.equals("Intenso")) {
                targetCategories.add(RoutineManager.RoutineCategory.AVANZADO);
                targetCategories.add(RoutineManager.RoutineCategory.INTERMEDIO);
            } else targetCategories.add(RoutineManager.RoutineCategory.INTERMEDIO);
        } else targetCategories.add(RoutineManager.RoutineCategory.PRINCIPIANTE);

        if (goal.equals("Fuerza") && !hasPain &&
                !targetCategories.contains(RoutineManager.RoutineCategory.INTERMEDIO)) {
            targetCategories.add(RoutineManager.RoutineCategory.INTERMEDIO);
        }

        if (goal.equals("Flexibilidad") &&
                !targetCategories.contains(RoutineManager.RoutineCategory.FLEXIBILIDAD))
            targetCategories.add(RoutineManager.RoutineCategory.FLEXIBILIDAD);

        List<RoutineManager.Routine> warmups = new ArrayList<>();
        List<RoutineManager.Routine> selected = new ArrayList<>();

        for (RoutineManager.Routine r : allRoutines) {
            if (r.category == RoutineManager.RoutineCategory.CALENTAMIENTO)
                warmups.add(r);
            else if (targetCategories.contains(r.category))
                selected.add(r);
        }

        List<RoutineManager.Routine> finalList = new ArrayList<>();

        if (!warmups.isEmpty()) {
            RoutineManager.Routine warm = warmups.get(new Random().nextInt(warmups.size()));
            finalList.add(warm);
        }

        selected.removeIf(r -> r.category == RoutineManager.RoutineCategory.CALENTAMIENTO);

        Collections.shuffle(selected);
        int max = 4;
        if (selected.size() > max) finalList.addAll(selected.subList(0, max));
        else finalList.addAll(selected);

        return finalList;
    }


    private void routineCompleted() {

        isRoutineRunning = false;

        if (currentRoutineIndex != -1 && currentRoutineIndex < remainingRoutines.size())
            remainingRoutines.remove(currentRoutineIndex);

        // SUMAR PROGRESO (20%)
        DailyProgressManager pm = new DailyProgressManager();
        pm.addProgress(20); // PROGRESO +20%

        if (!remainingRoutines.isEmpty()) {

            tvTimer.postDelayed(this::startRandomRoutine, 2000);

        } else {
            Toast.makeText(getContext(), "¡Entrenamiento completado! 🏆", Toast.LENGTH_LONG).show();
            resetRoutineStateWithRecommendations();
        }
    }


    private void resetRoutineStateWithRecommendations() {

        isRoutineRunning = false;
        isPaused = false;
        currentRoutineIndex = -1;

        if (recommendedRoutines != null)
            remainingRoutines = new ArrayList<>(recommendedRoutines);

        if (currentRoutineTimer != null && !isRoutineRunning)
            currentRoutineTimer.cancel();

        tvMainTitle.setText("¡Plan listo! 🚀");
        tvStartDescription.setText("Presiona iniciar para comenzar una nueva rutina.");
        tvTimer.setText("Esperando...");
        imgRoutine.setImageResource(R.drawable.estretch1);
        btnDoAction.setText("Iniciar Rutina Personalizada");
        btnDoAction.setEnabled(true);
        btnStopRoutine.setVisibility(View.GONE);
    }


    private void startRandomRoutine() {

        if (remainingRoutines == null || remainingRoutines.isEmpty()) {
            Toast.makeText(getContext(), "¡Completaste todas las rutinas! 🎉", Toast.LENGTH_LONG).show();
            resetRoutineStateWithRecommendations();
            return;
        }

        currentRoutineIndex = new Random().nextInt(remainingRoutines.size());
        RoutineManager.Routine r = remainingRoutines.get(currentRoutineIndex);
        startRoutine(r.title, r.description, r.imageResId);
    }


    private void startRoutine(String title, String desc, int imageResId) {

        isRoutineRunning = true;
        isPaused = false;
        timeRemaining = ROUTINE_DURATION;

        tvMainTitle.setText(title + " (" + remainingRoutines.size() + " restantes)");
        tvStartDescription.setText(desc);
        imgRoutine.setImageResource(imageResId);

        btnDoAction.setText("Cambiar Rutina 🔄");
        btnStopRoutine.setText("Pausar ⏸️");
        btnStopRoutine.setVisibility(View.VISIBLE);

        startTimer(ROUTINE_DURATION);
    }


    private void changeToNextRoutine() {

        if (!isRoutineRunning) return;

        if (currentRoutineTimer != null)
            currentRoutineTimer.cancel();

        // ELIMINAR RUTINA ACTUAL
        if (currentRoutineIndex != -1 && currentRoutineIndex < remainingRoutines.size())
            remainingRoutines.remove(currentRoutineIndex);

        // SUMAR PROGRESO POR CAMBIO DE RUTINA
        DailyProgressManager pm = new DailyProgressManager();
        pm.addProgress(20); // PROGRESO +20% AQUÍ TAMBIÉN

        Toast.makeText(getContext(), "Rutina cambiada ✔️", Toast.LENGTH_SHORT).show();

        startRandomRoutine();
    }


    private void togglePauseResume() {

        if (!isRoutineRunning) return;

        if (!isPaused) {
            if (currentRoutineTimer != null)
                currentRoutineTimer.cancel();

            isPaused = true;
            btnStopRoutine.setText("Continuar ▶️");
            tvTimer.setText("Pausado");

        } else {
            isPaused = false;
            btnStopRoutine.setText("Pausar ⏸️");
            startTimer(timeRemaining);
        }
    }


    private void startTimer(long duration) {

        currentRoutineTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millis) {
                timeRemaining = millis;
                tvTimer.setText("⏱ " + (millis / 1000) + "s");
            }

            public void onFinish() {
                routineCompleted();
            }
        }.start();
    }
}
