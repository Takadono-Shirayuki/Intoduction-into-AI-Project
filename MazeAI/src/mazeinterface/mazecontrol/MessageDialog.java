package mazeinterface.mazecontrol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MessageDialog extends javax.swing.JDialog {
    private int fadeDialogAlpha = 180;
    public MessageDialog(JFrame parent, String text, Dimension size) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0,0));
        
        JDialog fadeDilog = new JDialog(parent, false);
        fadeDilog.setUndecorated(true);
        fadeDilog.setBackground(new Color(0, 0, 0, 180));
        fadeDilog.setSize(parent.getWidth(), parent.getHeight());
        fadeDilog.setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa parent
        fadeDilog.setVisible(true); // Hiển thị dialog
        fadeDilog.setAlwaysOnTop(true); // Đặt dialog ở trên cùng

        JPanel contentPanel = new JPanel();
        ImageButton button = new ImageButton(
            "/mazeai/Icon/SpookyButton.jpg", 
            text, 
            new Font("Arial", Font.BOLD, 20), 
            Color.WHITE, 
            size);
        button.addActionListener(e -> {
            fadeDilog.dispose(); // Đóng dialog fade
            this.dispose(); // Đóng dialog chính
        });
        contentPanel.setLayout(new java.awt.BorderLayout());
        contentPanel.setOpaque(false); // Đảm bảo nền trong suốt
        contentPanel.add(button, BorderLayout.CENTER);
        add(contentPanel);
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa parent
        Timer timer = new Timer(30, e -> {
            fadeDialogAlpha -= 5;
            fadeDilog.setBackground(new Color(0, 0, 0, fadeDialogAlpha));
            this.setBackground(new Color(0, 0, 0, 256 - 76 * 256 / (256-fadeDialogAlpha)));
            if (fadeDialogAlpha == 0)
            {
                fadeDilog.dispose(); // Đóng dialog fade
                ((Timer) e.getSource()).stop(); // Dừng timer
            }
        });
        timer.start(); // Bắt đầu timer
        setVisible(true); // Hiển thị dialog
    }
}
