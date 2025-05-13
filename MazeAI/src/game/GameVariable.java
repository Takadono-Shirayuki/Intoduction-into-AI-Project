package game;

public class GameVariable {
    public static class GameVariableType {
        public static final String PlayerName = "Player Name";
    }

    private static String PlayerName = "Test";

    public static void loadGameVariable() {
        
    }

    public static String format(String str)
    {
        String returnStr = str;
        returnStr = returnStr.replaceAll("\\{" + GameVariableType.PlayerName +"\\}", PlayerName);
        return returnStr;
    }

    public static <T> void setVariable(String variableName, T variableValue)
    {
        switch (variableName) {
            case GameVariableType.PlayerName:
                PlayerName = (String)variableValue;
                break;
            default:
                break;
        }
    }
}
