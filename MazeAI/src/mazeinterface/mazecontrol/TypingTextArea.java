package mazeinterface.mazecontrol;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import game.GameVariable;
import mazeobject.Command.Dialogue;

import java.awt.Color;
import java.awt.Font;

public class TypingTextArea extends JPanel {

    private JLabel displayNameLabel; // Nhãn hiển thị tên người nói
    private JTextArea contentTextArea; // Khu vực hiển thị nội dung lời thoại
    private java.util.Timer typingTimer; // Bộ đếm thời gian cho hiệu ứng gõ chữ

    private String content = ""; // Nội dung lời thoại
    public TypingTextArea() {
        setLayout(null);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0, 0, 0, 0)); // Màu nền trong suốt

        // Nhãn hiển thị tên người nói
        displayNameLabel = new JLabel("");
        displayNameLabel.setFont(new Font("Serif", Font.BOLD, 28));
        displayNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        displayNameLabel.setForeground(Color.YELLOW);
        add(displayNameLabel);

        // Khu vực hiển thị lời thoại
        contentTextArea = new JTextArea();
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setFont(new Font("Serif", Font.PLAIN, 26));
        contentTextArea.setForeground(Color.WHITE);
        contentTextArea.setEditable(false);
        contentTextArea.setBackground(Color.DARK_GRAY);
        contentTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentTextArea);
    }

    /**
     * Phương thức này dùng để tải dữ liệu mới cho lời thoại <p>
     * @param dialogue Đối tượng Dialogue chứa thông tin lời thoại
     */
    public void load(Dialogue dialogue, int typingPeriod) {
        displayNameLabel.setText(GameVariable.format(dialogue.displayName)); // Hiển thị tên người nói
        contentTextArea.setText(""); // Xóa nội dung cũ
        
        content = GameVariable.format(dialogue.content); // Lưu nội dung lời thoại

        // Tạo hiệu ứng gõ chữ
        typingTimer = new java.util.Timer();
        typingTimer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                contentTextArea.append(String.valueOf(dialogue.content.charAt(contentTextArea.getText().length()))); // Thêm ký tự vào nội dung
                if (contentTextArea.getText().length() == dialogue.content.length())
                    cancel(); // Dừng lại khi đã hết nội dung
            }
        }, 0, typingPeriod); // Thời gian giữa các ký tự
    }

    /**
     * Bỏ qua hiệu ứng gõ chữ <p>
     */
    public void skipTyping() {
        if (typingTimer != null) {
            typingTimer.cancel(); // Dừng bộ đếm thời gian
        }
        contentTextArea.setText(content); // Bỏ qua hiệu ứng gõ chữ
    }

    /**
     * Kiểm tra xem còn ký tự nào chưa gõ không <p>
     * @return true nếu còn ký tự chưa gõ, false nếu đã gõ hết
     */
    public boolean isTyping() {
        return contentTextArea.getText().length() < content.length(); // Kiểm tra xem còn ký tự nào chưa gõ không
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        displayNameLabel.setBounds(30, 10, width - 20, 40);
        contentTextArea.setBounds(10, 50, width - 20, height - 60);
    }
}