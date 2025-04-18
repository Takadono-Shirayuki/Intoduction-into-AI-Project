package mazenv;

import java.awt.Point;

public class MazeEnv {
    public class Action{
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
    }

    public class Buff{
        public static final int NONE = 0;
        public static final int SENRIGAN = 1;
        public static final int TOU_NO_HIKARI = 2;
        public static final int UNMEI_NO_MICHI = 3;
        public static final int SLIME_SAN_ONEGAI = 4;
    }

    public class Debuff{
        public static final int NONE = 0;
        public static final int WAAMU_HOURU = 1;
        public static final int SHIN_NO_MEIRO = 2;
    }

    private Maze maze;
    private DiscoveredMaze discoveredMaze;
    private boolean senriganBuff = false;
    private int slimeStep;
    public int maxStep;
    
    public MazeEnv(int mazeSize, int maxStep, int pathPercent, int slimeStep) {
        this.maze = new Maze(mazeSize, pathPercent);
        this.discoveredMaze = new DiscoveredMaze(mazeSize);
        this.maxStep = maxStep;
        this.slimeStep = slimeStep;
    }

    public void createDataSet(String path) {
        // Implementation here
    }

    public void exportMazeData() {
        // Implementation here
    }

    public MazeState reset() {
        maze.reset();
        return regenerateMaze(Buff.NONE);
    }

    public MazeState reset(int buff) {
        maze.reset();
        return regenerateMaze(buff, Debuff.NONE);
    }

    public MazeState regenerateMaze(int buff) {
        return regenerateMaze(buff, Debuff.NONE);
    }

    public MazeState regenerateMaze(int buff, int debuff) {
        // Kích hoạt buff 
        int pathToGoalAdder = 0;
        boolean deadMaze = false;
        switch (buff) {
            case Buff.SENRIGAN:
                senriganBuff = true;
                break;
            case Buff.UNMEI_NO_MICHI:
                pathToGoalAdder = 1;
            default:
                senriganBuff = false;
                break;
        }
        // Kích hoạt debuff
        switch (debuff) {
            case Debuff.WAAMU_HOURU:
                maze.activateWaamuHouruDebuff();
                break;
            case Debuff.SHIN_NO_MEIRO:
                deadMaze = true;
                break;
            default:
                break;
        }
        maze.generateMaze(pathToGoalAdder, deadMaze);
        discoveredMaze.reset();

        // Kích hoạt buff
        switch (buff) {
            case Buff.SLIME_SAN_ONEGAI:
                discoveredMaze.discoverMaze(maze.activateSlimeBuff(slimeStep), null);
                break;
            default:
                discoveredMaze.discoverMaze(maze.getDiscoverData(10), null);
                break;
        }
        int[][] mazeData = maze.getDiscoverData(senriganBuff ? 5 : 3);
        discoveredMaze.discoverMaze(mazeData, maze.getAgentPosition());
        
        return new MazeState(discoveredMaze.getLocalObs(maze.getAgentPosition()),
                discoveredMaze.getGlobalObs(),
                maze.getAgentPosition(),
                false);
    }

    public Pair<MazeState, Boolean> step(int action) {
        boolean takeAction = maze.step(action);
        Point agentPos = maze.getAgentPosition();
        int[][] mazeData = maze.getDiscoverData(senriganBuff ? 5 : 3);
        discoveredMaze.discoverMaze(mazeData, agentPos);
        boolean success = maze.isGoal(agentPos);
        MazeState state = new MazeState(discoveredMaze.getLocalObs(agentPos),
                discoveredMaze.getGlobalObs(),
                agentPos,
                success);
        return new Pair<MazeState, Boolean> (state, takeAction);
    }

    public void setMazeSize(int mazeSize) {
        maze = new Maze(mazeSize, maze.getPathPercent());
    }

    public void setPathPercent(int pathPercent) {
        maze.setPathPercent(pathPercent);
    }

    public boolean checkPositionValidity(Point position) {
        return maze.checkPositionValidity(position);
    }

    public int[][] getMaze() {
        return maze.getMaze();
    }

    public int[][] getDiscoveredMaze() {
        return discoveredMaze.getDiscoveredMaze(getMaze());
    }

    public double getTauCoefficient(double tauExponent) {
        return maze.getTauCoefficient(tauExponent);
    }

    public void render() {
        System.out.println(maze.getAgentPosition().x + " " + maze.getAgentPosition().y);
    }
}
