package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import mazenv.MazeEnv.Buff;

public class GameVariable {
    public static class GameVariableType {
        public static final String PLAYER_NAME = "Player Name";
        public static final String NPC_NAME = "NPC Name";
    }
    public static final String SAVED_GAME_PATH = "saved_env.dat";  // Đường dẫn đến tệp lưu game
    public static final String SPOOKY_IMAGE_PATH = "/mazeai/Icon/SpookyButton.jpg";  // Đường dẫn đến ảnh Spooky
    private static String VARIABLE_DOC = "src/mazeai/SavedGame/VariableDocument.txt";
    private static String PlayerName;
    private static String NpcName;

    public static void loadGameVariable() {
        // Đọc biến từ file
        try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Buff.class.getResourceAsStream(VARIABLE_DOC), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String variableName = parts[0].trim();
                    String variableValue = parts[1].trim();
                    setVariable(variableName, variableValue);
                }
            }
        } catch (Exception e) {
            // Đặt giá trị mặc định nếu không thể đọc file
            PlayerName = "Player";
            NpcName = "Saku";
            // In ra thông báo lỗi
            System.err.println("Error loading game variables: " + e.getMessage());
        }
    }

    private static void saveGameVariable() {
        // Ghi biến vào file
        try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(VARIABLE_DOC), StandardCharsets.UTF_8))) {
            writer.write(GameVariableType.PLAYER_NAME + "=" + PlayerName);
            writer.newLine();
            writer.write(GameVariableType.NPC_NAME + "=" + NpcName);
            writer.newLine();
        } catch (Exception e) {
            // In ra thông báo lỗi
            System.err.println("Error saving game variables: " + e.getMessage());
        }
    }

    public static String format(String str)
    {
        String returnStr = str;
        returnStr = returnStr.replaceAll("\\{" + GameVariableType.PLAYER_NAME +"\\}", PlayerName);
        returnStr = returnStr.replaceAll("\\{" + GameVariableType.NPC_NAME +"\\}", NpcName);
        return returnStr;
    }

    public static <T> void setVariable(String variableName, T variableValue)
    {
        switch (variableName) {
            case GameVariableType.PLAYER_NAME:
                PlayerName = (String)variableValue;
                break;
            case GameVariableType.NPC_NAME:
                NpcName = (String)variableValue;
                break;
            default:
                break;
        }
        saveGameVariable();
    }
}
