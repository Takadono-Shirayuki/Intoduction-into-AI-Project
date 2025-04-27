package mazeai;

import javax.swing.*;
import java.awt.*;
import mazenv.*;

/**
 * Lớp hiển thị và quản lý giao diện đồ họa cho mê cung.
 * <p>
 * Lớp này kế thừa từ JPanel và chịu trách nhiệm vẽ mê cung, xử lý tương tác người dùng
 * và hiển thị trạng thái hiện tại của trò chơi.
 */
public class MazePanel extends JPanel {
    private int mazeSize;
    private float scale = 1.0f;
    private boolean fullView = false;
    private int lightSize;
    
    private MazeEnv mazeEnv;
    private SkillManager skillManager;

    /**
     * Khởi tạo panel mê cung.
     * @param mazeSize Kích thước mê cung (số ô mỗi chiều)
     * @param lightSize Bán kính tầm nhìn của người chơi
     * @param mazeEnv Đối tượng môi trường mê cung
     */
    public MazePanel(int mazeSize, int lightSize, MazeEnv mazeEnv) {
        this.mazeSize = mazeSize;
        this.lightSize = lightSize;
        this.mazeEnv = mazeEnv;
        this.skillManager = new SkillManager(mazeEnv);

        setPreferredSize(new Dimension(800, 600));
        adjustScaleToFit();
    }

    /**
     * Di chuyển người chơi theo hướng chỉ định.
     * @param action Hướng di chuyển (sử dụng hằng số từ MazeEnv.Action)
     */
    public void movePlayer(int action) {
        Pair<MazeState, Boolean> stepState = mazeEnv.step(action);
        repaint();
    }

    /**
     * Chuyển đổi giữa chế độ xem toàn bộ mê cung và chế độ xem thông thường.
     */
    public void toggleFullView() {
        fullView = !fullView;
        repaint();
    }

    /**
     * Kiểm tra có đang ở chế độ xem toàn bộ mê cung hay không.
     * @return true nếu đang ở chế độ xem toàn bộ, false nếu ngược lại
     */
    public boolean isFullView() {
        return fullView;
    }

    /**
     * Điều chỉnh tỷ lệ hiển thị để mê cung vừa với kích thước panel.
     */
    public void adjustScaleToFit() {
        int width = getWidth();
        int height = getHeight();
        scale = Math.min((float) width / mazeSize, (float) height / mazeSize);
        repaint();
    }

    /**
     * Tạo mê cung mới và reset trạng thái trò chơi.
     */
    public void generateNewMaze() {
        mazeEnv.reset();
        repaint();
    }

    /**
     * Vẽ các thành phần của mê cung lên panel.
     * @param g Đối tượng Graphics để vẽ
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int cellWidth = (int)(scale);
        int cellHeight = (int)(scale);
        
        // Lấy dữ liệu mê cung tùy theo chế độ xem
        int[][] mazeData = fullView ? mazeEnv.getMaze() : mazeEnv.getDiscoveredMaze();
        
        // Tìm vị trí agent và đích trong dữ liệu mê cung
        Point agentPos = findPosition(mazeData, Maze.AGENT_POSITION);
        Point goalPos = findPosition(mazeData, Maze.GOAL);
        
        // Vẽ từng ô của mê cung
        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                // Vẽ ô dựa trên loại của nó
                switch (mazeData[row][col]) {
                    case Maze.WALL:
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        break;
                    case Maze.PATH:
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        break;
                    case Maze.GOAL:
                        g2d.setColor(Color.RED);
                        g2d.fillOval(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        break;
                    case Maze.AGENT_POSITION:
                        g2d.setColor(Color.BLUE);
                        g2d.fillOval(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        break;
                    case Maze.UNEXPLORED:
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        break;
                    default:
                        // Đối với các đường đã khám phá
                        if (mazeData[row][col] == 5 * Maze.PATH) {
                            g2d.setColor(Color.LIGHT_GRAY);
                            g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                        }
                        break;
                }
                
                // Vẽ đường viền ô
                g2d.setColor(Color.GRAY);
                g2d.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
        
        // Vẽ rõ vị trí agent và đích nếu tồn tại
        if (agentPos != null) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(agentPos.y * cellWidth, agentPos.x * cellHeight, cellWidth, cellHeight);
        }
        
        if (goalPos != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(goalPos.y * cellWidth, goalPos.x * cellHeight, cellWidth, cellHeight);
        }
    }

    /**
     * Tìm vị trí của một loại ô cụ thể trong mê cung.
     * @param mazeData Dữ liệu mê cung
     * @param target Loại ô cần tìm (sử dụng hằng số từ Maze)
     * @return Vị trí của ô hoặc null nếu không tìm thấy
     */
    private Point findPosition(int[][] mazeData, int target) {
        for (int row = 0; row < mazeData.length; row++) {
            for (int col = 0; col < mazeData[row].length; col++) {
                if (mazeData[row][col] == target) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Sử dụng kỹ năng đặc biệt trong mê cung.
     * @param skill Tên kỹ năng cần sử dụng
     */
    public void useSkill(String skill) {
        System.out.println("Using skill: " + skill);
        int[][] mazeData = mazeEnv.getMaze();
        Point agentPos = findPosition(mazeData, Maze.AGENT_POSITION);
        if (agentPos != null) {
            skillManager.useSkill(skill, agentPos.x, agentPos.y);
        }
        repaint();
    }
}