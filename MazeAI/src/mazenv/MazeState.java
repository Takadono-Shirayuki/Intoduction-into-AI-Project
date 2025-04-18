package mazenv;

import java.awt.Point;

public class MazeState {
    public int[][] localObs;
    public int[][] globalObs;
    public Point agentPosition;

    public MazeState(int[][] localObs, int[][] globalObs, Point agentPosition) {
        this.localObs = localObs;
        this.globalObs = globalObs;
        this.agentPosition = agentPosition;
    }
}
