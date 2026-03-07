package gestions.ferme.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ProfanityAPI {
    
    // API PurgoMalum - une API gratuite pour la détection de gros mots
    private static final String API_URL = "https://www.purgomalum.com/service/json?text=";
    
    /**
     * Vérifie si le texte contient des gros mots en utilisant l'API PurgoMalum
     * @param text Le texte à vérifier
     * @return true si le texte contient des gros mots, false sinon
     */
    public static boolean containsProfanity(String text) {
        try {
            String encodedText = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            URL url = new URL(API_URL + encodedText);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                System.err.println("Erreur API: " + conn.getResponseCode());
                return false;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            String result = jsonResponse.getString("result");
            
            // Si le texte original et le résultat sont différents, cela signifie que des gros mots ont été détectés
            return !text.equals(result);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Alternative: API Content Moderation
     * Cette méthode utilise une autre API si PurgoMalum ne convient pas
     */
    public static boolean checkWithContentModerationAPI(String text) {
        try {
            // Exemple avec l'API de modération de contenu de Sightengine
            URL url = new URL("https://api.sightengine.com/1.0/text/check.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            
            // Remplacez ces valeurs par vos propres clés API
            String apiUser = "YOUR_API_USER";
            String apiSecret = "YOUR_API_SECRET";
            
            String postData = "text=" + java.net.URLEncoder.encode(text, StandardCharsets.UTF_8.toString()) +
                    "&mode=standard&lang=fr&api_user=" + apiUser + "&api_secret=" + apiSecret;
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            if (conn.getResponseCode() != 200) {
                System.err.println("Erreur API: " + conn.getResponseCode());
                return false;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject profanity = jsonResponse.getJSONObject("profanity");
            
            // Vérifier si des gros mots ont été détectés
            return profanity.getDouble("matches") > 0;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}