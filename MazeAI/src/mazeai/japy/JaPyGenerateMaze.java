package mazeai.japy;

import mazenv.Maze;
import mazenv.Pair;

public class JaPyGenerateMaze {
    private JaPy jaPy;
    private int mazeSize;
    private int pathPercent;

    public JaPyGenerateMaze(String pythonScriptFilePath, int mazeSize, int pathPercent) {
        this.jaPy = new JaPy(pythonScriptFilePath, true);
        this.mazeSize = mazeSize;
        this.pathPercent = pathPercent;

        this.jaPy = new JaPy(pythonScriptFilePath, false);
    }

    public Maze generateMaze() {
        jaPy.createPythonProcess();
        Pair<String, Boolean> result = jaPy.runPythonScript(mazeSize + " " + pathPercent);
        if (result.getItem2()) {
            return new Maze(result.getItem1());
        } else {
            return null;
        }
    }    
}
