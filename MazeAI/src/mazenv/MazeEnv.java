package mazenv;

import java.awt.Point;
import java.util.List;

public class MazeEnv {
    /**
     * Các hành động có thể thực hiện trong mê cung. <p>
     * Các hành động này được sử dụng để di chuyển tác tử trong mê cung. <p>
     * UP: Đi lên <p>
     * DOWN: Đi xuống <p>
     * LEFT: Đi sang trái <p>
     * RIGHT: Đi sang phải <p>
     * Các hành động này được sử dụng trong phương thức step() để thực hiện hành động.
     */
    public class Action{
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
    }

    /**
     * Các buff có thể kích hoạt trong mê cung. <p>
     * SENRIGAN: Tăng tầm nhìn của tác tử. <p>
     * TOU_NO_HIKARI: Quan sát khu vực rộng lớn xung quanh tác tử. <p>
     * UNMEI_NO_MICHI: Tăng số lượng đường đi đến đích. <p>
     * SLIME_SAN_ONEGAI: Tìm kiếm đường có thể đi xung quanh tác tử. <p>
     * Các buff này được sử dụng trong phương thức regenerateMaze() để kích hoạt buff cho mê cung.
     */
    public class Buff{
        /**
         * Không có buff nào được kích hoạt.
         */
        public static final int NONE = 0;
        /**
         * Buff tăng tầm nhìn của tác tử.
         */
        public static final int SENRIGAN = 1;
        /**
         * Buff nhìn thấy khu vực rộng lớn xung quanh tác tử.
         */
        public static final int TOU_NO_HIKARI = 2;
        /**
         * Buff tăng số lượng đường đi đến đích.
         */
        public static final int UNMEI_NO_MICHI = 3;
        /**
         * Buff tìm kiếm đường có thể đi xung quanh tác tử.
         */
        public static final int SLIME_SAN_ONEGAI = 4;

        public static final int SLIME_STEP = 100;
        public static final int TOU_NO_HIKARI_OBS = 8;
    }

    /**
     * Các debuff có thể kích hoạt trong mê cung. <p>
     * WAAMU_HOURU: Dịch chuyển tác tử đến vị trí ngẫu nhiên trong mê cung. <p>
     * SHIN_NO_MEIRO: Tạo mê cung chết, không có đường đi đến đích. <p>
     * Các debuff này được sử dụng trong phương thức regenerateMaze() để kích hoạt debuff cho mê cung.
     */
    public class Debuff{
        /**
         * Không có debuff nào được kích hoạt.
         */
        public static final int NONE = 0;
        /**
         * Debuff dịch chuyển tác tử đến vị trí ngẫu nhiên trong mê cung.
         */
        public static final int WAAMU_HOURU = 1;
        /**
         * Debuff tạo mê cung chết, không có đường đi đến đích.
         */
        public static final int SHIN_NO_MEIRO = 2;

        public static final int SHUU_MATSU_DO_KEI = 3;
        public static List<Integer> getDebuffList() {
            return List.of(WAAMU_HOURU, SHIN_NO_MEIRO, SHUU_MATSU_DO_KEI);
        }
    }

    private Maze maze;
    private DiscoveredMaze discoveredMaze;
    private boolean senriganBuff = false;
    private int slimeStep;
    private int touNoHikariObs;
    public int maxStep;
    
    public MazeEnv(int mazeSize, int maxStep, int pathPercent, int slimeStep, int touNoHikariObs) {
        this(mazeSize, maxStep, pathPercent, slimeStep, touNoHikariObs, false);
    }

    public MazeEnv(int mazeSize, int maxStep, int pathPercent, int slimeStep, int touNoHikariObs, boolean hellMode) {
        this.maze = new Maze(mazeSize, pathPercent);
        this.discoveredMaze = new DiscoveredMaze(mazeSize, maze.getDiscoverData(3), hellMode);
        this.maxStep = maxStep;
        this.slimeStep = slimeStep;
        this.touNoHikariObs = touNoHikariObs;
    }

    public void createDataSet(String path) {
        // Implementation here
    }

    public void exportMazeData() {
        // Implementation here
    }

    /**
     * Khởi tạo lại trạng thái của môi trường mê cung.
     * @return Trạng thái mê cung mới.
     * Thông tin trạng thái mê cung xem tại lớp MazeState.
     */
    public MazeState reset() {
        maze.reset();
        return regenerateMaze(Buff.NONE);
    }

    /**
     * Khởi tạo lại trạng thái của môi trường mê cung với buff.
     * @param buff Buff được kích hoạt.
     * @return Trạng thái mê cung mới.
     * Thông tin trạng thái mê cung xem tại lớp MazeState.
     */
    public MazeState reset(int buff) {
        maze.reset();
        return regenerateMaze(buff, Debuff.NONE);
    }

