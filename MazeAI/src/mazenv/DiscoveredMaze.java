package mazenv;

import java.awt.Point;
import java.io.Serializable;

public class DiscoveredMaze implements Serializable{
    private int[][] discoveredMaze;
    private Boolean hellMode;

    public DiscoveredMaze(int mazeSize, int[][] discoverData, Boolean hellMode) {
        this.hellMode = hellMode;
        this.discoveredMaze = new int[mazeSize][mazeSize];
        discoverMaze(discoverData);
    }

    /** 
     * Đặt lại trạng thái của ma trận đã khám phá về trạng thái ban đầu.
     */
    public void reset(){
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                discoveredMaze[i][j] = Maze.UNEXPLORED;
            }
        }
    }
    /**
     * Cập nhật trạng thái của ma trận đã khám phá dựa trên dữ liệu khám phá mới.
     * @param discoverData Dữ liệu khám phá mới.
     */
    public void discoverMaze(int[][] discoverData) {
        if (hellMode)
        {
            discoveredMaze = discoverData;
            return;
        }
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                if (discoverData[i][j] != Maze.UNEXPLORED) {
                    discoveredMaze[i][j] = discoverData[i][j];
                }
            }
        }
    }

    /**
     * Lấy ma trận đã khám phá.
     * Ý nghĩa giá trị của từng ô xem tại lớp Maze.
     * @return Ma trận đã khám phá
     */
    public int[][] getDiscoveredMaze(Point agentPos) {
        int[][] returnMaze = new int[discoveredMaze.length][discoveredMaze.length];
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                returnMaze[i][j] = discoveredMaze[i][j];
            }
        }
        returnMaze[agentPos.x][agentPos.y] = Maze.AGENT_POSITION; // Đánh dấu vị trí của agent
        return returnMaze;
    }
}
