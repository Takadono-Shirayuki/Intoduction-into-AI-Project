package mazenv;

import java.awt.Point;
import java.util.AbstractMap.SimpleEntry;

public class MazeEnv {
    private Maze maze;
    private DiscoveredMaze discoveredMaze;
    private boolean buff;
    private boolean debuff;
    private int maxStep;

    public MazeEnv(int mazeSize, int maxStep, int pathPercent) {
        this.maze = new Maze(mazeSize, pathPercent);
        this.discoveredMaze = new DiscoveredMaze(mazeSize);
        this.maxStep = maxStep;
    }

    public void createDataSet(String path) {
        // Implementation here
    }

    public void exportMazeData() {
        // Implementation here
    }

    public void reset() {
        maze.reset();
        discoveredMaze.reset();
    }

    public MazeState regenerateMaze(int pathToGoalAdder) {
        maze.generateMaze(pathToGoalAdder, false);
        return new MazeState(discoveredMaze.getLocalObs(5, maze.getAgentPosition()),
                discoveredMaze.getGlobalObs(),
                maze.getAgentPosition());
    }

    public SimpleEntry<MazeState, Boolean> step(int action) {
        boolean success = maze.step(action);
        Point agentPos = maze.getAgentPosition();
        discoveredMaze.discoverMaze(discoveredMaze.getDiscoveredMaze(), agentPos, agentPos);
        MazeState state = new MazeState(discoveredMaze.getLocalObs(5, agentPos),
                discoveredMaze.getGlobalObs(),
                agentPos);
        return new SimpleEntry<>(state, success);
    }

    public void activateBuff(String buff) {
        this.buff = true;
    }

    public void activateDebuff(String debuff) {
        this.debuff = true;
    }

    public void setMazeSize(int mazeSize) {
        maze = new Maze(mazeSize, maze.getPathPercent());
    }

    public void setPathPercent(int pathPercent) {
        maze.setPathPercent(pathPercent);
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public boolean checkPositionValidity(Point position) {
        return position.x >= 0 && position.y >= 0;
    }

    public int[][] getMaze() {
        return maze.getMaze();
    }

    public int[][] getDiscoveredMaze() {
        return discoveredMaze.getDiscoveredMaze();
    }

    public double getTauCoefficient(double tauExponent) {
        return maze.getTauCoefficient(tauExponent);
    }
}
