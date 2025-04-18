package mazenv;

import java.awt.Point;

public class DiscoveredMaze {
    private int[][] discoveredMaze;

    public DiscoveredMaze(int mazeSize) {
        this.discoveredMaze = new int[mazeSize + 10][mazeSize + 10];
        reset();
    }

    public void reset() {
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                if (i < 5 || j < 5 || i >= discoveredMaze.length - 5 || j >= discoveredMaze[i].length - 5) {
                    discoveredMaze[i][j] = -5; // Điền tường ở xung quanh
                } else {
                    discoveredMaze[i][j] = 1; // Mặc định là chưa khám phá
                }
            }
        }
    }

    public int get(int x, int y) {
        return discoveredMaze[x][y];
    }

    public int[][] getLocalObs(Point agentPosition) {
        int[][] localOBS = new int[11][11];
        int startX = agentPosition.x;
        int startY = agentPosition.y;
        int endX = agentPosition.x + 11;
        int endY = agentPosition.y + 11;
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                localOBS[i - startX][j - startY] = discoveredMaze[i][j];
            }
        }
        return localOBS;
    }

    public int[][] getGlobalObs() {
        int globalObsSize = (int)Math.ceil(discoveredMaze.length / 5.0);
        int[][] globalOBS = new int[globalObsSize][globalObsSize];
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                globalOBS[i / 5][j / 5] += discoveredMaze[i][j];
            }
        }
        return globalOBS;
    }

    public void discoverMaze(int[][] discoverData, Point agentPosition) {
        for (int i = 0; i < discoverData.length; i++) {
            for (int j = 0; j < discoverData[i].length; j++) {
                if (discoveredMaze[i + 5][j + 5] == 1) { // Chỉ đánh dấu những ô chưa khám phá
                    discoveredMaze[i + 5][j + 5] = discoverData[i][j]; // Đánh dấu đã khám phá
                }
            }
        }

        if (agentPosition == null) 
              return;

        if (discoveredMaze[agentPosition.x][agentPosition.y] > -5) {
            discoveredMaze[agentPosition.x][agentPosition.y] -= 1; // Giảm giá trị ô hiện tại
        }
    }

    public int[][] getDiscoveredMaze(int[][] maze) {
        int[][] returnMaze = new int[discoveredMaze.length - 10][discoveredMaze.length - 10];
        for (int i = 0; i < returnMaze.length; i++) {
            for (int j = 0; j < returnMaze[i].length; j++) {
                if (discoveredMaze[i + 5][j + 5] == 1)
                    returnMaze[i][j] = 1; // Chưa khám phá
                else    
                    returnMaze[i][j] = maze[i][j]; // Đã khám phá
            }
        }
        return returnMaze;
    }
}