    /**
     * Tái tạo lại mê cung với buff và không có debuff.
     * @param buff
     * @return Trạng thái mê cung mới.
     * Thông tin trạng thái mê cung xem tại lớp MazeState.
     */
    public MazeState regenerateMaze(int buff) {
        return regenerateMaze(buff, Debuff.NONE);
    }

    /**
     * Tái tạo lại mê cung với buff và debuff.
     * @param buff Buff được kích hoạt.
     * @param debuff Debuff được kích hoạt.
     * @return Trạng thái mê cung mới.
     * Thông tin trạng thái mê cung xem tại lớp MazeState.
     */
    public MazeState regenerateMaze(int buff, int debuff) {
        // Kích hoạt buff 
        int pathToGoalAdder = 0;
        boolean deadMaze = false;
        switch (buff) {
            case Buff.SENRIGAN:
                senriganBuff = true;
                break;
            case Buff.UNMEI_NO_MICHI:
                pathToGoalAdder = 1;
            default:
                senriganBuff = false;
                break;
        }
        // Kích hoạt debuff
        switch (debuff) {
            case Debuff.WAAMU_HOURU:
                maze.activateWaamuHouruDebuff();
                break;
            case Debuff.SHIN_NO_MEIRO:
                deadMaze = true;
                break;
            default:
                break;
        }
        maze.generateMaze(pathToGoalAdder, deadMaze);
        discoveredMaze.reset();

        // Kích hoạt buff
        switch (buff) {
            case Buff.SLIME_SAN_ONEGAI:
                discoveredMaze.discoverMaze(maze.activateSlimeBuff(slimeStep));
                break;
            case Buff.TOU_NO_HIKARI:
                discoveredMaze.discoverMaze(maze.getDiscoverData(touNoHikariObs));
                break;
        }
        int[][] mazeData = maze.getDiscoverData(senriganBuff ? 5 : 3);
        discoveredMaze.discoverMaze(mazeData);
        
        return new MazeState(discoveredMaze.getDiscoveredMaze(maze.getAgentPosition()),
                maze.getAgentPosition(),
                maze.getGoalPosition(),
                false);
    }

    /**
     * Thực hiện một bước trong môi trường mê cung. <p>
     * Các Action được định nghĩa trong lớp Action.
     * @param action
     * @return Cặp trạng thái mê cung và trạng thái thực hiện hành động.
     * Thông tin trạng thái mê cung xem tại lớp MazeState.
     * @note Trạng thái thực hiện hành động có thể là true hoặc false tuỳ theo hành động có hợp lệ hay không. 
     * Nếu hành động không hợp lệ, trạng thái mê cung sẽ không thay đổi.
     * Nếu hành động hợp lệ, trạng thái mê cung sẽ thay đổi và trả về trạng thái mới.
     */
    public Pair<MazeState, Boolean> step(int action) {
        boolean takeAction = maze.step(action);
        Point agentPos = maze.getAgentPosition();
        int[][] mazeData = maze.getDiscoverData(senriganBuff ? 5 : 3);
        discoveredMaze.discoverMaze(mazeData);
        boolean success = maze.isGoal();
        MazeState state = new MazeState(discoveredMaze.getDiscoveredMaze(agentPos),
                agentPos,
                maze.getGoalPosition(),
                success);
        return new Pair<MazeState, Boolean> (state, takeAction);
    }

    /**
     * Đặt lại kích thước mê cung.
     * @param mazeSize
     */
    public void setMazeSize(int mazeSize) {
        maze = new Maze(mazeSize, maze.getPathPercent());
    }

    /**
     * Đặt lại tỷ lệ đường đi trong mê cung.
     * @param pathPercent
     */
    public void setPathPercent(int pathPercent) {
        maze.setPathPercent(pathPercent);
    }

    /**
     * Trả về mê cung hiện tại.<p>
     * Ý nghĩa giá trị của từng ô xem tại lớp Maze.
     * @return Mê cung hiện tại.
     */
    public int[][] getMaze() {
        return maze.getMaze();
    }

    /**
     * Lấy ma trận đã khám phá.<p>
     * Ý Nghĩa giá trị của từng ô xem tại lớp Maze.
     * @return Ma trận đã khám phá.
     */
    public int[][] getDiscoveredMaze() {
        return discoveredMaze.getDiscoveredMaze(maze.getAgentPosition());
    }

    /**
     * Lấy vị trí của tác tử trong mê cung.
     */
    public Point getAgentPosition() {
        return maze.getAgentPosition();
    }

    /**
     * Kiểm tra xem tác tử có ở trong khu vực đích hay không.
     * @return true nếu tác tử ở trong khu vực đích, false nếu không.
     */
    public Boolean inGoalArea() {
        if (maze.getAgentPosition().x >= maze.getMazeSize() * 2 / 3 && maze.getAgentPosition().y >= maze.getMazeSize() * 2 / 3) {
            return true;
        } else {
            return false;
        }
    }
}
