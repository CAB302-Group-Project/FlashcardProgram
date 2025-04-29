package ai;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class protoAI {

    private String modelName;
    private final String endpointUrl = "http://localhost:11434/api/generate";

    public protoAI() {
        this.modelName = "gemma3:latest"; // default model
    }

    public protoAI(String modelName) {
        this.modelName = modelName; // custom model
    }

    public String proto(String promptText) throws IOException {
        // Set up the URL and connection
        URL url = new URL(endpointUrl);
        HttpURLConnection eConn = (HttpURLConnection) url.openConnection();
        eConn.setRequestMethod("POST");
        eConn.setRequestProperty("Content-Type", "application/json; utf-8");
        eConn.setRequestProperty("Accept", "application/json");
        eConn.setDoOutput(true);
        eConn.setConnectTimeout(5000); // 5 seconds
        eConn.setReadTimeout(30000);   // 30 seconds

        // JSON body
        String jsonInputString = String.format(
                "{\"model\": \"%s\", \"prompt\":\"%s\", \"stream\": false}", modelName, promptText
        );

        // Write the JSON body
        try (OutputStream os = eConn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get response
        int code = eConn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed: HTTP error code : " + code);
        }

        // Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(eConn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Parse JSON
        JSONObject jsonResponse = new JSONObject(response.toString());
        String responseText = jsonResponse.getString("response");

        // Close connection
        eConn.disconnect();

        return responseText;
    }
}
