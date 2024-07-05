package PruebaWeka;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class ExtractAndSendDOM {
    public static void main(String[] args) {
        try {
            // Configurar el path del ChromeDriver
            WebDriverManager.chromedriver().driverVersion("126.0.6478.126").setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            // Iniciar el ChromeDriver
            ChromeDriver driver = new ChromeDriver(options);

            // Navegar a la página web
            driver.get("https://www.mclibre.org/consultar/htmlcss/html/html-plantilla.html");

            // Extraer el documento DOM
            WebElement dom = driver.findElement(By.xpath("/*"));
            String domContent = dom.getAttribute("outerHTML");

            // Guardar el DOM en un archivo
            String filePath = "domContent.txt";
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(domContent);
            }

            // Leer el contenido del archivo
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Configurar la API de OpenAI
            String apiKey = "insertapikey";
            String apiUrl = "https://api.openai.com/v1/completions";

            // Crear el payload para la solicitud
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "gpt-3.5-turbo-instruct");
            payload.put("prompt", fileContent);
            payload.put("max_tokens", 100);

            // Convertir el payload a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Enviar la solicitud a la API de OpenAI
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            // Escribir el payload en la solicitud
            OutputStream os = connection.getOutputStream();
            os.write(jsonPayload.getBytes());
            os.flush();
            os.close();

            // Leer la respuesta de la API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Imprimir la respuesta de la API
                System.out.println("Respuesta de la API de OpenAI: " + response.toString());
            } else {
                System.out.println("Solicitud fallida. Código de respuesta: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
