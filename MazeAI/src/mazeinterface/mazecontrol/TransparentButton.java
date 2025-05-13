package mazeinterface.mazecontrol;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Tạo nút trong suốt với viền tròn
 * Khi di chuột vào nút, viền sẽ dày lên
 * Khi không di chuột vào nút, viền sẽ mỏng lại
 */
public class TransparentButton extends JButton {
    private int borderWidth = 1; // Độ dày viền
    public TransparentButton(String text, Font font, Color textColor, java.awt.Dimension size) {
        super(text);
        setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        setForeground(textColor); // Màu chữ
        setFont(font); // Font chữ
        setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        setVerticalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        setContentAreaFilled(false); // Làm trong suốt khu vực nội dung
        setFocusPainted(false); // Không vẽ viền khi có focus
        setBorderPainted(false); // Không vẽ viền
        setSize(size); // Kích thước nút
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay
        
        // Hiệu ứng khi di chuột vào
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                borderWidth = 3;
                repaint(); // Vẽ lại nút
            }

            @Override
            public void mouseExited(MouseEvent e) {
                borderWidth = 1;
                repaint(); // Vẽ lại nút
            }
        });
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Đặt độ dày viền
        g2.setStroke(new BasicStroke(borderWidth));
        g2.setColor(Color.WHITE); // Màu viền
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4,getHeight(), getHeight()); // Vẽ viền tròn
    }
}
