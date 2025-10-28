package com.uncuentoparati.formulario.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class IaService {

    private final String API_KEY = System.getenv("OPENAI_API_KEY");

    public String generarRecomendacion(List<String> perfil) {
        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                return "Error: No se encontró la API Key de OpenAI en las variables de entorno.";
            }

            String prompt = "Soy un sistema que recomienda cuentos. Con base en este perfil lector: "
                    + String.join(", ", perfil)
                    + ", recomienda tres cuentos adecuados y explica brevemente por qué.";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "model": "gpt-4o-mini",
                          "messages": [{"role":"user", "content":"%s"}]
                        }
                    """.formatted(prompt)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("RESPUESTA DE OPENAI: " + response.body());

            JSONObject json = new JSONObject(response.body());

            if (!json.has("choices")) {
                return "Error: OpenAI no devolvió 'choices'. Revisa la API Key o el request.";
            }

            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectarse con OpenAI: " + e.getMessage();
        }
    }
}
