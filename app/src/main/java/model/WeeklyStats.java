package model;


import java.util.HashMap;
import java.util.Map;

public class WeeklyStats {
    private int totalSesiones;
    private int totalMinutos;
    private int rachaDias;
    // Mapa de fecha (yyyy-MM-dd) → progreso del día (0-100)
    private Map<String, Integer> progresoPorDia;

    public WeeklyStats() {
        progresoPorDia = new HashMap<>();
    }

    public int getTotalSesiones() { return totalSesiones; }
    public void setTotalSesiones(int totalSesiones) { this.totalSesiones = totalSesiones; }
    public int getTotalMinutos() { return totalMinutos; }
    public void setTotalMinutos(int totalMinutos) { this.totalMinutos = totalMinutos; }
    public int getRachaDias() { return rachaDias; }
    public void setRachaDias(int rachaDias) { this.rachaDias = rachaDias; }
    public Map<String, Integer> getProgresoPorDia() { return progresoPorDia; }
    public void setProgresoPorDia(Map<String, Integer> progresoPorDia) { this.progresoPorDia = progresoPorDia; }
}
