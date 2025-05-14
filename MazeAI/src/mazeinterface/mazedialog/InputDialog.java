package mazeinterface.mazedialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import mazeinterface.mazecontrol.TransparentButton;
public class InputDialog extends JDialog {
    public String returnValue = null;
    
    private Boolean exitOnClose = true; // Biến để xác định có đóng JFrame cha hay không
    public InputDialog(JFrame parent, String displayText, Dimension size) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (exitOnClose) {
                    System.exit(0); // Đóng ứng dụng nếu exitOnClose là true
                }
            }
        });
        setSize(parent.getWidth(), parent.getHeight());

        // Tạo content pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setOpaque(false); // Đặt nền trong suốt
        contentPane.setBackground(new Color(0, 0, 0, 120)); // Nền trong suốt

        // Thêm JLabel để hiển thị displayText
        JLabel displayTextLabel = new JLabel(displayText);
        displayTextLabel.setForeground(Color.WHITE);
        displayTextLabel.setHorizontalAlignment(JLabel.CENTER);
        displayTextLabel.setVerticalAlignment(JLabel.CENTER);
        displayTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayTextLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        displayTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        displayTextLabel.setLocation((getWidth() - displayTextLabel.getWidth()) / 2, parent.getHeight() / 2 - 120); // Đặt vị trí của JLabel
        contentPane.add(displayTextLabel);

        // Thêm JTextField để nhập liệu
        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.BOLD, 20));
        inputField.setForeground(Color.WHITE);
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setBorder(null); // Bỏ viền
        inputField.setOpaque(false); // Đặt nền trong suốt
        inputField.setSize(size); // Kích thước của JTextField
        inputField.setLocation((getWidth() - inputField.getWidth()) / 2, parent.getHeight() / 2 - 60); // Đặt vị trí của JTextField
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    returnValue = inputField.getText(); // Lưu lại giá trị nhập vào
                    exitOnClose = false; // Đặt biến để không đóng JFrame cha
                    dispose(); // Đóng dialog chính
                }
            }
        });
        contentPane.add(inputField);

        // Thêm nút xác nhận
        TransparentButton confirmButton = new TransparentButton("Xác nhận", new Font("Arial", Font.BOLD, 20), Color.WHITE, size);
        confirmButton.setLocation((getWidth() - confirmButton.getWidth()) / 2, parent.getHeight() / 2); // Đặt vị trí của nút
        confirmButton.addActionListener(e -> {
            returnValue = inputField.getText(); // Lưu lại giá trị nhập vào
            exitOnClose = false; // Đặt biến để không đóng JFrame cha
            dispose(); // Đóng dialog chính
        });
        contentPane.add(confirmButton);

        add(contentPane); // Thêm content pane vào dialog
        setVisible(true); // Hiển thị dialog
    }
}
