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

    private static String modelName;
    private String endpointUrl = "http://localhost:11434/api/generate";

    public protoAI() {
        this.modelName = "gemma3:latest"; // default constructor, takes just gemma3
    }

    public protoAI(String modelName) {
        this.modelName = modelName; // Setting custom model if you want ig
    }


    public static String proto(String promptText) throws IOException {


        // Set up the URL and connection
        URL url = new URL("http://localhost:11434/api/generate");
        HttpURLConnection eConn = (HttpURLConnection) url.openConnection();
        eConn.setRequestMethod("POST");
        eConn.setRequestProperty("Content-Type", "application/json; utf-8");
        eConn.setRequestProperty("Accept", "application/json");
        eConn.setDoOutput(true);
        eConn.setConnectTimeout(5000); // 5 seconds to connect
        eConn.setReadTimeout(10000);   // 10 seconds to read response


        //JSON body using variables
        String jsonInputString = String.format(
                "{\"model\": \"%s\", \"prompt\":\"%s\", \"stream\": false}", modelName, promptText
        );

        // Write the JSon body to the request
        try (OutputStream os = eConn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code (optional)
        int code = eConn.getResponseCode();
        //System.out.println("Response Code: " + code);

        //Read the response body
        BufferedReader in = new BufferedReader(new InputStreamReader(eConn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        //Comments are just debugging stuff

        //System.out.println("Response Body: " + response.toString());

        //parse the json response and print the response field
        JSONObject jsonResponse = new JSONObject(response.toString());
        String responseText = jsonResponse.getString("response");
        //System.out.println("Response: " + responseText);

        // Close connection
        eConn.disconnect();

        return responseText;
    }



}
