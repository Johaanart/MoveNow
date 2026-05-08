package model;

public class User {
    private String uid;
    private String nombre;
    private String email;
    private String nivel;
    private String objetivo;
    private boolean tieneDolor;
    private boolean yaClasificado;

    public User() {}

    public User(String uid, String nombre, String email) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public boolean isTieneDolor() { return tieneDolor; }
    public void setTieneDolor(boolean tieneDolor) { this.tieneDolor = tieneDolor; }
    public boolean isYaClasificado() { return yaClasificado; }
    public void setYaClasificado(boolean yaClasificado) { this.yaClasificado = yaClasificado; }
}