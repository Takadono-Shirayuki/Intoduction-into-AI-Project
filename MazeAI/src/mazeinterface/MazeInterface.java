package mazeinterface;

import javax.swing.*;
import mazenv.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class MazeInterface {
    private JFrame frame;
    private MazePanel mazePanel;
    private Map<String, Integer> skillCounts = new HashMap<>();
    private Map<String, JLabel> skillLabels = new HashMap<>();
    private MazeEnv mazeEnv;

    public MazeInterface(int mazeSize) {
        // Initialize MazeEnv first
        this.mazeEnv = new MazeEnv(mazeSize, 15, 70, 50); // Example parameters
        
        frame = new JFrame("\uD83C\uDF00 Mê cung vô vọng - ver 1.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create background
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource("/mazeai/Icon/Background.jpg"));
            Image bgImage = bgIcon.getImage();
            frame.setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            });
            frame.getContentPane().setLayout(new BorderLayout());
        } catch (Exception e) {
            System.err.println("Không thể tải background");
        }

        // Initialize MazePanel with the MazeEnv instance
        mazePanel = new MazePanel(mazeSize, mazeEnv);

        // Initialize skill counts
        skillCounts.put("Thiên lý nhãn", 5);    // Corresponds to SENRIGAN
        skillCounts.put("Slime thông thái", 5); // Corresponds to SLIME_SAN_ONEGAI
        skillCounts.put("Ánh sáng của Đảng", 5); // Corresponds to TOU_NO_HIKARI
        skillCounts.put("Con đường vận mệnh", 5); // Corresponds to UNMEI_NO_MICHI

        // Maze container
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(mazePanel);
        frame.add(container, BorderLayout.CENTER);

        // Movement control panel
        JPanel movePanel = new JPanel(new GridBagLayout());
        movePanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();

        JButton up = createArrowButton("↑");
        JButton down = createArrowButton("↓");
        JButton left = createArrowButton("←");
        JButton right = createArrowButton("→");

        // Add action listeners for movement
        up.addActionListener(e -> mazePanel.movePlayer(MazeEnv.Action.UP));
        down.addActionListener(e -> mazePanel.movePlayer(MazeEnv.Action.DOWN));
        left.addActionListener(e -> mazePanel.movePlayer(MazeEnv.Action.LEFT));
        right.addActionListener(e -> mazePanel.movePlayer(MazeEnv.Action.RIGHT));

        c.gridx = 1; c.gridy = 0;
        movePanel.add(up, c);
        c.gridx = 0; c.gridy = 1;
        movePanel.add(left, c);
        c.gridx = 1; c.gridy = 1;
        movePanel.add(down, c);
        c.gridx = 2; c.gridy = 1;
        movePanel.add(right, c);

        frame.add(movePanel, BorderLayout.EAST);

        // Skill panel
        JPanel skillPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        skillPanel.setOpaque(false);
        skillPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        skillPanel.add(createSkillButtonWithIcon("Thiên lý nhãn", "Icon/Thien_Ly_Nhan.jpg"));
        skillPanel.add(createSkillButtonWithIcon("Slime thông thái", "Icon/Slime_Thong_Thai.jpg"));
        skillPanel.add(createSkillButtonWithIcon("Ánh sáng của Đảng", "Icon/Anh_Sang.jpg"));
        skillPanel.add(createSkillButtonWithIcon("Con đường vận mệnh", "Icon/Con_Duong.jpg"));

        frame.add(skillPanel, BorderLayout.SOUTH);

        // Settings button
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topLeftPanel.setOpaque(false);
        JButton settingBtn = new JButton("⚙");
        JPopupMenu settingMenu = new JPopupMenu();
        
        JMenuItem retryItem = new JMenuItem("Retry");
        retryItem.addActionListener(e -> mazePanel.generateNewMaze());
        settingMenu.add(retryItem);
        
        settingMenu.add(new JMenuItem("Load"));
        settingMenu.add(new JMenuItem("Save"));
        settingMenu.add(new JMenuItem("Home"));
        
        settingBtn.addActionListener(e -> settingMenu.show(settingBtn, 0, settingBtn.getHeight()));
        topLeftPanel.add(settingBtn);
        frame.add(topLeftPanel, BorderLayout.NORTH);

        // Resize handler
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                mazePanel.adjustScaleToFit();
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        mazePanel.requestFocusInWindow();
    }

    private JButton createArrowButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(70, 70));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        return btn;
    }

    private JPanel createSkillButtonWithIcon(String skillName, String iconFileName) {
        JPanel panel = new JPanel(new BorderLayout());
        ImageIcon icon = createRoundedIcon(iconFileName, 64);

        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(80, 80));
        if (icon != null) btn.setIcon(icon);
        btn.setToolTipText(skillName);

        JLabel countLabel = new JLabel("x" + skillCounts.get(skillName), SwingConstants.CENTER);
        skillLabels.put(skillName, countLabel);

        btn.addActionListener(e -> {
            int count = skillCounts.getOrDefault(skillName, 0);
            if (count > 0) {
                mazePanel.useSkill(skillName);
                skillCounts.put(skillName, count - 1);
                countLabel.setText("x" + (count - 1));
            } else {
                JOptionPane.showMessageDialog(frame, "Bạn đã hết kỹ năng: " + skillName);
            }
        });

        panel.add(btn, BorderLayout.CENTER);
        panel.add(countLabel, BorderLayout.SOUTH);
        return panel;
    }

    private ImageIcon createRoundedIcon(String path, int size) {
        try {
            BufferedImage original = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/mazeai/" + path));
            Image scaled = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = rounded.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();
            return new ImageIcon(rounded);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh: " + path);
            return null;
        }
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }
}