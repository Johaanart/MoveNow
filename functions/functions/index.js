// Importaciones necesarias (actualizadas para V2 y ES Modules)
import { onSchedule } from "firebase-functions/v2/scheduler";
import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import OpenAI from "openai";
import "dotenv/config"; // Carga variables de .env al inicio

// Inicializar Firebase Admin SDK
initializeApp();
const db = getFirestore();

// Inicializar cliente de OpenAI con la API Key desde .env
const openai = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY,
});

// Función programada para generar consejos (sintaxis V2)
export const generarConsejos = onSchedule(
  {
    schedule: "every 24 hours",
    timeZone: "America/Bogota",
  },
  async (event) => {
    console.log("⏰ Iniciando generación de consejos IA...");

    try {
      const snapshot = await db.collection("userGoals").get();
      if (snapshot.empty) {
        console.log("No hay usuarios en userGoals.");
        return null;
      }

      for (const doc of snapshot.docs) {
        const data = doc.data();
        const goal = data.goals || "sin meta definida";
        const progress = data.progress || 0;

        const prompt = `
Eres un asistente de bienestar. Da tres consejos breves (en español) para ayudar al usuario
a cumplir su meta: "${goal}". Sé cálido, motívalo a cambiar su estilo de vida, no solo a hacer ejercicio.
Su progreso actual es ${progress}%. Los consejos deben ser positivos, breves y claros.
Hazle saber que eres una IA de acompañamiento de la app MoveNow.
        `;

        // Llamada a OpenAI
        const completion = await openai.chat.completions.create({
          model: "gpt-4o-mini",
          messages: [{ role: "user", content: prompt }],
          temperature: 0.7,
        });

        const texto = completion.choices[0]?.message?.content || "";
        // Un filtro más robusto para los consejos
        const consejos = texto
          .split('\n')
          .map(line => line.replace(/^[0-9-.\s*]+/, '').trim())
          .filter(line => line.length > 10);

        // Actualizar el documento del usuario
        await doc.ref.update({
          advice: consejos,
          // `FieldValue.serverTimestamp()` no está disponible en la API modular,
          // se puede omitir o usar `new Date()` si es aceptable
          lastUpdated: new Date(),
        });

        console.log(`✅ Consejos generados para ${doc.id}:`, consejos);
      }

      console.log("🎯 Consejos actualizados correctamente.");
      return null;
    } catch (error) {
      console.error("❌ Error generando consejos:", error);
      // Captura de errores más específica de OpenAI
      if (error instanceof OpenAI.APIError) {
        console.error("Error de OpenAI:", error.status, error.message, error.code, error.type);
      }
      return null;
    }
  }
);
