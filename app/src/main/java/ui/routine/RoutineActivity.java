package ui.routine;

public class RoutineActivity {

    private final String title;
    private final String description;
    private final long intervalMillis; // Cada cuánto debe repetirse la rutina
    private long lastDone; // Última vez que se hizo

    // Constructor con intervalo por defecto de 1 hora
    public RoutineActivity(String title, String description) {
        this.title = title;
        this.description = description;
        this.intervalMillis = 1 * 60 * 60 * 1000; // 1 hora
        this.lastDone = 0;
    }

    // Constructor con intervalo personalizado
    public RoutineActivity(String title, String description, long intervalMillis) {
        this.title = title;
        this.description = description;
        this.intervalMillis = intervalMillis;
        this.lastDone = 0;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public long getLastDone() {
        return lastDone;
    }

    // Marcar la rutina como realizada ahora mismo
    public void markDone() {
        this.lastDone = System.currentTimeMillis();
    }

    // Verificar si ya se puede repetir la rutina según el intervalo
    public boolean isDue() {
        return (System.currentTimeMillis() - lastDone) >= intervalMillis;
    }
}
