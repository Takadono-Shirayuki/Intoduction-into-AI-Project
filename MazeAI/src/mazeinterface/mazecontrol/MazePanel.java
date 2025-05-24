package mazeinterface.mazecontrol;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import game.Main;
import mazeinterface.GameForm;
import mazenv.*;
import mazenv.MazeEnv.Debuff;

public class MazePanel extends JPanel {
    private int mazeSize;
    private float scale = 1.0f;
    private boolean fullView = false;

    private MazeEnv mazeEnv;
    private int stepCounter = 0;
    private GameForm parent;

    private Image playerIcon; // ảnh người chơi

    public MazePanel(int mazeSize, MazeEnv mazeEnv, GameForm parent) {
        Main.setMazePanel(this);
        
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
            System.err.println("Không thể tải ảnh Robot.jpg");
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    public void resetGame() {
        mazeEnv.gameOver();
        stepCounter = 0;
        repaint();
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP -> movePlayer(MazeEnv.Action.UP);
            case KeyEvent.VK_DOWN -> movePlayer(MazeEnv.Action.DOWN);
            case KeyEvent.VK_LEFT -> movePlayer(MazeEnv.Action.LEFT);
            case KeyEvent.VK_RIGHT -> movePlayer(MazeEnv.Action.RIGHT);
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
                int debuff = Debuff.NONE;

                if (mazeEnv.inGoalArea()) {
                    List<Integer> debuffs = Debuff.getDebuffList();
                    Random random = new Random();
                    debuff = debuffs.get(random.nextInt(debuffs.size()));
                    Pair<String, String> debuffInfo = Debuff.getDebuffInfo(debuff);
                    String message = "<html><div style='text-align: center;'>" + debuffInfo.getItem1() + "</div><br>" +
                            debuffInfo.getItem2().replace("\n", "<br>") + "</html>";
                    parent.showMessage(message, new Dimension(400, 250));
                }

                mazeEnv.regenerateMaze(buff, debuff);
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

    // ✅ Căn giữa mê cung theo panel
    int offsetX = (panelWidth - mazePixelWidth) / 2;
    int offsetY = (panelHeight - mazePixelHeight) / 2;

    // ✅ Viền đậm hơn
    g2d.setStroke(new BasicStroke(2));

    // Lấy dữ liệu mê cung (toàn bộ hoặc khám phá)
    int[][] mazeData = fullView ? mazeEnv.getMaze() : mazeEnv.getDiscoveredMaze();

    for (int row = 0; row < mazeSize; row++) {
        for (int col = 0; col < mazeSize; col++) {
            int drawX = offsetX + col * cellWidth;
            int drawY = offsetY + row * cellHeight;

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

            // ✅ Vẽ viền ô
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
    /** Cho phép cập nhật lại môi trường khi load */
    public void setMazeEnv(MazeEnv env) {
        this.mazeEnv = env;
    }

    public void setStepCounter(int count) {
        this.stepCounter = count;
    }

}

