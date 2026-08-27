package com.renacerfinancierousa.mirenacer;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class ApiClient {
    static final String BASE = "https://revendepro.com/finanzaspro/wp-json/rae-mobile/v1/";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    interface Callback { void complete(JSONObject json, String error, int status); }

    void login(String username, String password, Callback callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", username); body.put("password", password);
            body.put("device_name", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);
            body.put("device_id", Integer.toHexString(android.os.Build.FINGERPRINT.hashCode()));
        } catch (Exception ignored) {}
        request("auth/login", "POST", body, "", callback);
    }

    void get(String endpoint, String token, Callback callback) { request(endpoint, "GET", null, token, callback); }
    void logout(String token, Callback callback) { request("auth/logout", "POST", new JSONObject(), token, callback); }

    private void request(String endpoint, String method, JSONObject body, String token, Callback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(BASE + endpoint).openConnection();
                connection.setRequestMethod(method); connection.setConnectTimeout(15000); connection.setReadTimeout(25000);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                if (token != null && !token.isEmpty()) connection.setRequestProperty("Authorization", "Bearer " + token);
                if (body != null) {
                    connection.setDoOutput(true);
                    try (OutputStream out = connection.getOutputStream()) { out.write(body.toString().getBytes(StandardCharsets.UTF_8)); }
                }
                int status = connection.getResponseCode();
                InputStream stream = status >= 200 && status < 400 ? connection.getInputStream() : connection.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                StringBuilder text = new StringBuilder(); String line;
                while ((line = reader.readLine()) != null) text.append(line);
                JSONObject json = new JSONObject(text.toString());
                callback.complete(json, status >= 400 ? extractError(json) : null, status);
            } catch (Exception e) {
                callback.complete(null, "No fue posible conectar con Renacer. Revisa tu conexión.", 0);
            } finally { if (connection != null) connection.disconnect(); }
        });
    }

    private String extractError(JSONObject json) {
        String message = json.optString("message", "No fue posible completar la solicitud.");
        JSONObject data = json.optJSONObject("data");
        return data != null ? data.optString("message", message) : message;
    }
}
