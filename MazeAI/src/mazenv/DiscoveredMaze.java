package mazenv;

public class DiscoveredMaze {
    private int[][] discoveredMaze;

    public DiscoveredMaze(int mazeSize) {
        this.discoveredMaze = new int[mazeSize][mazeSize];
        reset();
    }

    /**
     * Đặt lại trạng thái ban đầu cho DiscoveredMaze.
     */
    public void reset() {
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                discoveredMaze[i][j] = Maze.UNEXPLORED; // Mặc định là chưa khám phá
            }
        }
    }

    /**
     * Cập nhật trạng thái của ma trận đã khám phá dựa trên dữ liệu khám phá mới.
     * @param discoverData Dữ liệu khám phá mới.
     */
    public void discoverMaze(int[][] discoverData) {
        discoveredMaze = discoverData;
    }

    /**
     * Lấy ma trận đã khám phá.
     * Ý nghĩa giá trị của từng ô xem tại lớp Maze.
     * @return Ma trận đã khám phá
     */
    public int[][] getDiscoveredMaze() {
        int[][] returnMaze = new int[discoveredMaze.length][discoveredMaze.length];
        for (int i = 0; i < discoveredMaze.length; i++) {
            for (int j = 0; j < discoveredMaze[i].length; j++) {
                returnMaze[i][j] = discoveredMaze[i][j];
            }
        }
        return returnMaze;
    }
}
