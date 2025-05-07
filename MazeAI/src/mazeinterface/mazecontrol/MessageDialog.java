package mazeinterface.mazecontrol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.WindowAdapter;

public class MessageDialog extends javax.swing.JDialog {
    private static final String BUTTON_ICON_PATH = "/mazeai/Icon/SpookyButton.jpg";
    private int fadeDialogAlpha = 180;
    private Boolean exitOnClose = true; // Biến để xác định có đóng JFrame cha hay không
    public MessageDialog(JFrame parent, String text, Dimension size) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0,0));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (exitOnClose) {
                    System.exit(0); // Đóng ứng dụng nếu exitOnClose là true
                }
            }
        });

        // Tạo một lớp phủ mờ dần cho MessageDialog
        JDialog fadeDialog = new JDialog(parent, false);
        fadeDialog.setUndecorated(true);
        fadeDialog.setBackground(new Color(0, 0, 0, 180));
        fadeDialog.setSize(parent.getWidth(), parent.getHeight());
        fadeDialog.setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa parent
        fadeDialog.setVisible(true); // Hiển thị dialog
        fadeDialog.setAlwaysOnTop(true); // Đặt dialog ở trên cùng
        fadeDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (exitOnClose) {
                    System.exit(0); // Đóng ứng dụng nếu exitOnClose là true
                }
            }
        });

        // Tạo một JPanel để chứa nút và đặt layout cho nó
        JPanel contentPanel = new JPanel();
        ImageButton button = new ImageButton(
            BUTTON_ICON_PATH, // Đường dẫn đến icon của nút
            text, // Văn bản hiển thị trên nút
            new Font("Arial", Font.BOLD, 20), // Phông chữ 
            Color.WHITE, // Màu chữ
            size); // Kích thước của nút
        button.addActionListener(e -> {
            exitOnClose = false; // Đặt biến để không đóng JFrame cha
            fadeDialog.dispose(); // Đóng dialog fade
            this.dispose(); // Đóng dialog chính
        });
        
        // Thiết lập contentPanel
        contentPanel.setLayout(new java.awt.BorderLayout()); // Sử dụng BorderLayout để căn giữa nút
        contentPanel.setOpaque(false); // Đảm bảo nền trong suốt
        contentPanel.add(button, BorderLayout.CENTER); // Thêm nút vào contentPanel
        add(contentPanel); // Thêm contentPanel vào dialog chính

        // Tạo timer để điều chỉnh độ mờ
        Timer timer = new Timer(30, e -> {
            fadeDialogAlpha -= 5; // Giảm độ mờ mỗi lần tick
            fadeDialog.setBackground(new Color(0, 0, 0, fadeDialogAlpha)); // Cập nhật độ mờ của dialog fade
            this.setBackground(new Color(0, 0, 0, 256 - 76 * 256 / (256-fadeDialogAlpha))); // Cập nhật độ mờ của dialog chính
            if (fadeDialogAlpha <= 0) // Nếu độ mờ đã đạt đến 0
            {
                exitOnClose = false; // Đặt biến để không đóng JFrame cha
                fadeDialog.dispose(); // Đóng dialog fade
                exitOnClose = true; // Đặt biến để đóng JFrame cha
                ((Timer) e.getSource()).stop(); // Dừng timer
            }
        });
        timer.start(); // Bắt đầu timer

        setSize(parent.getWidth(), parent.getHeight()); // Đặt kích thước của dialog chính bằng kích thước của parent
        setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa parent
        setVisible(true); // Hiển thị dialog
    }
}
