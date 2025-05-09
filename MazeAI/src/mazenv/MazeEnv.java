package mazenv;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        private static final String SENRIGAN_DOC = "/src/mazeai/Document/Senrigan.txt";
        private static final String SLIME_SAN_ONEGAI_DOC = "/src/mazeai/Document/SlimeSanOnegai.txt";
        private static final String TOU_NO_HIKARI_DOC = "/src/mazeai/Document/TouNoHikari.txt";
        private static final String UNMEI_NO_MICHI_DOC = "/src/mazeai/Document/UnmeiNoMichi.txt";
        /**
         * Lấy tài liệu mô tả kỹ năng từ đường dẫn.
         * @param path Đường dẫn đến tài liệu
         * @return Tên và mô tả kỹ năng dưới dạng Pair
         */
        public static Pair<String, String> getBuffInfo(int buff) {
            String path = "";
            switch (buff) {
                case Buff.SENRIGAN:
                    path = SENRIGAN_DOC;
                    break;
                case Buff.SLIME_SAN_ONEGAI:
                    path = SLIME_SAN_ONEGAI_DOC;
                    break;
                case Buff.TOU_NO_HIKARI:
                    path = TOU_NO_HIKARI_DOC;
                    break;
                case Buff.UNMEI_NO_MICHI:   
                    path = UNMEI_NO_MICHI_DOC;
                    break;
                default:
                    break;
            }
            String skillName, skillDescription;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + path))) {
                skillName = reader.readLine(); // Đọc dòng đầu tiên là tên kỹ năng
                
                StringBuilder descriptionBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    descriptionBuilder.append(line).append("\n"); // Đọc các dòng tiếp theo là mô tả kỹ năng
                }
                skillDescription = descriptionBuilder.toString().trim(); // Chuyển đổi thành chuỗi và loại bỏ khoảng trắng thừa
            } catch (IOException e) {
                System.err.println("Không thể đọc tài liệu: " + path);
                return null;
            }
            return new Pair<String, String>(skillName, skillDescription); // Trả về tên và mô tả kỹ năng
        }
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

        /**
         * Debuff giới hạn số bước đi còn lại của người chơi.
         */
        public static final int SHUU_MATSU_DO_KEI = 3;

        /**
         * Lấy danh sách các debuff có thể kích hoạt.
         * @return Danh sách các debuff có thể kích hoạt.
         */
        public static List<Integer> getDebuffList() {
            return List.of(WAAMU_HOURU, SHIN_NO_MEIRO, SHUU_MATSU_DO_KEI);
        }

        private static final String WAAMU_HOURU_DOC = "/src/mazeai/Document/WaamuHouru.txt";
        private static final String SHIN_NO_MEIRO_DOC = "/src/mazeai/Document/ShinNoMeiro.txt";
        private static final String SHUU_MATSU_DO_KEI_DOC = "/src/mazeai/Document/ShuuMatsuDoKei.txt";
        /**
         * Lấy tài liệu mô tả debuff từ đường dẫn.
         * @param path Đường dẫn đến tài liệu
         * @return Tên và mô tả debuff dưới dạng Pair
         */
         public static Pair<String, String> getBuffInfo(int buff) {
            String path = "";
            switch (buff) {
                case Debuff.WAAMU_HOURU:
                    path = WAAMU_HOURU_DOC;
                    break;
                case Debuff.SHIN_NO_MEIRO:
                    path = SHIN_NO_MEIRO_DOC;
                    break;
                case Debuff.SHUU_MATSU_DO_KEI:
                    path = SHUU_MATSU_DO_KEI_DOC;
                    break;
                default:
                    break;
            }
            String debuffName, debuffDescription;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + path))) {
                debuffName = reader.readLine(); // Đọc dòng đầu tiên là tên kỹ năng
                
                StringBuilder descriptionBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    descriptionBuilder.append(line).append("\n"); // Đọc các dòng tiếp theo là mô tả kỹ năng
                }
                debuffDescription = descriptionBuilder.toString().trim(); // Chuyển đổi thành chuỗi và loại bỏ khoảng trắng thừa
            } catch (IOException e) {
                System.err.println("Không thể đọc tài liệu: " + path);
                return null;
            }
            return new Pair<String, String>(debuffName, debuffDescription); // Trả về tên và mô tả kỹ năng
        }
    }

    private Maze maze;
    private DiscoveredMaze discoveredMaze;
    private boolean senriganBuff = false;
    private int slimeStep; // Thay đổi mỗi khi qua màn
    private int touNoHikariObs; 
    public int maxStep; // Thay đổi mỗi khi qua màn
    private int stepRemaining = 0;
    private int numberOfUsedSteps = 0; 
    private int reward; // Thay đổi mỗi khi qua màn 
    // Lưu dữ liệu khởi tạo
    private int baseMazeSize;
    private int baseMaxStep;
    private int basePathPercent;
    private int baseSlimeStep;
    private int baseTouNoHikariObs;
    private boolean hellMode = false;

    public MazeEnv(int mazeSize, int maxStep, int pathPercent, int slimeStep, int touNoHikariObs) {
        this(mazeSize, maxStep, pathPercent, slimeStep, touNoHikariObs, false);
    }

    public MazeEnv(int mazeSize, int maxStep, int pathPercent, int slimeStep, int touNoHikariObs, boolean hellMode) {
        this.maze = new Maze(mazeSize, pathPercent);
        this.discoveredMaze = new DiscoveredMaze(mazeSize, maze.getDiscoverData(3), hellMode);
        this.maxStep = maxStep;
        this.slimeStep = slimeStep;
        this.touNoHikariObs = touNoHikariObs;
        this.reward = 4 * mazeSize;
        reset();

        // Lưu dữ liệu khởi tạo
        this.baseMazeSize = mazeSize;
        this.baseMaxStep = maxStep;
        this.basePathPercent = pathPercent;
        this.baseSlimeStep = slimeStep;
        this.baseTouNoHikariObs = touNoHikariObs;
        this.hellMode = hellMode;
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
        stepRemaining += reward - numberOfUsedSteps;
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
                Point agentPos = maze.getAgentPosition();
                Point goalPos = maze.getGoalPosition();
                if (Math.abs(agentPos.x - goalPos.x) < 2 && Math.abs(agentPos.y - goalPos.y) < 2) {
                    maze.activateWaamuHouruDebuff();
                }
                break;
            case Debuff.SHUU_MATSU_DO_KEI:
                numberOfUsedSteps = numberOfUsedSteps + stepRemaining - maxStep;
                stepRemaining = maxStep;
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
        if (takeAction) {
            numberOfUsedSteps++;
            stepRemaining --;
        }
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

    /**
     * Trả về số bước còn lại trong mê cung. <p>
     * Khi số bước về 0, trò chơi kết thúc. <p>
     * Nhận được thêm số bước mỗi khi qua màn.
     * @return Số bước còn lại trong mê cung.
     */
    public int getStepRemaining() {
        return stepRemaining;
    }

    public void gameOver() {
        // Khởi tạo lại môi trường mê cung với các thông số ban đầu.
        maze = new Maze(baseMazeSize, basePathPercent);
        discoveredMaze = new DiscoveredMaze(baseMazeSize, maze.getDiscoverData(3), hellMode);
        maxStep = baseMaxStep;
        slimeStep = baseSlimeStep;
        touNoHikariObs = baseTouNoHikariObs;
        reward = 4 * baseMazeSize;

        stepRemaining = 0;
        numberOfUsedSteps = 0;
        senriganBuff = false;
        // Đặt lại mê cung về trạng thái ban đầu.
        reset();
    }
}
