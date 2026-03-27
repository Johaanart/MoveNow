package com.example.movenow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import java.util.Random;

public class HealthInfoFragment extends Fragment {
    private boolean isConsejoVisible = false;
    private final String[] consejos = {
            "¡PARA! Deja de hacer lo que haces y tomate 5 min para relfexionar.",
            "Bebe un vaso de agua, debn ser 8 al día.",
            "Evita pasar más de 30 minutos seguidos sentado.",
            "Camina al menos 15 minutos cada día.",
            "Respira profundo y relaja tus hombros cada tanto.",
            "Duerme al menos 7 horas cada noche.",
            "Evita mirar pantallas justo antes de dormir.",
            "Haz pausas activas mientras trabajas o estudias.",
            "Come despacio y disfruta cada bocado.",
            "Sal a tomar aire fresco al menos una vez al día.",
            "Recuerda completar tú rutina diaria.",
            "No excedas las rutinas (Por eso son cada 4 horas)."
    };

    public HealthInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        // --- Toggle de consejo diario ---
        LinearLayout consejoToggle = view.findViewById(R.id.consejoToggle);
        LinearLayout consejoLayout = view.findViewById(R.id.consejoLayout);
        ImageView arrowIcon = view.findViewById(R.id.arrowIcon);
        TextView consejoTexto = view.findViewById(R.id.consejoTexto);

        consejoTexto.setText(getConsejoAleatorio());

        consejoToggle.setOnClickListener(v -> {
            isConsejoVisible = !isConsejoVisible;
            consejoLayout.setVisibility(isConsejoVisible ? View.VISIBLE : View.GONE);
            arrowIcon.setRotation(isConsejoVisible ? 180 : 0);
        });

        // --- Gráfica semanal (estática) ---
        BarChart barChart = view.findViewById(R.id.weeklyChart);
        setupWeeklyChart(barChart);

        return view;
    }

    // --- Gráfica estática de minutos activos ---
    private void setupWeeklyChart(BarChart chart) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        float[] activeMinutes = {30, 45, 20, 60, 50, 40, 35}; // Datos ejemplo
        for (int i = 0; i < activeMinutes.length; i++) {
            entries.add(new BarEntry(i, activeMinutes[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Minutos activos por día");
        dataSet.setColor(Color.parseColor("#4CAF50")); // Verde MoveNow
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData data = new BarData(dataSet);
        chart.setData(data);

        // Ejes y formato
        String[] days = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // ✅ ValueFormatter correcto (para MPAndroidChart 3.1.0)
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value >= 0 && value < days.length) {
                    return days[(int) value];
                } else {
                    return "";
                }
            }
        };
        xAxis.setValueFormatter(formatter);

        // Configurar ejes
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);

        // Estilo general
        chart.getDescription().setEnabled(false);
        chart.getLegend().setTextSize(12f);
        chart.animateY(1000);
        chart.invalidate();
    }

    // --- Consejo aleatorio ---
    private String getConsejoAleatorio() {
        Random random = new Random();
        return consejos[random.nextInt(consejos.length)];
    }
}
