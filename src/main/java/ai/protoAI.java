package ai;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


    // THIS CLASS IS A CONSTRUCTOR FOR SIMPLE AI FUNCTION AND DOES NOT NEED TO BE MODIFIED UNLESS BROKEN
    // Using gemma3:latest for the model, if it needs to change then modify all instances of 'gemma3:latest' to preferred model.
    //

/**
 * This is a base class for simple AI interaction.
 * It sends a prompt and then receives and converts a json response into usable text.
 */

public class protoAI {

    private final String modelName;
    private final String endpointUrl = "http://localhost:11434/api/generate";

    /**
     * Declares the default Ollama model as a parameter.
     */

    public protoAI() {
        this.modelName = "gemma3:latest"; // default model parameter
    }

    /**
     * Allows the choosing of a custom model in the case of needing images. This was out of scope and is
     * largely unused.
     * @param modelName Given string to declare a model not given by the default method.
     */

    public protoAI(String modelName) {
        this.modelName = modelName; // if custom model is to be used
    }

    /**
     * Sets up a HTTP connection with the local Ollama setup. Feeds inserted string and then converts
     * the received json response into usable text.
     * Do not be deceived by its name protoAI. This is the final product. I will not be redoing this.
     *
     * @param promptText The input given to the AI as a prompt.
     * @return A usable AI response in the form of a string.
     * @throws IOException Checks for the line and error type if Ollama fails to respond. Gives HTTP code for pinpointing errors.
     * 400 - Prompt too long/bad input, 500 - Ollama server issue/timeout, 404 - cannot find API. Worst case scenario.
     */

    public String proto(String promptText) throws IOException {

        // Set up the local URL and connection.
        URL url = new URL(endpointUrl);
        HttpURLConnection eConn = (HttpURLConnection) url.openConnection();
        eConn.setRequestMethod("POST");
        eConn.setRequestProperty("Content-Type", "application/json; utf-8");
        eConn.setRequestProperty("Accept", "application/json");
        eConn.setDoOutput(true);
        eConn.setConnectTimeout(25000); // 15 seconds until the initial connection times out.
        eConn.setReadTimeout(60000);   // 30 seconds until the AI's response times out.


        // JSON body, it works so don't touch it this is like a demon core
        JSONObject json = new JSONObject();
        json.put("model", modelName);
        json.put("prompt", promptText);
        json.put("stream", false);

        String jsonInputString = json.toString();


        // Write the JSON body for the response
        try (OutputStream os = eConn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get response from the AI
        int code = eConn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed: HTTP error code : " + code);
            // Will give error code if it eats shit.
            // Common ones include:
            // 500 - server error | 400 - prompt too long | 404 - Cry (Not detecting maybe because it wasn't running a model?)

        }

        // Read response from Ai
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

        // Close connection, this should keep every prompt independent of one another.
        eConn.disconnect();

        return responseText;  // Returns the raw response
    }
}
