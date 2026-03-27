package com.example.movenow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private EditText etName, etAge, etHeight, etWeight;
    private Spinner spinnerGender;
    private Button btnSave;
    private ImageButton btnSettings;
    private TextView txtProfileTitle;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Enlaces de vistas
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        btnSave = view.findViewById(R.id.btnSave);
        btnSettings = view.findViewById(R.id.btnSettings);
        txtProfileTitle = view.findViewById(R.id.txtProfileTitle);

        // Spinner de género
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Cargar datos del usuario
        loadUserData();

        // Guardar datos
        btnSave.setOnClickListener(v -> saveUserData());

        // Abrir Settings
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ CORREGIDO: ahora usa UID, no email
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String age = documentSnapshot.getString("age");
                String gender = documentSnapshot.getString("gender");
                String height = documentSnapshot.getString("height");
                String weight = documentSnapshot.getString("weight");

                if (name != null) {
                    etName.setText(name);
                    txtProfileTitle.setText("Hola, " + name.split(" ")[0] + " 👋");
                }
                if (age != null) etAge.setText(age);
                if (height != null) etHeight.setText(height);
                if (weight != null) etWeight.setText(weight);

                if (gender != null) {
                    ArrayAdapter<String> genderAdapter = (ArrayAdapter<String>) spinnerGender.getAdapter();
                    int position = genderAdapter.getPosition(gender);
                    if (position >= 0) spinnerGender.setSelection(position);
                }
            } else {
                Toast.makeText(getContext(), "No se encontraron datos guardados.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Error al cargar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "No hay sesión activa para guardar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("age", age);
        userData.put("height", height);
        userData.put("weight", weight);
        userData.put("gender", spinnerGender.getSelectedItem().toString());

        // ✅ CORREGIDO: guardamos por UID, no por email
        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "Datos guardados correctamente ✅", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
