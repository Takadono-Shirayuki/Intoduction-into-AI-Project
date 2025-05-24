package mazeinterface.mazecontrol;

import java.awt.*;
import javax.swing.*;

public class AIPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel pingLabel;
    private int ping;
    private TransparentButton runButton;
    private Image backgroundImage;

    public AIPanel(int ping) {
        this.ping = ping;

        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        Dimension preferredSize = new Dimension(380, screenHeight / 3);
        setPreferredSize(preferredSize);
        setMaximumSize(preferredSize);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/mazeai/Icon/SkillCard.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền AIPanel.");
        }

        Font labelFont = new Font("SansSerif", Font.BOLD, 20);
        Color textColor = Color.WHITE;

        titleLabel = new JLabel("Kết nối model");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        pingLabel = new JLabel("Chạy mỗi " + ping + " ms");
        pingLabel.setFont(labelFont);
        pingLabel.setForeground(textColor);
        pingLabel.setAlignmentX(CENTER_ALIGNMENT);

        runButton = new TransparentButton(
            "/mazeai/Icon/SpookyButton.jpg",
            new Font("SansSerif", Font.BOLD, 16),
            Color.WHITE,
            new Dimension(200, 40)
        );
        runButton.setText("Run AI model");
        runButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        runButton.setPreferredSize(new Dimension(200, 40));
        runButton.setMaximumSize(new Dimension(200, 40));
        runButton.setAlignmentX(CENTER_ALIGNMENT);
        runButton.addActionListener(e -> {
            System.out.println("Run AI model clicked!");
        });

        // Bọc nội dung trong contentBox để căn giữa dọc
        JPanel contentBox = new JPanel();
        contentBox.setOpaque(false);
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));

        contentBox.add(titleLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(pingLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(runButton);

        // Căn giữa dọc bằng glue
        add(Box.createVerticalGlue());
        add(contentBox);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    public void setPing(int newPing) {
        this.ping = newPing;
        pingLabel.setText("Chạy mỗi " + ping + " ms");
    }

    public int getPing() {
        return ping;
    }
}
