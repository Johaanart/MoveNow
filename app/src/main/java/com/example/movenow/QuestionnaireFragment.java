package com.example.movenow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class QuestionnaireFragment extends Fragment {

    private RadioGroup rgExperience, rgPain, rgIntensity, rgGoal;
    private Button btnGenerateRoutine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        rgExperience = view.findViewById(R.id.rgExperience);
        rgPain = view.findViewById(R.id.rgPain);
        rgIntensity = view.findViewById(R.id.rgIntensity);
        rgGoal = view.findViewById(R.id.rgGoal);
        btnGenerateRoutine = view.findViewById(R.id.btnGenerateRoutine);

        btnGenerateRoutine.setOnClickListener(v -> {

            // -------------------------------------------------------------
            // 1. Recoger respuestas
            // -------------------------------------------------------------
            boolean hasExercised = ((RadioButton) view.findViewById(rgExperience.getCheckedRadioButtonId()))
                    .getText().toString().equals("Sí");

            boolean hasPain = ((RadioButton) view.findViewById(rgPain.getCheckedRadioButtonId()))
                    .getText().toString().equals("Sí");

            String intensity = ((RadioButton) view.findViewById(rgIntensity.getCheckedRadioButtonId()))
                    .getText().toString(); // "Suave" o "Intenso"

            String goal = ((RadioButton) view.findViewById(rgGoal.getCheckedRadioButtonId()))
                    .getText().toString(); // "Fuerza" o "Flexibilidad"


            // -------------------------------------------------------------
            // 2. Determinar nivel según reglas
            // -------------------------------------------------------------
            String finalLevel;

            if (hasPain) {
                finalLevel = "Principiante";
            } else if (hasExercised) {
                if (intensity.equals("Intenso")) {
                    finalLevel = "Avanzado";
                } else {
                    finalLevel = "Intermedio";
                }
            } else {
                finalLevel = "Principiante";
            }

            // Ajuste por meta (no afecta nivel)
            if (goal.equals("Flexibilidad") && !finalLevel.equals("Avanzado")) {
                // No cambia nada, solo se usa después
            }


            // -------------------------------------------------------------
            // 3. Guardar datos en Firestore
            // -------------------------------------------------------------
            FirestoreHelper dbHelper = new FirestoreHelper();
            dbHelper.saveUserProfile(finalLevel, goal, hasPain, true);


            // -------------------------------------------------------------
            // 3.5 Guardar bandera LOCAL de que el cuestionario ya fue completado
            // -------------------------------------------------------------
            requireActivity()
                    .getSharedPreferences("MoveNowPrefs", 0)
                    .edit()
                    .putBoolean("QUESTIONNAIRE_COMPLETED", true)
                    .apply();


            // -------------------------------------------------------------
            // 4. Navegar al StartFragment eliminando este de la pila
            // -------------------------------------------------------------
            Bundle args = new Bundle();
            args.putBoolean("USER_HAS_EXERCISED", hasExercised);
            args.putBoolean("USER_HAS_PAIN", hasPain);
            args.putString("USER_INTENSITY", intensity);
            args.putString("USER_GOAL", goal);

            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.questionnaireFragment, true)
                    .build();

            NavController navController = Navigation.findNavController(view);
            navController.navigate(
                    R.id.action_questionnaireFragment_to_startFragment,
                    args,
                    navOptions
            );
        });

        return view;
    }
}
