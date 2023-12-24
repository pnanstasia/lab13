package com.example.task3;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class OpenAIEnricher {
    @SneakyThrows
    public static CompanyData extract(String domain) {
        URL url = new URL(domain);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
        connection.disconnect();

        try {
            String apiKey = "";
            String chat = "https://api.openai.com/v1/engines/text-davinci-003/completions";
            String request = "Extract address and logo from " + text;
            JSONObject payloadJson = new JSONObject();
            payloadJson.put("prompt", request);
            String payload = payloadJson.toString();

            URL url2 = new URL(chat);
            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
            connection2.setRequestMethod("POST");
            connection2.setRequestProperty("Content-Type", "application/json");
            connection2.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection2.setDoOutput(true);

            try (OutputStream os = connection2.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection2.getResponseCode();
            StringBuilder response = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection2.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
            } else {
                System.out.println("Error: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection2.getErrorStream()))) {
                    String errorInputLine;
                    while ((errorInputLine = errorReader.readLine()) != null) {
                        response.append(errorInputLine);
                    }
                }
            }

            connection2.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");

            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                String generatedText = firstChoice.getString("text");
                JSONObject generatedJson = new JSONObject(generatedText);
                String address = generatedJson.getString("address");
                String logo = generatedJson.getString("logo");
                return new CompanyData(domain, logo, address);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        String domain = "https://nltu.edu.ua";
        extract(domain);
    }
}
