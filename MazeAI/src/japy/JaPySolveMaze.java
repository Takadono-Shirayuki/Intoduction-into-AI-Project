package japy;

import java.io.IOException;

import mazenv.Maze;
import mazenv.Pair;

public class JaPySolveMaze {
    private static JaPy jaPy;

    public static void Initialize(String pythonScriptFilePath) {
        jaPy = new JaPy(pythonScriptFilePath, true);
    }

    public static String generateMazeInput(int[][] discovered, int posX, int posY) {
        StringBuilder sb = new StringBuilder();
        sb.append(posX).append(" ").append(posY).append("\n\n");
        for (int[] row : discovered) {
            for (int val : row) {
                sb.append(val).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String generateMazeAndSkillsInput(int[][] discovered, int posX, int posY, int[] skillIds) {
        StringBuilder sb = new StringBuilder(generateMazeInput(discovered, posX, posY));
        for (int id : skillIds) {
            sb.append(id).append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }


    public int requestNextStep(int[][] discovered, int posX, int posY) {
        jaPy.createPythonProcess();
        String input = generateMazeInput(discovered, posX, posY);
        Pair<String, Boolean> result = jaPy.runPythonScript(input);
        
        // process here

        return 1;
    }

    public int requestNextSkill(int[][] discovered, int posX, int posY, int[] skillIds) {
        jaPy.createPythonProcess();
        String input = generateMazeAndSkillsInput(discovered, posX, posY, skillIds);
        Pair<String, Boolean> result = jaPy.runPythonScript(input);
        
        // process here

        return 1;
    }
    
}
