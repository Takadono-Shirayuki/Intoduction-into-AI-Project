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
    private int[][] maze;
    private Point agentPosition;
    private int mazeSize;
    private Point basePosition;
    private Point goalPosition;
    private int pathPercent;

    public Maze(int mazeSize, int pathPercent) {
        this.mazeSize = mazeSize;
        this.pathPercent = pathPercent;
    }

    public void reset() {
        this.basePosition = new Point(this.mazeSize / 6 - 1, this.mazeSize / 6 - 1);
        this.agentPosition = new Point(basePosition);
        this.goalPosition = new Point(mazeSize * 5 / 6, mazeSize * 5 / 6);
    }

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

            // Tạo danh sách ngẫu nhiên các giá trị đường (0) và tường (-1)
            List<Integer> mazeValues = new ArrayList<>();
            for (int i = 0; i < numPaths; i++)
                mazeValues.add(0);
            for (int i = 0; i < numWalls; i++)
                mazeValues.add(-1);
            Collections.shuffle(mazeValues, rand);

            // Điền dữ liệu vào ma trận mê cung
            maze = new int[mazeSize][mazeSize];
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    maze[i][j] = mazeValues.get(i * mazeSize + j);
                }
            }

            // Đặt điểm bắt đầu và đích
            maze[agentPosition.x][agentPosition.y] = 0;
            maze[goalPosition.x][goalPosition.y] = 2;

            // Kiểm tra tính hợp lệ
            if (validateMaze(numberOfPath, deadMaze)) {
                break;
            }
        }
    }

    public boolean validateMaze(int numberOfPath, boolean deadMaze) {
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
                if (maze[nx][ny] > -1 && !visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return deadMaze; // Nếu không tìm được đường đi thì phụ thuộc vào debuff
    }

    public List<Point> getNeighbors(int x, int y) {
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
            if (maze[newX][newY] != -1) {
                agentPosition.setLocation(newX, newY);
                takeAction = true;
            }
        }
        return takeAction;
    }

    public int[][] getDiscoverData(int obsSize) {
        int[][] discoverData = new int[mazeSize][mazeSize];
        for (int i = 0; i < discoverData.length; i++) {
            for (int j = 0; j < discoverData[i].length; j++) {
                discoverData[i][j] = 1; // Mặc định là chưa khám phá (1)
            }
        }
        int startX = agentPosition.x - obsSize;
        int startY = agentPosition.y - obsSize;
        int endX = agentPosition.x + obsSize + 1;
        int endY = agentPosition.y + obsSize + 1;
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                if (i >= 0 && j >= 0 && i < mazeSize && j < mazeSize) {
                    discoverData[i][j] = 5 * maze[i][j]; // Đánh dấu đã khám phá
                }
            }
        }
        return discoverData;
    }

    public int[][] activateSlimeBuff(int step) {
        // Tạo ma trận đầu ra
        int[][] slimeBuff = new int[mazeSize][mazeSize];
        for (int i = 0; i < slimeBuff.length; i++) {
            for (int j = 0; j < slimeBuff[i].length; j++) {
                slimeBuff[i][j] = 1; // Mặc định là chưa khám phá
            }
        }

        // Tập các đỉnh đã duyệt
        Set<Point> visited = new HashSet<>();
        List<Point> visitedOrder = new ArrayList<>();

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
                visitedOrder.add(current);

                slimeBuff[x][y] = 0; // Đánh dấu đã khám phá

                if (visitedOrder.size() >= step) 
                    break; // Dừng lại nếu đã đủ số bước

                // Lấy danh sách các điểm lân cận
                List<Point> neighbors = getNeighbors(x, y);
                for (Point neighbor : neighbors) {
                    int nx = neighbor.x;
                    int ny = neighbor.y;

                    // Chỉ đi qua các đường hợp lệ
                    if (maze[nx][ny] > -1 && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return slimeBuff;
    }

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

    public int[][] getMaze() {
        return maze;
    }

    public Point getAgentPosition() {
        return agentPosition;
    }

    public void setPathPercent(int pathPercent) {
        this.pathPercent = pathPercent;
    }

    public int getPathPercent() {
        return pathPercent;
    }

    public double getTauCoefficient(double tauExponent) {
        int distance = goalPosition.x - agentPosition.x + goalPosition.y - agentPosition.y;
        return Math.pow(distance / (4 / 3.0 * mazeSize), tauExponent);
    }

    public boolean isGoal(Point agentPosition) {
        return goalPosition.equals(agentPosition);
    }

    public boolean checkPositionValidity(Point position) {
        return position.x >= 0 && position.y >= 0 && position.x < mazeSize && position.y < mazeSize && maze[position.x][position.y] != -1;
    }
}
