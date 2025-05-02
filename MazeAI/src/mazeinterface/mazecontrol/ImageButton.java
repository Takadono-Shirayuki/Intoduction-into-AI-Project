package mazeinterface.mazecontrol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ImageButton extends JButton {
    /**
     * Tao một nút với ảnh và văn bản
     * @param imagePath Đường dẫn đến ảnh
     * @param text Văn bản hiển thị trên nút
     * @param font Font chữ
     * @param textColor Màu chữ
     * @param size Kích thước của nút
     */
    public ImageButton(String imagePath, String text, Font font, Color textColor, Dimension size) {
        super(text); // Tạo nút với văn bản
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath)); // Load ảnh
        Image scaledImage = icon.getImage().getScaledInstance(150, 60, java.awt.Image.SCALE_SMOOTH); // Thay đổi kích thước ảnh
        icon = new ImageIcon(scaledImage); // Tạo lại ImageIcon với ảnh đã thay đổi kích thước
        
        setIcon(icon); // Đặt icon cho nút
        setForeground(textColor); // Màu chữ
        setFont(font); // Font chữ
        setHorizontalTextPosition(SwingConstants.CENTER); // Căn giữa chữ
        setVerticalTextPosition(SwingConstants.CENTER); // Căn giữa chữ
        setContentAreaFilled(false); // Làm trong suốt khu vực nội dung
        setBorderPainted(false); // Loại bỏ viền
        setFocusPainted(false); // Loại bỏ đường viền khi được chọn
        setPreferredSize(new Dimension(150, 60)); // Điều chỉnh kích thước nút
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay
    }
}
