package util.notifications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat SDF =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static String today() {
        return SDF.format(new Date());
    }

    public static String format(long timestamp) {
        return SDF.format(new Date(timestamp));
    }

    // Devuelve los 7 días de la semana actual (lunes a domingo)
    public static List<String> getCurrentWeekDays() {
        List<String> days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        // Ir al lunes de esta semana
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++) {
            days.add(SDF.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    // Devuelve el lunes de la semana actual como String
    public static String getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return SDF.format(cal.getTime());
    }

    // Verifica si una fecha está en la semana actual
    public static boolean isInCurrentWeek(String fecha) {
        List<String> week = getCurrentWeekDays();
        return week.contains(fecha);
    }
}