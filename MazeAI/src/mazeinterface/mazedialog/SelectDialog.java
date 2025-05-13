package mazeinterface.mazedialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mazeinterface.mazecontrol.TransparentButton;

public class SelectDialog extends JDialog{
    public int returnValue = -1;

    private Boolean exitOnClose = true; // Biến để xác định có đóng JFrame cha hay không

    public SelectDialog(JFrame parent, String displayText, String[] options) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 120));
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
        displayTextLabel.setSize(300, 50); // Kích thước của JLabel
        displayTextLabel.setLocation((getWidth() - displayTextLabel.getWidth()) / 2, parent.getHeight() / 2 - 100 - 30 * (options.length)); // Đặt vị trí của JLabel
        contentPane.add(displayTextLabel);

        // Thêm các nút lựa chọn
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            TransparentButton optionButton = new TransparentButton(option, new Font("Arial", Font.BOLD, 20), Color.WHITE, new java.awt.Dimension(600, 50));
            final int index = i; // Biến final để sử dụng trong ActionListener
            optionButton.addActionListener(e -> {
                returnValue = index; // Lưu lại giá trị lựa chọn
                exitOnClose = false; // Đặt biến để không đóng JFrame cha
                dispose(); // Đóng dialog chính
            });
            optionButton.setLocation((getWidth() - optionButton.getWidth()) / 2, displayTextLabel.getY() + displayTextLabel.getHeight() + (i * 60)); // Đặt vị trí của nút
            contentPane.add(optionButton); // Thêm nút vào dialog
        }
        
        add(contentPane); // Thêm content pane vào dialog
        setVisible(true); // Hiển thị dialog
    }
}
