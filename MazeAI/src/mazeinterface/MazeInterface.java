package mazeinterface;

import javax.swing.*;

import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazecontrol.SkillDialog;

import java.awt.*;
import mazenv.*;
import mazenv.MazeEnv.Buff;

public class MazeInterface {
    private JFrame frame;
    private MazePanel mazePanel;

    /**
     * Khởi tạo giao diện chính của trò chơi mê cung
     * @param mazeSize Kích thước mê cung
     * @param lightSize Kích thước ánh sáng
     */
    public MazeInterface(int mazeSize, int lightSize) {
        // Khởi tạo JFrame
        frame = new JFrame("\uD83C\uDF00 Mê cung vô vọng - ver 1.2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(true); // Bỏ bỏ thanh tiêu đề
        frame.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
        
        // Tạo một JPanel với hình nền
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

        // Khởi tạo MazePanel và thêm vào JFrame
        MazeEnv mazeEnv = new MazeEnv(mazeSize, 15, 50, Buff.SLIME_STEP, Buff.TOU_NO_HIKARI_OBS);
        mazePanel = new MazePanel(mazeSize, mazeEnv);

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(mazePanel);
        container.setPreferredSize(new Dimension(mazePanel.getWidth(), mazePanel.getHeight()));
        frame.add(container, BorderLayout.CENTER);
        
        // Tạo một JPanel cho các nút điều khiển
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);

        // Tạo nút chọn kỹ năng
        JButton skillMenuBtn = new JButton("\uD83C\uDFAF Skill");
        skillMenuBtn.setPreferredSize(new Dimension(120, 40));
        skillMenuBtn.addActionListener(e -> openSkillDialog());
        topLeftPanel.add(skillMenuBtn);

        // Tạo nút cài đặt
        JButton settingBtn = new JButton("⚙");
        settingBtn.setPreferredSize(new Dimension(50, 40));
        JPopupMenu settingMenu = new JPopupMenu();
        settingMenu.add(new JMenuItem("Retry"));
        settingMenu.add(new JMenuItem("Load"));
        settingMenu.add(new JMenuItem("Save"));
        settingMenu.add(new JMenuItem("Home"));
        settingBtn.addActionListener(e -> settingMenu.show(settingBtn, 0, settingBtn.getHeight()));
        topLeftPanel.add(settingBtn);

        frame.add(topLeftPanel, BorderLayout.NORTH);

        // Thay đổi kích thước của MazePanel để vừa với kích thước của JFrame
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                mazePanel.adjustScaleToFit();
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        mazePanel.requestFocusInWindow();
    }

    private void openSkillDialog() {
        SkillDialog skillDialog = new SkillDialog(frame);
        int selectedSkill = skillDialog.selectedSkill; // Lấy kỹ năng đã chọn từ SkillDialog
        mazePanel.useSkill(selectedSkill); // Sử dụng kỹ năng trong MazePanel
        skillDialog.dispose(); // Đóng SkillDialog
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }
}
