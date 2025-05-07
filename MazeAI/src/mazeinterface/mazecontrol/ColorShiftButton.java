package mazeinterface.mazecontrol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ColorShiftButton extends JButton {
    private Color startColor;
    private Color endColor;
    public ColorShiftButton(String text, Font font, Color textColor, Color startColor, Color endColor, Dimension size) {
        super(text);

        setForeground(textColor); // Màu chữ
        setFont(font); // Font chữ
        setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        setVerticalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        setContentAreaFilled(false); // Làm trong suốt khu vực nội dung
        setFocusPainted(false); // Không vẽ viền khi có focus
        setBorderPainted(false); // Không vẽ viền
        setSize(size); // Kích thước nút
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay

        this.startColor = startColor; // Màu bắt đầu
        this.endColor = endColor; // Màu kết thúc
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Tạo gradient màu cho nút
        java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        ((java.awt.Graphics2D) g).setPaint(gp);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Vẽ nút với các góc tròn
        super.paintComponent(g); // Vẽ nội dung của nút
    }
    
}
