package ui.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.movenow.R;

public class ProgressFragment extends Fragment {

    private ProgressBar pbEjercicio, pbAgua;
    private TextView tvEjercicio, tvAgua;
    private Button btnDetalles;

    public ProgressFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_progress, container, false);

        pbEjercicio = view.findViewById(R.id.pbEjercicio);
        pbAgua = view.findViewById(R.id.pbAgua);
        tvEjercicio = view.findViewById(R.id.tvEjercicio);
        tvAgua = view.findViewById(R.id.tvAgua);
        btnDetalles = view.findViewById(R.id.btnDetalles);

        // 🔹 Ejemplo de valores (estos luego se conectarían con la lógica real o IA)
        int progresoEjercicio = 75;
        int progresoAgua = 60;

        pbEjercicio.setProgress(progresoEjercicio);
        pbAgua.setProgress(progresoAgua);

        tvEjercicio.setText("Ejercicio físico: " + progresoEjercicio + "%");
        tvAgua.setText("Hidratación: " + progresoAgua + "%");

        btnDetalles.setOnClickListener(v ->
                Toast.makeText(getContext(), "Más detalles próximamente 👀", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
