package com.ARmony.chatARmony.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class ChatService {

    /**
     * Construye el prompt que se enviará a la API de OpenAI.
     *
     * @param userMessage Mensaje original que escribió el usuario en el foro.
     * @param emotion     Emoción detectada (por ahora puedes mandar "neutral" si no tienes otra cosa).
     * @return Prompt completo en español listo para enviar al modelo.
     */
    public String buildPrompt(String userMessage, String emotion) {

        String emotionNormalized = normalize(emotion);

        return """
                Eres un asistente emocional breve y empático llamado Emonical.
                Tu misión es responder en ESPAÑOL con mensajes cortos (máximo 3–4 oraciones),
                cálidos, claros y fáciles de entender.

                CONTEXTO:
                - El usuario participa en un foro de bienestar emocional.
                - Emoción principal del usuario: %s
                - Mensaje del usuario: "%s"

                INSTRUCCIONES PARA TU RESPUESTA:
                1. Valida cómo se siente el usuario y hazle saber que no está solo.
                2. Da una frase motivacional concreta, evitando frases vacías como "todo estará bien".
                3. Propón UNA acción sencilla y realista que pueda hacer hoy
                   (por ejemplo: hablar con alguien de confianza, escribir lo que siente,
                   hacer respiraciones profundas, tomar una pequeña pausa, etc.).
                4. Si notas señales de autodaño, suicidio, violencia fuerte o peligro serio,
                   dilo de forma clara y anima a buscar AYUDA PROFESIONAL URGENTE
                   (psicólogo, línea de ayuda, familiar de confianza), sin dar diagnósticos.
                5. No menciones diagnósticos médicos ni nombres de trastornos.
                6. No superes las 120 palabras.
                7. Responde solo con texto plano, sin listas, sin viñetas, sin emojis y sin comillas.
                """.formatted(emotionNormalized, userMessage);
    }

    /**
     * Versión simple usada actualmente por MainController.
     * Desde el front solo se envía el mensaje del usuario, así que aquí asumimos emoción "neutral".
     */
    public String getPrompt(String userMessage) {
        String defaultEmotion = "neutral";
        return buildPrompt(userMessage, defaultEmotion);
    }

    // -------------------- Helpers --------------------

    private String normalize(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}
