package model;

public class WorkoutSession {
    private String id;
    private String userId;
    private long timestamp;
    private int duracionMinutos;
    private String fecha; // formato yyyy-MM-dd
    private int progreso; // 0-100

    public WorkoutSession() {}

    public WorkoutSession(String userId, long timestamp, int duracionMinutos, String fecha, int progreso) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.duracionMinutos = duracionMinutos;
        this.fecha = fecha;
        this.progreso = progreso;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public int getProgreso() { return progreso; }
    public void setProgreso(int progreso) { this.progreso = progreso; }
}