package com.example.movenow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RoutineManager {

    // Enum para categorizar las rutinas según su intensidad y propósito.
    public enum RoutineCategory {
        CALENTAMIENTO,  // Obligatorio para todos, ejercicios suaves de preparación.
        PRINCIPIANTE,   // Bajo impacto, ideal para empezar.
        INTERMEDIO,     // Mayor intensidad y algo de fuerza.
        AVANZADO,       // Alta intensidad, ejercicios complejos.
        FLEXIBILIDAD    // Estiramientos y movilidad.
    }

    // La clase Routine ahora incluye una categoría.
    public static class Routine {
        public final String title;
        public final String description;
        public final int imageResId; // ID de la imagen en drawable
        public final RoutineCategory category; // Categoría de la rutina

        public Routine(String title, String description, int imageResId, RoutineCategory category) {
            this.title = title;
            this.description = description;
            this.imageResId = imageResId;
            this.category = category;
        }
    }

    // Lista base de rutinas, ahora clasificadas.
    private static final List<Routine> routines = new ArrayList<>(Arrays.asList(

            // --- CALENTAMIENTO SIEMPRE SE DEBE HACER---
            new Routine("Gira tus hombros",
                    "Haz 10 rotaciones suaves hacia adelante y atrás.",
                    R.drawable.shoulder, RoutineCategory.CALENTAMIENTO),

            new Routine("Respira profundo",
                    "Respira lentamente 5 veces para activar tu cuerpo.",
                    R.drawable.breath, RoutineCategory.CALENTAMIENTO),

            new Routine("Marcha suave en el sitio",
                    "Marcha en el lugar 30 segundos para calentar piernas.",
                    R.drawable.march, RoutineCategory.CALENTAMIENTO),

            new Routine("Movilidad de pierna",
                    "Mueve las piernas (hasta donde puedas) en forma lateral  10 veces.",
                    R.drawable.legs1, RoutineCategory.CALENTAMIENTO),

            new Routine("Hidrátate 💧",
                    "Bebe agua para iniciar con buena energía (sorbos, no un vaso entero).",
                    R.drawable.water, RoutineCategory.CALENTAMIENTO),

            // --- PRINCIPIANTE ---
            new Routine("Camina 500 pasos",
                    "Activa tu circulación con una caminata ligera.",
                    R.drawable.walk, RoutineCategory.PRINCIPIANTE),

            new Routine("Hidrátate 💧",
                    "Bebe agua para iniciar con buena energía (sorbos, no un vaso entero).",
                    R.drawable.water, RoutineCategory.PRINCIPIANTE),

            new Routine("Postura de montaña",
                    "Párate derecho y respira profundo.",
                    R.drawable.mountain, RoutineCategory.PRINCIPIANTE),

            new Routine("Balancea tus piernas",
                    "Balancea cada pierna adelante y atrás 10 veces.",
                    R.drawable.legs, RoutineCategory.PRINCIPIANTE),

            new Routine("Círculos de brazos",
                    "Haz movimientos suaves con los brazos 15 veces.",
                    R.drawable.armcircle, RoutineCategory.PRINCIPIANTE),

            new Routine("Paso lateral suave",
                    "Da pasos laterales por 30 segundos.",
                    R.drawable.side, RoutineCategory.PRINCIPIANTE),

            new Routine("Trote suave en el lugar ️",
                    "Trotecito muy suave durante 20 segundos.",
                    R.drawable.cross, RoutineCategory.PRINCIPIANTE),

            new Routine("Levantamiento de rodillas",
                    "Eleva las rodillas suavemente 10 veces (pesas opcional).",
                    R.drawable.toetouch, RoutineCategory.PRINCIPIANTE),

            new Routine("Extensión de piernas",
                    "Sientate e intenta tocar tus pies sin forzar, 6 veces.",
                    R.drawable.stretching, RoutineCategory.PRINCIPIANTE),

            new Routine("Toca tus puntas",
                    "Inclínate y toca tus pies sin forzar, 10 veces.",
                    R.drawable.knees, RoutineCategory.PRINCIPIANTE),

            new Routine("Rotación de cadera",
                    "Sientate y Haz círculos con la cadera 10 veces.",
                    R.drawable.hip, RoutineCategory.PRINCIPIANTE),

            new Routine("Pausa activa básica 🎵",
                    "Muévete libremente durante 30 segundos.",
                    R.drawable.music, RoutineCategory.PRINCIPIANTE),
// --- INTERMEDIO ---
            new Routine("Sentadillas",
                    "Haz 12 sentadillas cuidando la postura.",
                    R.drawable.squat, RoutineCategory.INTERMEDIO),

            new Routine("Hidrátate 💧",
                    "Bebe agua para iniciar con buena energía (sorbos, no un vaso entero).",
                    R.drawable.water, RoutineCategory.INTERMEDIO),

            new Routine("Sube y baja escaleras",
                    "Haz 3 subidas a ritmo moderado.",
                    R.drawable.stairs, RoutineCategory.INTERMEDIO),

            new Routine("Zancadas alternas",
                    "Haz 10 por pierna manteniendo equilibrio.",
                    R.drawable.lunge, RoutineCategory.INTERMEDIO),

            new Routine("Respiración guiada 1 min",
                    "Relaja todo tu cuerpo mientras respiras profundo.",
                    R.drawable.breath, RoutineCategory.INTERMEDIO),

            new Routine("Toca tus pies",
                    "Realiza 10 repeticiones sin doblar rodillas, sin forzar.",
                    R.drawable.down, RoutineCategory.INTERMEDIO),

            new Routine("Jumping jacks",
                    "Haz 20 saltos jacks moderados.",
                    R.drawable.jump, RoutineCategory.INTERMEDIO),

            new Routine("Elevación de talones",
                    "Haz 20 para activar tus pantorrillas.",
                    R.drawable.calf, RoutineCategory.INTERMEDIO),

            new Routine("Paso rápido en escalón",
                    "Sube/baja un escalón 30 segundos.",
                    R.drawable.stairs, RoutineCategory.INTERMEDIO),

            new Routine("Rodillas arriba rápido ️",
                    "Eleva las rodillas con ritmo 15 segundos.",
                    R.drawable.kneerun, RoutineCategory.INTERMEDIO),

            new Routine("Plancha lateral 8s por lado",
                    "Activa oblicuos y equilibrio (Ten cuidado).",
                    R.drawable.sideplank, RoutineCategory.INTERMEDIO),

            new Routine("Patada atrás firme",
                    "Haz 12 repeticiones por pierna.",
                    R.drawable.kickback, RoutineCategory.INTERMEDIO),

            new Routine("Burpees modificados",
                    "Haz 5 burpees sin salto final.",
                    R.drawable.burpee, RoutineCategory.INTERMEDIO),
// --- AVANZADO ---
            new Routine("Burpees explosivos",
                    "Haz 10 burpees con salto.",
                    R.drawable.burpee, RoutineCategory.AVANZADO),

            new Routine("Sentadilla con salto",
                    "Haz 12 repeticiones potentes.",
                    R.drawable.jump_lunge, RoutineCategory.AVANZADO),

            new Routine("Plancha 1 minuto",
                    "Resiste todo el minuto apretando core.",
                    R.drawable.plank, RoutineCategory.AVANZADO),

            new Routine("Mountain climbers",
                    "Haz 30 segundos intensos.",
                    R.drawable.mountainclimber, RoutineCategory.AVANZADO),

            new Routine("Zancadas con salto",
                    "12 por pierna alternando.",
                    R.drawable.jump_lunge, RoutineCategory.AVANZADO),

            new Routine("Flexiones clásicas",
                    "Haz 15 repeticiones.",
                    R.drawable.pushup, RoutineCategory.AVANZADO),

            new Routine("Flexiones diamante",
                    "Haz 5 repeticiones cerradas 3 veces.",
                    R.drawable.diamondpush, RoutineCategory.AVANZADO),

            new Routine("Sprints en el lugar",
                    "20 segundos de velocidad máxima (ten cuidado).",
                    R.drawable.cross, RoutineCategory.AVANZADO),

            new Routine("Saltos laterales rápidos",
                    "Haz 40 saltos.",
                    R.drawable.laters, RoutineCategory.AVANZADO),

            new Routine("Rodillas arriba intenso",
                    "20 segundos al máximo ritmo.",
                    R.drawable.toetouch, RoutineCategory.AVANZADO),

            new Routine("Inclina rodillas arriba/abajo con toques al hombro",
                    "20 toques alternados.",
                    R.drawable.shoulder, RoutineCategory.AVANZADO),

            new Routine("Saltos en escalón",
                    "15 subidas explosivas.",
                    R.drawable.stairs, RoutineCategory.AVANZADO),

            new Routine("Tijeras rápidas",
                    "Haz 30 segundos.",
                    R.drawable.scissors, RoutineCategory.AVANZADO),

            new Routine("Patada de burro",
                    "20 por pierna.",
                    R.drawable.mountainclimber, RoutineCategory.AVANZADO),

            new Routine("Elevaciones de pierna acostado",
                    "15 por pierna.",
                    R.drawable.legraise, RoutineCategory.AVANZADO),

            new Routine("Saltos frontales",
                    "20 saltos potentes.",
                    R.drawable.frontjump, RoutineCategory.AVANZADO),

            new Routine("Isométrico en sentadilla 40s",
                    "Mantén la posición sin moverte.",
                    R.drawable.squat, RoutineCategory.AVANZADO),

            new Routine("Hidrátate 💧",
                    "Bebe agua para iniciar con buena energía (sorbos, no un vaso entero).",
                    R.drawable.water, RoutineCategory.AVANZADO),

            new Routine("Flexiones abiertas",
                    "15 repeticiones.",
                    R.drawable.pushup, RoutineCategory.AVANZADO),

            new Routine("Plancha con desplazamiento",
                    "Muévete lateralmente 20 segundos.",
                    R.drawable.sideplank, RoutineCategory.AVANZADO),
// --- FLEXIBILIDAD ---

            new Routine("Estiramiento de isquiotibiales",
                    "20 segundos por pierna.",
                    R.drawable.hamstring, RoutineCategory.FLEXIBILIDAD),

            new Routine("Gira tus hombros",
                    "Gira suavemente 20 segundos por lado.",
                    R.drawable.shoulder, RoutineCategory.FLEXIBILIDAD),

            new Routine("Estiramiento de brazos",
                    "Mantén la postura y estirate 15 segundos.",
                    R.drawable.quad, RoutineCategory.FLEXIBILIDAD),

            new Routine("Postura del niño",
                    "Relájate 30 segundos.",
                    R.drawable.butterfly, RoutineCategory.FLEXIBILIDAD),

            new Routine("Mariposa",
                    "Mantén 30 segundos.",
                    R.drawable.butterfly, RoutineCategory.FLEXIBILIDAD),

            new Routine("Estiramiento de glúteos",
                    "20 segundos por pierna.",
                    R.drawable.hamstring, RoutineCategory.FLEXIBILIDAD),

            new Routine("Estiramiento de espalda alta",
                    "20 segundos.",
                    R.drawable.stretch, RoutineCategory.FLEXIBILIDAD),


            new Routine("Apertura de pecho",
                    "20 segundos estirando brazos atrás.",
                    R.drawable.stretch, RoutineCategory.FLEXIBILIDAD),

            new Routine("Flexión lateral del torso",
                    "20 segundos por lado.",
                    R.drawable.estretch1, RoutineCategory.FLEXIBILIDAD),

            new Routine("Estiramiento de brazos",
                    "Extiende tus brazos por 20 segundos.",
                    R.drawable.stretch, RoutineCategory.FLEXIBILIDAD),

            new Routine("Extiende los brazos de un lado al otro",
                    "Haz 10 por dirección.",
                    R.drawable.estretch1, RoutineCategory.FLEXIBILIDAD),

            new Routine("Estiramiento frontal sentado",
                    "Inclínate hacia adelante 20 segundos.",
                    R.drawable.hip, RoutineCategory.FLEXIBILIDAD),

            new Routine("Postura de montaña",
                    "Párate derecho y respira profundo.",
                    R.drawable.mountain, RoutineCategory.FLEXIBILIDAD),

            new Routine("Hidrátate 💧",
                    "Bebe agua para iniciar con buena energía (sorbos, no un vaso entero).",
                    R.drawable.water, RoutineCategory.FLEXIBILIDAD),

            new Routine("Alza arriba/abajo un peso menos de 4 kg en cada mano",
                    "4 repeticiones.",
                    R.drawable.training, RoutineCategory.FLEXIBILIDAD),

            new Routine("Respiración guiada 1 min",
                    "Relaja todo tu cuerpo mientras respiras profundo.",
                    R.drawable.breath, RoutineCategory.FLEXIBILIDAD)


    ));

    // Devuelve una rutina aleatoria (Este método puede ser menos útil ahora)
    public static Routine getRandomRoutine() {
        Random random = new Random();
        return routines.get(random.nextInt(routines.size()));
    }

    // Devuelve todas las rutinas para que el "motor de IA" pueda filtrarlas.
    public static List<Routine> getAllRoutines() {
        return new ArrayList<>(routines);
    }

    public static List<Routine> getByCategory(RoutineCategory category) {
        return routines.stream()
                .filter(r -> r.category == category)
                .collect(Collectors.toList());
    }

}
