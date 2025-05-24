package japy;

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
    public Maze regenerateMaze(int posX, int posY, int goalX, int goalY) {
        jaPy.createPythonProcess();
        StringBuilder sb = new StringBuilder();
        sb.append(posX).append(" ").append(posY).append("\n");
        sb.append(goalX).append(" ").append(goalY).append("\n");
        Pair<String, Boolean> result = jaPy.runPythonScript(sb.toString());

        if (result.getItem2()) {
            return new Maze(result.getItem1());  // Chuyển chuỗi trả về thành Maze
        } else {
            System.err.println("Không tạo được maze mới từ Python");
            return null;
        }
    }
}
