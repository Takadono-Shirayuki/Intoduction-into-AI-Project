package mazenv;

import java.awt.Point;

public class DiscoveredMaze {
    private int[][] discoveredMaze;

    public DiscoveredMaze(int mazeSize) {
        this.discoveredMaze = new int[mazeSize][mazeSize];
    }

    public void reset() {
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                discoveredMaze[i][j] = 0;
            }
        }
    }

    public int[][] get(int x, int y) {
        return discoveredMaze;
    }

    public int[][] getLocalObs(int localObsSize, Point agentPosition) {
        return new int[localObsSize][localObsSize]; // mock data
    }

    public int[][] getGlobalObs() {
        return discoveredMaze;
    }

    public void discoverMaze(int[][] discoverData, Point agentPosition, Point agentRelativePosition) {
        // Update logic here
    }

    public int[][] getDiscoveredMaze() {
        return discoveredMaze;
    }
}
