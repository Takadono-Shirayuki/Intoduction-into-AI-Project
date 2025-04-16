import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Nhập kích thước mê cung
            int rows = Integer.parseInt(JOptionPane.showInputDialog("Nhập số hàng:"));
            int cols = Integer.parseInt(JOptionPane.showInputDialog("Nhập số cột:"));
            int lightSize = 3;

            // Tạo cửa sổ chính
            JFrame frame = new JFrame("🌀 Mê cung vô vọng - Phóng to tự do");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Tạo panel mê cung
            JPanel container = new JPanel(new GridBagLayout());
            MazePanel mazePanel = new MazePanel(rows, cols, lightSize);
            container.add(mazePanel);
            frame.add(container, BorderLayout.CENTER);

            // Panel điều khiển di chuyển
            JPanel movePanel = new JPanel(new GridLayout(2, 3));
            JButton up = new JButton("↑");
            JButton down = new JButton("↓");
            JButton left = new JButton("←");
            JButton right = new JButton("→");

            movePanel.add(new JLabel());
            movePanel.add(up);
            movePanel.add(new JLabel());
            movePanel.add(left);
            movePanel.add(down);
            movePanel.add(right);

            up.addActionListener(e -> mazePanel.movePlayer(0, -1));
            down.addActionListener(e -> mazePanel.movePlayer(0, 1));
            left.addActionListener(e -> mazePanel.movePlayer(-1, 0));
            right.addActionListener(e -> mazePanel.movePlayer(1, 0));
            frame.add(movePanel, BorderLayout.NORTH);

            // Panel kỹ năng
            JButton toggleView = new JButton("🗺 Xem bản đồ");
            toggleView.addActionListener(e -> {
                mazePanel.toggleFullView();
                toggleView.setText(mazePanel.isFullView() ? "❌ Tắt bản đồ" : "🗺 Xem bản đồ");
            });

            JButton skillBtn = new JButton("🎯 Kỹ năng");
            JPopupMenu skillMenu = new JPopupMenu();
            JMenuItem skill1 = new JMenuItem("Thiên lý nhãn");
            JMenuItem skill2 = new JMenuItem("Slime thông thái");
            JMenuItem skill3 = new JMenuItem("Ánh sáng của Đảng");
            JMenuItem skill4 = new JMenuItem("Con đường vận mệnh");

            skill1.addActionListener(e -> mazePanel.useSkill("Thiên lý nhãn"));
            skill2.addActionListener(e -> mazePanel.useSkill("Slime thông thái"));
            skill3.addActionListener(e -> mazePanel.useSkill("Ánh sáng của Đảng"));
            skill4.addActionListener(e -> mazePanel.useSkill("Con đường vận mệnh"));

            skillMenu.add(skill1);
            skillMenu.add(skill2);
            skillMenu.add(skill3);
            skillMenu.add(skill4);
            skillBtn.addActionListener(e -> skillMenu.show(skillBtn, 0, skillBtn.getHeight()));

            JButton nextBtn = new JButton("🔁 Qua màn");
            nextBtn.addActionListener(e -> {
                mazePanel.generateNewMaze();
                toggleView.setText("🗺 Xem bản đồ");
            });

            JPanel controlPanel = new JPanel(new FlowLayout());
            controlPanel.add(skillBtn);
            controlPanel.add(nextBtn);
            controlPanel.add(toggleView);
            frame.add(controlPanel, BorderLayout.SOUTH);

            // Thêm listener để điều chỉnh kích thước khi cửa sổ thay đổi
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    mazePanel.adjustScaleToFit();
                }
            });

            // Hiển thị cửa sổ
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            mazePanel.requestFocusInWindow();
        });
    }
}