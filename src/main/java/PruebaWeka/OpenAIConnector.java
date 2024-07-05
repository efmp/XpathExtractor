package PruebaWeka;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class OpenAIConnector {

    private static final String API_KEY = "insert-apikey";
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private final OkHttpClient client;

    public OpenAIConnector() {
        this.client = new OkHttpClient();
    }

    public String getResponse(String prompt) throws IOException {
        // Crear JSON para la solicitud
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "gpt-3.5-turbo-instruct");
        jsonObject.put("prompt", prompt);
        jsonObject.put("max_tokens", 100);

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Crear solicitud
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        // Hacer la solicitud y manejar la respuesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Parsear la respuesta JSON
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getJSONArray("choices").getJSONObject(0).getString("text");
        }
    }

    public static void main(String[] args) {
        OpenAIConnector openAIConnector = new OpenAIConnector();
        try {
            String prompt = "Send your prompt";
            String response = openAIConnector.getResponse(prompt);
            System.out.println("Respuesta de OpenAI: " + response);
        } catch (IOException e) {
            System.err.println("Error");
            e.printStackTrace();
        }
    }



}
