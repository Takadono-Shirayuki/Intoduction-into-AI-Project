package mazenv;

import java.awt.Point;
import java.io.Serializable;

public class MazeState implements Serializable{
    public int[][] discoveredMaze;
    public Point agentPosition;
    public Point goalPosition;
    public boolean success;

    /**
     * Lớp MazeState dùng để lưu trữ trạng thái của tác tử trong mê cung.<p>
     * @param localObs Quan sát cục bộ của tác tử.
     * @param globalObs Quan sát toàn cục của mê cung.
     * @param agentPosition Vị trí của tác tử trong mê cung.
     * @param success Trạng thái đã đến đích hay chưa.
     */
    public MazeState(int[][] discoveredMaze, Point agentPosition, Point goalPosition, boolean success) {
        this.discoveredMaze = discoveredMaze;
        this.agentPosition = agentPosition;
        this.goalPosition = goalPosition;
        this.success = success;
    }
}
