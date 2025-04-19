package mazenv;

import java.awt.Point;

public class MazeState {
    public int[][] localObs;
    public int[][] globalObs;
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
    public MazeState(int[][] localObs, int[][] globalObs, Point agentPosition, Point goalPosition, boolean success) {
        this.localObs = localObs;
        this.globalObs = globalObs;
        this.agentPosition = agentPosition;
        this.goalPosition = goalPosition;
        this.success = success;
    }
}
