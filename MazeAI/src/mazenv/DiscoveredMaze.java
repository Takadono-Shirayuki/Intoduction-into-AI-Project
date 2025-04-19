package mazenv;

import java.awt.Point;

public class DiscoveredMaze {
    private int[][] discoveredMaze;

    public DiscoveredMaze(int mazeSize) {
        this.discoveredMaze = new int[mazeSize + 10][mazeSize + 10];
        reset();
    }

    /**
     * Đặt lại trạng thái ban đầu cho DiscoveredMaze.
     */
    public void reset() {
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                if (i < 5 || j < 5 || i >= discoveredMaze.length - 5 || j >= discoveredMaze[i].length - 5) {
                    discoveredMaze[i][j] = 5 * Maze.WALL; // Điền tường ở xung quanh
                } else {
                    discoveredMaze[i][j] = Maze.UNEXPLORED; // Mặc định là chưa khám phá
                }
            }
        }
    }

    /**
     * Lấy quan sát cục bộ của tác tử.
     * Quan sát cục bộ là một ma trận 11x11, trong đó ô ở giữa là vị trí của tác tử.
     * @param agentPosition Tọa độ của tác tử trong ma trận.
     * @return Ma trận 11x11 chứa giá trị của các ô xung quanh tác tử.
     */
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

    /**
     * Lấy quan sát toàn cục của ma trận đã khám phá.
     * Quan sát toàn cục là ma trận được downsample từ ma trận đã khám phá.
     * @note: Ma trận được downsample với kích thước 5x5 dùng hàm tính tổng để tính giá trị.
     * @return Ma trận chứa giá trị của các ô đã khám phá.
     */
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

    /**
     * Cập nhật trạng thái của ma trận đã khám phá dựa trên dữ liệu khám phá mới.
     * Giảm giá trị của ô hiện tại thể hiện số lần đã khám phá.
     * Nếu agentsPosition là null, chỉ cập nhật ma trận mà không giảm giá trị ô hiện tại.
     * @param discoverData Dữ liệu khám phá mới.
     * @param agentPosition Tọa độ của tác tử trong ma trận.
     */
    public void discoverMaze(int[][] discoverData, Point agentPosition) {
        for (int i = 0; i < discoverData.length; i++) {
            for (int j = 0; j < discoverData[i].length; j++) {
                if (discoveredMaze[i + 5][j + 5] == Maze.UNEXPLORED) { // Chỉ đánh dấu những ô chưa khám phá
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

    /**
     * Lấy ma trận đã khám phá.
     * Ý nghĩa giá trị của từng ô xem tại lớp Maze.
     * @param maze Ma trận gốc để so sánh với ma trận đã khám phá.
     * @return Ma trận đã khám phá
     */
    public int[][] getDiscoveredMaze(int[][] maze) {
        int[][] returnMaze = new int[discoveredMaze.length - 10][discoveredMaze.length - 10];
        for (int i = 0; i < returnMaze.length; i++) {
            for (int j = 0; j < returnMaze[i].length; j++) {
                if (discoveredMaze[i + 5][j + 5] == 1)
                    returnMaze[i][j] = Maze.UNEXPLORED; // Chưa khám phá
                else    
                    returnMaze[i][j] = maze[i][j]; // Đã khám phá
            }
        }
        return returnMaze;
    }
}
