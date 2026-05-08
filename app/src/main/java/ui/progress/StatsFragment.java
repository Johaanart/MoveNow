package ui.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movenow.R;
import viewmodel.StatsViewModel;
import util.notifications.DateUtils;

import java.util.List;
import java.util.Map;

public class StatsFragment extends Fragment {

    private StatsViewModel viewModel;

    // Barras de progreso por día
    private ProgressBar pbLun, pbMar, pbMie, pbJue, pbVie, pbSab, pbDom;
    private TextView tvLun, tvMar, tvMie, tvJue, tvVie, tvSab, tvDom;

    // Resumen semanal
    private TextView tvTotalSesiones, tvRachaDias, tvMensaje;

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(StatsViewModel.class);

        // Barras por día
        pbLun = view.findViewById(R.id.pbLun);
        pbMar = view.findViewById(R.id.pbMar);
        pbMie = view.findViewById(R.id.pbMie);
        pbJue = view.findViewById(R.id.pbJue);
        pbVie = view.findViewById(R.id.pbVie);
        pbSab = view.findViewById(R.id.pbSab);
        pbDom = view.findViewById(R.id.pbDom);

        tvLun = view.findViewById(R.id.tvLun);
        tvMar = view.findViewById(R.id.tvMar);
        tvMie = view.findViewById(R.id.tvMie);
        tvJue = view.findViewById(R.id.tvJue);
        tvVie = view.findViewById(R.id.tvVie);
        tvSab = view.findViewById(R.id.tvSab);
        tvDom = view.findViewById(R.id.tvDom);

        // Resumen
        tvTotalSesiones = view.findViewById(R.id.tvTotalSesiones);
        tvRachaDias     = view.findViewById(R.id.tvRachaDias);
        tvMensaje       = view.findViewById(R.id.tvMensaje);

        // Observar estadísticas
        viewModel.getWeeklyStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats == null) return;

            List<String> weekDays = DateUtils.getCurrentWeekDays();
            Map<String, Integer> progreso = stats.getProgresoPorDia();

            ProgressBar[] bars = {pbLun, pbMar, pbMie, pbJue, pbVie, pbSab, pbDom};
            TextView[] labels  = {tvLun, tvMar, tvMie, tvJue, tvVie, tvSab, tvDom};
            String[] nombres   = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};

            for (int i = 0; i < 7; i++) {
                String fecha = weekDays.get(i);
                int p = progreso.containsKey(fecha) ? progreso.get(fecha) : 0;
                bars[i].setProgress(p);
                labels[i].setText(nombres[i] + "\n" + p + "%");
            }

            // Resumen
            tvTotalSesiones.setText("Sesiones esta semana: " + stats.getTotalSesiones());
            tvRachaDias.setText("🔥 Racha: " + stats.getRachaDias() + " días");

            // Mensaje motivacional
            if (stats.getRachaDias() >= 5) {
                tvMensaje.setText("¡Increíble semana! Sigue así 💪");
            } else if (stats.getTotalSesiones() >= 3) {
                tvMensaje.setText("¡Vas muy bien! No pares ahora 🚀");
            } else if (stats.getTotalSesiones() > 0) {
                tvMensaje.setText("¡Buen inicio! Cada día cuenta 🌱");
            } else {
                tvMensaje.setText("¡Hoy es un buen día para empezar! 💙");
            }
        });
    }
}
