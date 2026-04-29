package util.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.movenow.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "routine_channel";
    private static final int ROUTINE_REMINDER_NOTIFICATION_ID = 1001;

    public static void showNotification(Context context, String title, String message) {

        // Crear canal (solo Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Rutinas MoveNow",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Recordatorios automáticos de rutinas MoveNow");

            NotificationManager systemManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (systemManager != null) {
                systemManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        // ⚠️ Verificar permiso antes de mostrar la notificación
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no hay permiso, no se muestra la notificación
            return;
        }

        manager.notify(ROUTINE_REMINDER_NOTIFICATION_ID, builder.build());
    }
}