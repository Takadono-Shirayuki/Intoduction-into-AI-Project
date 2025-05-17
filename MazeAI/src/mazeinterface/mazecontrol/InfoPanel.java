package mazeinterface.mazecontrol;

import java.awt.*;
import javax.swing.*;

import game.Main;
/**
 * Class InfoPanel hiển thị các thông tin ở bên trái mê cung 
 * Các thông tin hiển thị gồm số tầng (level) hiện tại, số bước đến khi reset, tỉ lệ nhận buff
 * Hiện tại các số đang để cố định, chưa có bộ đếm tự động
 */
public class InfoPanel extends JPanel {

    public InfoPanel() {
        Main.setInfoPanel(this);
        
        // Kích cỡ panel
        setPreferredSize(new Dimension(300, 900));

        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Làm nền trong suốt
        setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel floorLabel = new JLabel("Tầng hiện tại: 1");
        JLabel stepsLabel = new JLabel("Số bước còn lại: 30");
        JLabel buffLabel = new JLabel("Tỉ lệ nhận buff: 25%");

        // Định dạng
        Font labelFont = new Font("SansSerif", Font.BOLD, 25);
        floorLabel.setFont(labelFont);
        stepsLabel.setFont(labelFont);
        buffLabel.setFont(labelFont);

        // Màu sắc
        Color textColor = Color.WHITE;
        floorLabel.setForeground(textColor);
        stepsLabel.setForeground(textColor);
        buffLabel.setForeground(textColor);

        // Khoảng cách giữa các dòng
        add(floorLabel);
        add(Box.createVerticalStrut(25));
        add(stepsLabel);
        add(Box.createVerticalStrut(25));
        add(buffLabel);
    }
}
