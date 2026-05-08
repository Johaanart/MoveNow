package repository;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.WeeklyStats;
import util.notifications.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressRepository {

    private static final String TAG = "ProgressRepository";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String getUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return null;
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // ── Viene de DailyProgressManager.addProgress ──────────────────────────
    public void addProgress(int amount) {
        String userId = getUserId();
        if (userId == null) return;

        String today = DateUtils.today();

        db.collection("dailyGoals").document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    int currentProgress = 0;
                    if (doc != null && doc.exists()) {
                        String savedDate = doc.getString("date");
                        if (today.equals(savedDate) && doc.contains("progress")) {
                            Long p = doc.getLong("progress");
                            currentProgress = p != null ? p.intValue() : 0;
                        }
                    }
                    int newProgress = Math.min(currentProgress + amount, 100);

                    Map<String, Object> update = new HashMap<>();
                    update.put("date", today);
                    update.put("progress", newProgress);

                    db.collection("dailyGoals").document(userId)
                            .set(update, SetOptions.merge())
                            .addOnSuccessListener(a -> Log.d(TAG, "Progreso: " + newProgress))
                            .addOnFailureListener(e -> Log.e(TAG, "Error guardando progreso", e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error leyendo progreso", e));
    }

    // ── Viene de FirestoreHelper.saveCompletedSession ───────────────────────
    public void saveCompletedSession() {
        String userId = getUserId();
        if (userId == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("lastCompleted", System.currentTimeMillis());
        data.put("fecha", DateUtils.today());

        db.collection("users").document(userId)
                .collection("progreso").document("rutina")
                .set(data, SetOptions.merge());
    }

    // ── Viene de FirestoreHelper.getLastCompletedSession ────────────────────
    public LiveData<Long> getLastCompletedSession() {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        String userId = getUserId();
        if (userId == null) {
            liveData.setValue(0L);
            return liveData;
        }

        db.collection("users").document(userId)
                .collection("progreso").document("rutina")
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.contains("lastCompleted")) {
                        Long last = doc.getLong("lastCompleted");
                        liveData.setValue(last != null ? last : 0L);
                    } else {
                        liveData.setValue(0L);
                    }
                })
                .addOnFailureListener(e -> liveData.setValue(0L));

        return liveData;
    }

    // ── NUEVO: Estadísticas semanales ────────────────────────────────────────
    public LiveData<WeeklyStats> getWeeklyStats() {
        MutableLiveData<WeeklyStats> liveData = new MutableLiveData<>();
        String userId = getUserId();
        if (userId == null) {
            liveData.setValue(new WeeklyStats());
            return liveData;
        }

        List<String> weekDays = DateUtils.getCurrentWeekDays();

        db.collection("dailyGoals")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(query -> {
                    WeeklyStats stats = new WeeklyStats();
                    Map<String, Integer> progresoPorDia = new HashMap<>();
                    int totalSesiones = 0;
                    int rachaDias = 0;
                    String today = DateUtils.today();

                    // Inicializar todos los días de la semana en 0
                    for (String day : weekDays) {
                        progresoPorDia.put(day, 0);
                    }

                    // Llenar con datos reales de Firestore
                    for (var doc : query.getDocuments()) {
                        String fecha = doc.getString("date");
                        if (fecha != null && weekDays.contains(fecha)) {
                            Long p = doc.getLong("progress");
                            int progreso = p != null ? p.intValue() : 0;
                            progresoPorDia.put(fecha, progreso);
                            if (progreso > 0) totalSesiones++;
                        }
                    }

                    // Calcular racha: días consecutivos hacia atrás desde hoy con progreso > 0
                    for (int i = weekDays.size() - 1; i >= 0; i--) {
                        String dia = weekDays.get(i);
                        if (!(dia.compareTo(today) > 0)) {
                            Integer prog = progresoPorDia.get(dia);
                            if (prog != null && prog > 0) {
                                rachaDias++;
                            } else {
                                break;
                            }
                        }
                    }

                    stats.setProgresoPorDia(progresoPorDia);
                    stats.setTotalSesiones(totalSesiones);
                    stats.setRachaDias(rachaDias);
                    liveData.setValue(stats);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error cargando estadísticas", e);
                    liveData.setValue(new WeeklyStats());
                });

        return liveData;
    }

    // Progreso del día actual
    public LiveData<Integer> getTodayProgress() {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        String userId = getUserId();
        if (userId == null) {
            liveData.setValue(0);
            return liveData;
        }

        String today = DateUtils.today();
        db.collection("dailyGoals").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && today.equals(doc.getString("date"))) {
                        Long p = doc.getLong("progress");
                        liveData.setValue(p != null ? p.intValue() : 0);
                    } else {
                        liveData.setValue(0);
                    }
                })
                .addOnFailureListener(e -> liveData.setValue(0));

        return liveData;
    }
}
