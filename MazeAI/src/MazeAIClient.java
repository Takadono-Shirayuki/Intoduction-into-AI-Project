import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MazeAIClient {
    private static final String SERVER_URL = "http://localhost:5000/next-move";

    public static String getNextMove(int[][] maze, int playerX, int playerY) {
        try {
            // Build the JSON request
            String jsonInputString = "{ \"maze\": " + arrayToJson(maze) + ", \"position\": [" + playerX + "," + playerY + "] }";

            // Open connection to the Python server
            URL url = new URL(SERVER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Send the JSON request to the server
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response from the server
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString(); // Return the response (next move)
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to convert 2D array to JSON format
    private static String arrayToJson(int[][] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append("[");
            for (int j = 0; j < array[i].length; j++) {
                sb.append(array[i][j]);
                if (j < array[i].length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if (i < array.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
