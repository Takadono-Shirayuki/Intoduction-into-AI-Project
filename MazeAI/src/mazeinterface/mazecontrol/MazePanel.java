package mazeinterface.mazecontrol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import mazenv.*;

/**
 * Lớp hiển thị và quản lý giao diện đồ họa cho mê cung.
 * <p>
 * Bao gồm: vẽ mê cung, di chuyển người chơi bằng bàn phím, xử lý tầm nhìn và cập nhật khám phá.
 */
public class MazePanel extends JPanel {
    private int mazeSize;
    private float scale = 1.0f;
    private boolean fullView = false;

    private MazeEnv mazeEnv;

    /**
     * Khởi tạo panel mê cung.
     * @param mazeSize Kích thước mê cung (số ô mỗi chiều)
     * @param mazeEnv Đối tượng môi trường mê cung
     */
    public MazePanel(int mazeSize, MazeEnv mazeEnv) {
        this.mazeSize = mazeSize;
        this.mazeEnv = mazeEnv;

        setPreferredSize(new Dimension(750, 750));
        adjustScaleToFit();

        // Thiết lập để nhận sự kiện phím
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    /**
     * Xử lý sự kiện phím bấm để di chuyển người chơi.
     * @param e Sự kiện KeyEvent
     */
    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                movePlayer(MazeEnv.Action.UP);
                break;
            case KeyEvent.VK_DOWN:
                movePlayer(MazeEnv.Action.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                movePlayer(MazeEnv.Action.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                movePlayer(MazeEnv.Action.RIGHT);
                break;
            default:
                break;
        }
    }

    /**
     * Di chuyển người chơi theo hướng chỉ định.
     * @param action Hướng di chuyển (MazeEnv.Action)
     */
    public void movePlayer(int action) {
        Pair<MazeState, Boolean> stepState = mazeEnv.step(action);

        repaint();
        if (stepState.getItem1().success == true) {
            JOptionPane.showMessageDialog(this, "Đã tìm thấy đường đi đến đích!");
            mazeEnv.reset();
            repaint();
        }
    }

    /**
     * Chuyển đổi giữa chế độ xem toàn bộ mê cung và chế độ xem theo tầm nhìn người chơi.
     */
    public void toggleFullView() {
        fullView = !fullView;
        repaint();
    }

    /**
     * Kiểm tra chế độ xem toàn bộ mê cung.
     * @return true nếu đang xem toàn bộ, false nếu chỉ xem tầm nhìn.
     */
    public boolean isFullView() {
        return fullView;
    }

    /**
     * Điều chỉnh tỷ lệ hiển thị sao cho mê cung vừa với kích thước panel.
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
     * @param g Đối tượng Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    
        int cellWidth = (int)(scale);
        int cellHeight = (int)(scale);
    
        int mazePixelWidth = mazeSize * cellWidth;
        int mazePixelHeight = mazeSize * cellHeight;
    
        // Tính offset căn giữa mê cung
        int offsetX = (getWidth() - mazePixelWidth) / 2;
        int offsetY = (getHeight() - mazePixelHeight) / 2;
    
        // Lấy dữ liệu mê cung (toàn bộ hoặc chỉ phần đã khám phá)
        int[][] mazeData = fullView ? mazeEnv.getMaze() : mazeEnv.getDiscoveredMaze();
    
        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                int drawX = offsetX + col * cellWidth;
                int drawY = offsetY + row * cellHeight;
    
                // Chọn màu sắc và vẽ từng ô
                switch (mazeData[row][col]) {
                    case Maze.WALL:
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                        break;
                    case Maze.PATH:
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                        break;
                    case Maze.GOAL:
                        g2d.setColor(Color.RED);
                        g2d.fillOval(drawX, drawY, cellWidth, cellHeight);
                        break;
                    case Maze.AGENT_POSITION:
                        g2d.setColor(Color.BLUE);
                        g2d.fillOval(drawX, drawY, cellWidth, cellHeight);
                        break;
                    case Maze.UNEXPLORED:
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                        break;
                    default:
                        if (mazeData[row][col] == 5 * Maze.PATH) {
                            g2d.setColor(Color.LIGHT_GRAY);
                            g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                        }
                        break;
                }
    
                // Vẽ viền ô
                g2d.setColor(Color.GRAY);
                g2d.drawRect(drawX, drawY, cellWidth, cellHeight);
            }
        }
    }

    /**
     * Sử dụng kỹ năng đặc biệt trong mê cung.
     * @param skill Kỹ năng sử dụng (theo hằng số trong lớp Buff)
     */
    public void useSkill(int skill) {
        mazeEnv.regenerateMaze(skill, MazeEnv.Debuff.NONE);
        repaint();
        requestFocusInWindow();  // Gọi lại để đảm bảo nhận sự kiện từ bàn phím
    }
}
