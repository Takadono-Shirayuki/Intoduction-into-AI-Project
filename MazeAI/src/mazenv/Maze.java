package mazenv;

import java.awt.Point;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import mazenv.MazeEnv.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class Maze {
    public static final int WALL = -1;
    public static final int UNEXPLORED = 0;
    public static final int PATH = 1;
    public static final int GOAL = 2;
    public static final int AGENT_POSITION = 3;

    private int[][] maze;
    private Point agentPosition;
    private int mazeSize;
    private Point basePosition;
    private Point goalPosition;
    private int pathPercent;

    public Maze(int mazeSize, int pathPercent) {
        this.mazeSize = mazeSize;
        this.pathPercent = pathPercent;
        reset();
    }

    public Maze(String mazeString) {
        String[] rows = mazeString.split("\n");
        this.mazeSize = rows.length;
        this.maze = new int[mazeSize][mazeSize];
        for (int i = 0; i < mazeSize; i++) {
            String[] cols = rows[i].split(" ");
            for (int j = 0; j < mazeSize; j++) {
                maze[i][j] = Integer.parseInt(cols[j]);
            }
        }
        this.agentPosition = new Point(mazeSize / 6 - 1, mazeSize / 6 - 1);
        this.goalPosition = new Point(mazeSize * 5 / 6, mazeSize * 5 / 6);
    }
    /**
     * Đặt lại trạng thái ban đầu cho mê cung.
     */
    public void reset() {
        this.basePosition = new Point(this.mazeSize / 6 - 1, this.mazeSize / 6 - 1);
        this.agentPosition = new Point(basePosition);
        this.goalPosition = new Point(mazeSize * 5 / 6, mazeSize * 5 / 6);
    }

    /**
     * Tạo mê cung ngẫu nhiên 
     * @param pathToGoalAdder Tham số điều chỉnh số lượng đường đi đến đích.
     * @param deadMaze Nếu true, mê cung sẽ không có đường đi đến đích.
     */
    public void generateMaze(int pathToGoalAdder, boolean deadMaze) {
        int numberOfPath;
        if (agentPosition.x >= mazeSize * 2 / 3 && agentPosition.y >= mazeSize * 2 / 3) {
            numberOfPath = 1 + pathToGoalAdder;
        } else {
            numberOfPath = 2 + pathToGoalAdder;
        }

        Random rand = new Random();

        while (true) {
            int totalCells = mazeSize * mazeSize;
            int numPaths = (int) (totalCells * pathPercent / 100.0);
            int numWalls = totalCells - numPaths;

            // Tạo danh sách ngẫu nhiên các giá trị đường và tường 
            List<Integer> mazeValues = new ArrayList<>();
            for (int i = 0; i < numPaths; i++)
                mazeValues.add(PATH);
            for (int i = 0; i < numWalls; i++)
                mazeValues.add(WALL);
            Collections.shuffle(mazeValues, rand);

            // Điền dữ liệu vào ma trận mê cung
            maze = new int[mazeSize][mazeSize];
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    maze[i][j] = mazeValues.get(i * mazeSize + j);
                }
            }

            // Đặt điểm bắt đầu và đích
            maze[agentPosition.x][agentPosition.y] = PATH;
            maze[goalPosition.x][goalPosition.y] = GOAL;

            // Kiểm tra tính hợp lệ
            if (validateMaze(numberOfPath, deadMaze)) {
                break;
            }
        }
    }

    /**
     * Kiểm tra tính hợp lệ của mê cung.
     * @param numberOfPath Số lượng đường đi đến đích.
     * @param deadMaze Nếu true, mê cung sẽ không có đường đi đến đích.
     * @return true nếu mê cung hợp lệ, false nếu không hợp lệ.
     */
    private boolean validateMaze(int numberOfPath, boolean deadMaze) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(agentPosition));
        Set<Point> visited = new HashSet<>();
        int d = 0;

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int x = p.x;
            int y = p.y;

            if (deadMaze && goalPosition.equals(p)) {
                return false;
            }

            if (goalPosition.equals(p)) {
                d++;
                if (d == numberOfPath) {
                    return true;
                }
                continue;
            }

            if (visited.contains(p)) {
                continue;
            }
            visited.add(p);

            // Lấy danh sách hàng xóm
            List<Point> neighbors = getNeighbors(x, y);
            for (Point neighbor : neighbors) {
                int nx = neighbor.x;
                int ny = neighbor.y;
                if (maze[nx][ny] != WALL && !visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return deadMaze; // Nếu không tìm được đường đi thì phụ thuộc vào deadMaze
    }

    /**
     * Lấy danh sách các điểm lân cận của một điểm trong mê cung.
     * * @param x Tọa độ x của điểm.
     * * @param y Tọa độ y của điểm.
     * * @return Danh sách các điểm lân cận.
     */
    private List<Point> getNeighbors(int x, int y) {
        List<Point> neighbors = new ArrayList<>();

        // Bốn hướng: lên, xuống, trái, phải
        int[][] directions = {
                { -1, 0 }, // lên
                { 1, 0 }, // xuống
                { 0, -1 }, // trái
                { 0, 1 } // phải
        };

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            // Kiểm tra nằm trong phạm vi mê cung
            if (nx >= 0 && ny >= 0 && nx < mazeSize && ny < mazeSize) {
                neighbors.add(new Point(nx, ny));
            }
        }

        return neighbors;
    }

    /**
     * Thực hiện hành động của tác tử trong mê cung. <p>
     * Các hành động được định nghĩa trong lớp Action.
     * @param action Hành động của tác tử.
     * @return true nếu hành động hợp lệ, false nếu không hợp lệ.
     * @note: Nếu hành động không hợp lệ, tác tử sẽ không di chuyển.
     */
    public boolean step(int action) {
        int newX = agentPosition.x;
        int newY = agentPosition.y;

        // Xác định hành động
        switch (action) {
            case Action.UP:
                newX --;
                break; // Lên
            case Action.DOWN:
                newX ++;
                break; // Xuống
            case Action.LEFT:
                newY --;
                break; // Trái
            case Action.RIGHT:
                newY ++;
                break; // Phải
            default:
                break;
        }

        boolean takeAction = false;
        // Kiểm tra hợp lệ và không phải tường trong discovered_maze
        if (newX >= 0 && newY >= 0 && newX < mazeSize && newY < mazeSize) {
            if (maze[newX][newY] != WALL) {
                agentPosition.setLocation(newX, newY);
                takeAction = true;
            }
        }
        return takeAction;
    }

    /** 
     * Lấy dữ liệu khám phá của mê cung.
     * @param obsSize Kích thước của ô quan sát.
     * @return Ma trận chứa dữ liệu khám phá.
     */
    public int[][] getDiscoverData(int obsSize) {
        int[][] discoverData = new int[mazeSize][mazeSize];
        for (int i = 0; i < discoverData.length; i++) {
            for (int j = 0; j < discoverData[i].length; j++) {
                discoverData[i][j] = UNEXPLORED; // Mặc định là chưa khám phá
            }
        }
        int startX = agentPosition.x - obsSize;
        int startY = agentPosition.y - obsSize;
        int endX = agentPosition.x + obsSize + 1;
        int endY = agentPosition.y + obsSize + 1;
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                if (i >= 0 && j >= 0 && i < mazeSize && j < mazeSize) {
                    discoverData[i][j] = maze[i][j]; // Đánh dấu đã khám phá
                }
            }
        }
        discoverData[goalPosition.x][goalPosition.y] = GOAL; // Đánh dấu vị trí đích
        return discoverData;
    }

    /**
     * Kích hoạt buff slime-san onegai trong mê cung.
     * @param step Số bước di chuyển của slime.
     * @return Ma trận chứa dữ liệu khám phá mê cung.
     */
    public int[][] activateSlimeBuff(int step) {
        // Tạo ma trận đầu ra
        int[][] slimeBuff = new int[mazeSize][mazeSize];
        for (int i = 0; i < slimeBuff.length; i++) {
            for (int j = 0; j < slimeBuff[i].length; j++) {
                slimeBuff[i][j] = UNEXPLORED; // Mặc định là chưa khám phá
            }
        }

        // Tập các đỉnh đã duyệt
        Set<Point> visited = new HashSet<>();

        // Đếm số lượng bước
        int stepCount = 0;

        // Hàng đợi (FIFO) để quản lý các đỉnh
        Queue<Point> queue = new LinkedList<>();
        queue.add(agentPosition);

        // Bắt đầu duyệt đồ thị
        while (!queue.isEmpty()) {
            // Lấy một đỉnh từ hàng đợi
            Point current = queue.poll();
            int x = current.x;
            int y = current.y;

            // Kiểm tra nếu đỉnh chưa được duyệt
            if (!visited.contains(current)) {
                visited.add(current);
                stepCount++;

                slimeBuff[x][y] = maze[x][y]; // Đánh dấu đã khám phá

                if (stepCount >= step) {
                    break; // Dừng lại nếu đã đủ số bước
                }

                // Lấy danh sách các điểm lân cận
                List<Point> neighbors = getNeighbors(x, y);
                for (Point neighbor : neighbors) {
                    int nx = neighbor.x;
                    int ny = neighbor.y;

                    // Chỉ đi qua các đường hợp lệ
                    if (maze[nx][ny] != WALL && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return slimeBuff;
    }

    /**
     * Kích hoạt debuff waamu houru trong mê cung.
     */
    public void activateWaamuHouruDebuff()
    {
        // Đặt vị trí của agent về vị trí ngẫu nhiên trong mê cung
        Random rand = new Random();
        while (true) {
            int newX = mazeSize / 3 + rand.nextInt(mazeSize * 2 / 3);
            int newY = mazeSize / 3 + rand.nextInt(mazeSize * 2 / 3);
            if (newX != goalPosition.x && newY != goalPosition.y) {
                agentPosition.setLocation(newX, newY);
                break;
            }
        }
    }

    /**
     * Lấy ma trận mê cung hiện tại. <p>
     * Ý nghĩa giá trị của từng ô xem tại lớp Maze.
     * @note: Tác tử sẽ được đánh dấu tại vị trí của nó trong ma trận.
     * @return Mê cung hiện tại.
     */
    public int[][] getMaze() {
        int[][] returnMaze = new int[mazeSize][mazeSize];
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                returnMaze[i][j] = maze[i][j]; // Sao chép mê cung
            }
        }
        returnMaze[agentPosition.x][agentPosition.y] = AGENT_POSITION; // Đánh dấu vị trí của tác tử
        return returnMaze;      
    }

    /**
     * Lấy vị trí của tác tử trong mê cung.
     * @return Tọa độ của tác tử.
     */
    public Point getAgentPosition() {
        return agentPosition;
    }

    /**
     * Lấy vị trí của điểm đích trong mê cung.
     * @return Tọa độ của điểm đích.
     */
    public Point getGoalPosition() {
        return goalPosition;
    }

    /**
     * Đặt mật độ đường đi trong mê cung.
     * @param pathPercent Tỷ lệ đường đi trong mê cung (0-100).
     */
    public void setPathPercent(int pathPercent) {
        if (pathPercent > 0 && pathPercent <= 100) {
            this.pathPercent = pathPercent;
        }
    }

    /**
     * Lấy tỷ lệ đường đi trong mê cung.
     * @return Tỷ lệ đường đi trong mê cung (0-100).
     */
    public int getPathPercent() {
        return pathPercent;
    }

    /**
     * Kiểm tra xem tác tử đã đến đích hay chưa.
     * @return true nếu tác tử đã đến đích, false nếu chưa.
     */
    public boolean isGoal() {
        return goalPosition.equals(agentPosition);
    }

    /**
     * Kiểm tra tính hợp lệ của vị trí trong mê cung. <p>
     * Vị trí hợp lệ là vị trí không nằm trong tường và nằm trong kích thước của mê cung.
     * @param position Vị trí cần kiểm tra.
     * @return true nếu vị trí hợp lệ, false nếu không hợp lệ.
     */
    public boolean checkPositionValidity(Point position) {
        return position.x >= 0 && position.y >= 0 && position.x < mazeSize && position.y < mazeSize && maze[position.x][position.y] != -1;
    }
}
