package mazenv;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
        this.agentPosition = new Point(basePosition); // deep copy
        this.generateMaze(0, false);
    }

    public void generateMaze(int pathToGoalAdder, boolean deadMaze) {

        int numberOfPath;
        if (agentPosition.x >= mazeSize * 2 / 3 && agentPosition.y >= mazeSize * 2 / 3) {
            numberOfPath = 1 + pathToGoalAdder;
        } else {
            numberOfPath = 2 + pathToGoalAdder;
        }

        Random rand = new Random();
        double pathPercent = 70.0; // Giữ nguyên mặc định 70%

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

            if (debuff && goalPosition.equals(p)) {
                return false;
            }

            if (goalPosition.equals(p)) {
                d++;
                if (d == numberOfPath) {
                    return true;
                }
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

        return debuff; // Nếu không tìm được đường đi thì phụ thuộc vào debuff
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
        int x = agentPosition.x;
        int y = agentPosition.y;

        int newX = x;
        int newY = y;

        // Xác định hành động
        switch (action) {
            case 0:
                newX = x - 1;
                break; // Lên
            case 1:
                newX = x + 1;
                break; // Xuống
            case 2:
                newY = y - 1;
                break; // Trái
            case 3:
                newY = y + 1;
                break; // Phải
            default:
                break;
        }

        boolean done = false;

        // Kiểm tra hợp lệ và không phải tường trong discovered_maze
        if (validCheck(newX, newY) && discoveredMaze[newX + 5][newY + 5] != -5) {
            // Cập nhật vị trí agent
            agentPosition = new Point(newX, newY);
            discoverMaze();

            // Kiểm tra kết thúc
            if (agentPosition.equals(goalPosition)) {
                done = true;
            }

            // Giảm giá trị ô đã khám phá
            discoveredMaze[newX + 5][newY + 5] -= 1;
        }

        // Cập nhật quan sát
        exportData.put("local_obs", getObservation());
        exportData.put("global_obs", downsample(discoveredMaze));
        exportData.put("agent_position", agentPosition);

        return done;
    }

    public SimpleEntry<int[][], Point> getDiscoverData(int obsSize) {
        return null;
    }

    public SimpleEntry<int[][], Point> activateSlimeBuff(int step) {
        return null;
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
        return Math.pow(mazeSize, tauExponent);
    }
}
