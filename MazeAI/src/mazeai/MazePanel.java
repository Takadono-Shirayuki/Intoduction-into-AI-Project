package mazeai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class MazePanel extends JPanel implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {
    private int[][] maze;
    private int rows, cols;
    private int playerX = 0, playerY = 0;
    private int goalX, goalY;
    private int lightRadius;
    private boolean fullView = false;
    private boolean temporaryView = false;
    private List<Point> currentPath = Collections.emptyList();
    private ExecutorService pathfindingExecutor = Executors.newSingleThreadExecutor();
    private float scale = 1.0f;
    private Point lastDragPoint;
    private Point viewOffset = new Point(0, 0);
    private final int BASE_TILE_SIZE = 20;

    // Khởi tạo mê cung với kích thước và tầm nhìn
    public MazePanel(int rows, int cols, int lightRadius) {
        this.rows = rows;
        this.cols = cols;
        this.lightRadius = lightRadius;
        generateNewMaze();
        setFocusable(true);
        addKeyListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void generateNewMaze() {
        maze = new int[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = (rand.nextDouble() < 0.2) ? 1 : 0;
            }
        }
        playerX = 0;
        playerY = 0;
        goalX = cols - 1;
        goalY = rows - 1;
        maze[playerY][playerX] = 0;
        maze[goalY][goalX] = 0;
        fullView = false;
        temporaryView = false;
        currentPath.clear();
        scale = calculateInitialScale();
        viewOffset.setLocation(0, 0);
        repaint();
    }

    private float calculateInitialScale() {
        Container parent = getParent();
        if (parent == null) return 1.0f;
        
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        
        float scaleX = (float)parentWidth / (cols * BASE_TILE_SIZE);
        float scaleY = (float)parentHeight / (rows * BASE_TILE_SIZE);
        
        // Giới hạn scale trong khoảng từ 0.1 đến 10
        return Math.max(0.1f, Math.min(Math.min(scaleX, scaleY), 10.0f));
    }

    public void adjustScaleToFit() {
        float newScale = calculateInitialScale();
        if (Math.abs(newScale - scale) > 0.01f) {
            scale = newScale;
            revalidate();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Áp dụng transform để phóng to và di chuyển
        g2d.translate(viewOffset.x, viewOffset.y);
        g2d.scale(scale, scale);

        // Vẽ nền
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, cols * BASE_TILE_SIZE, rows * BASE_TILE_SIZE);

        // Vẽ mê cung
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean visible = fullView || temporaryView || isInLightRadius(col, row);
                
                if (!visible) {
                    g2d.setColor(new Color(30, 30, 30));
                } else if (maze[row][col] == 1) {
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                
                g2d.fillRect(col * BASE_TILE_SIZE, row * BASE_TILE_SIZE, BASE_TILE_SIZE, BASE_TILE_SIZE);
                g2d.setColor(Color.GRAY);
                g2d.drawRect(col * BASE_TILE_SIZE, row * BASE_TILE_SIZE, BASE_TILE_SIZE, BASE_TILE_SIZE);
            }
        }

        // Vẽ đường đi
        if (!currentPath.isEmpty()) {
            g2d.setColor(new Color(0, 200, 255, 150));
            for (Point p : currentPath) {
                g2d.fillRect(p.x * BASE_TILE_SIZE + 1, p.y * BASE_TILE_SIZE + 1, 
                            BASE_TILE_SIZE - 2, BASE_TILE_SIZE - 2);
            }
        }

        // Vẽ đích
        g2d.setColor(Color.RED);
        g2d.fillRect(goalX * BASE_TILE_SIZE + 1, goalY * BASE_TILE_SIZE + 1, 
                     BASE_TILE_SIZE - 2, BASE_TILE_SIZE - 2);

        // Vẽ người chơi
        g2d.setColor(Color.BLUE);
        g2d.fillOval(playerX * BASE_TILE_SIZE + 2, playerY * BASE_TILE_SIZE + 2, 
                     BASE_TILE_SIZE - 4, BASE_TILE_SIZE - 4);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            (int)(cols * BASE_TILE_SIZE * scale), 
            (int)(rows * BASE_TILE_SIZE * scale)
        );
    }

    public void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (canMove(newX, newY)) {
            playerX = newX;
            playerY = newY;
            repaint();
            checkGoal();
        }
    }

    public void useSkill(String name) {
        switch (name) {
            case "Thiên lý nhãn":
                temporaryView = true;
                repaint();
                new javax.swing.Timer(2000, e -> {
                    temporaryView = false;
                    repaint();
                }).start();
                break;
                
            case "Ánh sáng của Đảng":
                lightRadius += 4;
                repaint();
                break;
                
            case "Slime thông thái":
                findPathAsync();
                break;
                
            case "Con đường vận mệnh":
                createPathToGoal();
                break;
        }
    }

    private void findPathAsync() {
        Future<?> future = pathfindingExecutor.submit(() -> {
            List<Point> path = findPathAStar();
            SwingUtilities.invokeLater(() -> {
                currentPath = (path != null) ? path : Collections.emptyList();
                repaint();
                if (path == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đường đi!");
                }
            });
        });
        
        new javax.swing.Timer(5000, e -> {
            if (!future.isDone()) {
                future.cancel(true);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Mê cung quá lớn, không thể tìm đường trong thời gian ngắn!");
                });
            }
        }).start();
    }

    private List<Point> findPathAStar() {
        class Node implements Comparable<Node> {
            Point point;
            Node parent;
            int g, h;

            Node(Point point, Node parent, int g, int h) {
                this.point = point;
                this.parent = parent;
                this.g = g;
                this.h = h;
            }

            int f() { return g + h; }

            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.f(), other.f());
            }
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Point, Integer> gScore = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();

        int h = Math.abs(playerX - goalX) + Math.abs(playerY - goalY);
        Node startNode = new Node(new Point(playerX, playerY), null, 0, h);
        openSet.add(startNode);
        gScore.put(startNode.point, 0);

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        while (!openSet.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) return null;
            
            Node current = openSet.poll();

            if (current.point.x == goalX && current.point.y == goalY) {
                List<Point> path = new ArrayList<>();
                Node node = current;
                while (node != null) {
                    path.add(node.point);
                    node = node.parent;
                }
                Collections.reverse(path);
                return path;
            }

            closedSet.add(current.point);

            for (int i = 0; i < 4; i++) {
                int nx = current.point.x + dx[i];
                int ny = current.point.y + dy[i];

                if (!canMove(nx, ny)) continue;

                Point neighbor = new Point(nx, ny);
                if (closedSet.contains(neighbor)) continue;

                int tentativeG = current.g + 1;
                if (!gScore.containsKey(neighbor) || tentativeG < gScore.get(neighbor)) {
                    int hScore = Math.abs(nx - goalX) + Math.abs(ny - goalY);
                    Node neighborNode = new Node(neighbor, current, tentativeG, hScore);
                    gScore.put(neighbor, tentativeG);
                    openSet.add(neighborNode);
                }
            }
        }
        return null;
    }

    private void checkGoal() {
        if (playerX == goalX && playerY == goalY) {
            JOptionPane.showMessageDialog(this, "🎉 Đã đến đích!");
            generateNewMaze();
        }
    }

    public void createPathToGoal() {
        int x = playerX, y = playerY;
        Random rand = new Random();
        
        while (x != goalX || y != goalY) {
            maze[y][x] = 0;

            int dx = goalX - x;
            int dy = goalY - y;

            if (Math.abs(dx) > Math.abs(dy)) {
                x += Integer.compare(dx, 0);
            } else if (dy != 0) {
                y += Integer.compare(dy, 0);
            } else {
                x += Integer.compare(dx, 0);
            }

            if (x >= 0 && y >= 0 && x < cols && y < rows) {
                maze[y][x] = 0;
            }
            
            // Thêm ngẫu nhiên để tránh đường thẳng quá
            if (rand.nextDouble() < 0.3) {
                if (rand.nextBoolean() && x > 0) x--;
                else if (x < cols - 1) x++;
            }
        }

        maze[goalY][goalX] = 0;
        repaint();
        JOptionPane.showMessageDialog(this, "✨ 'Con đường vận mệnh' đã mở đường thành công!");
    }

    public void toggleFullView() {
        fullView = !fullView;
        repaint();
    }

    public boolean isFullView() {
        return fullView;
    }

    private boolean canMove(int x, int y) {
        return x >= 0 && y >= 0 && x < cols && y < rows && maze[y][x] == 0;
    }

    private boolean isInLightRadius(int x, int y) {
        int r = lightRadius / 2;
        return Math.abs(playerX - x) <= r && Math.abs(playerY - y) <= r;
    }

    // Xử lý phím
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: movePlayer(0, -1); break;
            case KeyEvent.VK_DOWN: movePlayer(0, 1); break;
            case KeyEvent.VK_LEFT: movePlayer(-1, 0); break;
            case KeyEvent.VK_RIGHT: movePlayer(1, 0); break;
            case KeyEvent.VK_F: toggleFullView(); break;
            case KeyEvent.VK_EQUALS: case KeyEvent.VK_PLUS:
                scale *= 1.1f; revalidate(); repaint(); break;
            case KeyEvent.VK_MINUS:
                scale *= 0.9f; revalidate(); repaint(); break;
            case KeyEvent.VK_ESCAPE: generateNewMaze(); break;
        }
    }

    // Xử lý chuột
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float scaleFactor = e.getWheelRotation() < 0 ? 1.1f : 0.9f;
        scale *= scaleFactor;
        revalidate();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastDragPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point current = e.getPoint();
        int dx = current.x - lastDragPoint.x;
        int dy = current.y - lastDragPoint.y;
        viewOffset.translate(dx, dy);
        lastDragPoint = current;
        repaint();
    }

    // Các sự kiện không dùng tới
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}