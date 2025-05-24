package mazeinterface.mazecontrol;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import mazeinterface.GameForm;
import mazenv.Maze;
import mazenv.MazeEnv;
import mazenv.MazeState;
import mazenv.Pair;


/**
 * Lớp quản lý giao diện hiển thị mê cung, xử lý bàn phím và tương tác.
 */
public class MazePanel extends JPanel {
    private int mazeSize;
    private float scale = 1.0f;
    private boolean fullView = false;

    private MazeEnv mazeEnv;
    private int stepCounter = 0;
    private GameForm parent;

    private Image playerIcon;
    private Image wallTile;

    /**
     * Khởi tạo MazePanel với mazeEnv và gameForm liên kết.
     */
    public MazePanel(int mazeSize, MazeEnv mazeEnv, GameForm parent) {
        this.mazeSize = mazeSize;
        this.mazeEnv = mazeEnv;
        this.parent = parent;

        setPreferredSize(new Dimension(750, 750));
        adjustScaleToFit();
        setOpaque(false);
        setFocusable(true);
        requestFocusInWindow();

        try {
            playerIcon = new ImageIcon(getClass().getResource("/mazeai/Icon/Robot.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải Robot.jpg");
        }

        try {
            wallTile = new ImageIcon(getClass().getResource("/mazeai/Icon/wall.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải wall.jpg");
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    public void setMazeEnv(MazeEnv env) {
        this.mazeEnv = env;
    }

    public void resetGame() {
        mazeEnv.gameOver();
        stepCounter = 0;
        repaint();
    }

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

    public Pair<MazeState, Boolean> movePlayer(int action) {
        Pair<MazeState, Boolean> stepState = mazeEnv.step(action);
        if (stepState.getItem2()) {
            stepCounter++;
            repaint();
            if (mazeEnv.getStepRemaining() == 0) {
                parent.showMessage("Trò chơi kết thúc", new Dimension(300, 100));
                mazeEnv.gameOver();
                stepCounter = mazeEnv.maxStep;
            }

            if (stepState.getItem1().success) {
                parent.showMessage("Đã tìm thấy đường tới đích", new Dimension(300, 100));
                mazeEnv.reset();
                stepCounter = mazeEnv.maxStep;
            }

            if (stepCounter == mazeEnv.maxStep) {
                int buff = parent.openSkillDialog();
                mazeEnv.regenerateMaze(buff, MazeEnv.Debuff.NONE);
                stepCounter = 0;
            }
            repaint();
        }
        return stepState;
    }

    public void toggleFullView() {
        fullView = !fullView;
        repaint();
    }

    public boolean isFullView() {
        return fullView;
    }

    public void adjustScaleToFit() {
        int width = getWidth();
        int height = getHeight();
        scale = Math.min((float) width / mazeSize, (float) height / mazeSize);
        repaint();
    }

    /**
     * Phần vẽ mê cung — chỉ phần này được sửa để thêm ảnh viền.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cellWidth = (int) scale;
        int cellHeight = (int) scale;

        int mazePixelWidth = mazeSize * cellWidth;
        int mazePixelHeight = mazeSize * cellHeight;

        int offsetX = (panelWidth - mazePixelWidth) / 2;
        int offsetY = (panelHeight - mazePixelHeight) / 2;

        g2d.setStroke(new BasicStroke(2));

        int[][] mazeData = fullView ? mazeEnv.getMaze() : mazeEnv.getDiscoveredMaze();

        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                int drawX = offsetX + col * cellWidth;
                int drawY = offsetY + row * cellHeight;

                // ✅ Vẽ gạch ở viền mê cung
                if (row == 0 || row == mazeSize - 1 || col == 0 || col == mazeSize - 1) {
                    if (wallTile != null) {
                        g2d.drawImage(wallTile, drawX, drawY, cellWidth, cellHeight, this);
                    } else {
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                    }
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(drawX, drawY, cellWidth, cellHeight);
                    continue;
                }

                switch (mazeData[row][col]) {
                    case Maze.WALL -> {
                        g2d.setColor(new Color(0, 0, 0, 0));
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                    }
                    case Maze.PATH -> {
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                    }
                    case Maze.GOAL -> {
                        g2d.setColor(Color.RED);
                        g2d.fillOval(drawX, drawY, cellWidth, cellHeight);
                    }
                    case Maze.AGENT_POSITION -> {
                        if (playerIcon != null) {
                            g2d.drawImage(playerIcon, drawX, drawY, cellWidth, cellHeight, this);
                        } else {
                            g2d.setColor(Color.BLUE);
                            g2d.fillOval(drawX, drawY, cellWidth, cellHeight);
                        }
                    }
                    case Maze.UNEXPLORED -> {
                        g2d.setColor(new Color(0, 0, 0, 0));
                        g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                    }
                    default -> {
                        if (mazeData[row][col] == 5 * Maze.PATH) {
                            g2d.setColor(Color.LIGHT_GRAY);
                            g2d.fillRect(drawX, drawY, cellWidth, cellHeight);
                        }
                    }
                }

                g2d.setColor(Color.BLACK);
                g2d.drawRect(drawX, drawY, cellWidth, cellHeight);
            }
        }
    }

    public void useSkill(int skill) {
        mazeEnv.regenerateMaze(skill, MazeEnv.Debuff.NONE);
        repaint();
        requestFocusInWindow();
    }
}
