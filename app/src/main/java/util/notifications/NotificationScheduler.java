package util.notifications;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    private static final String UNIQUE_WORK_NAME = "MoveNow_Notifications";

    /** ✅ Activa el envío de notificaciones cada 2 horas */
    public static void startNotifications(Context context) {

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        ReminderWorker.class,
                        2, TimeUnit.HOURS // ✅ NOTIFICACIÓN CADA 2 HORAS
                )
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE, // 🔥 Actualiza si algo cambió (mejor que KEEP)
                request
        );
    }

    /** Detiene todas las notificaciones programadas */
    public static void stopNotifications(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME);
    }
}
