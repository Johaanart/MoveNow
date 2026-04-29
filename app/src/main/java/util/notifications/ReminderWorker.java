package util.notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context ctx = getApplicationContext();

        // 1. No enviar si es horario nocturno
        if (isQuietHours()) {
            Log.d("Notification", "No se envía notificación: horario nocturno.");
            return Result.success();
        }

        // 2. Si las condiciones son correctas, enviar la notificación directamente.
        // Se ha eliminado la lógica de cooldown de 4 horas.
        Log.d("Notification", "Enviando notificación de recordatorio.");

        String mensaje = NotificationMessages.getMensajeAleatorio();
        NotificationHelper.showNotification(
                ctx,
                "MoveNow te cuida 💙",
                mensaje
        );

        return Result.success();
    }

    /**
     * Detecta si es hora de NO enviar notificaciones (ej. horario nocturno).
     * @return true si es horario de no molestar, false en caso contrario.
     */
    private boolean isQuietHours() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // No molestar entre las 10 PM (22:00) y las 7 AM (07:00)
        return (hour >= 22 || hour < 7);
    }
}
