package util.notifications;

import java.util.Random;

public class NotificationMessages {

    private static final String[] mensajes = {
            "¡PARA! Tómate 5 min para reflexionar.",
            "Bebe un vaso de agua. 8 al día 💧",
            "Evita pasar más de 30 minutos sentado.",
            "Camina al menos 15 minutos hoy.",
            "Respira profundo y relaja tus hombros.",
            "Duerme al menos 7 horas esta noche 😴",
            "Evita mirar pantallas antes de dormir.",
            "Haz una pausa activa ahora mismo.",
            "Come despacio, disfruta tu comida.",
            "Sal a tomar aire fresco 🌿",
            "Recuerda completar tu rutina diaria 💪",
            "No excedas las rutinas (cada 4 h)."
    };

    public static String getMensajeAleatorio() {
        Random r = new Random();
        return mensajes[r.nextInt(mensajes.length)];
    }
}
