package mazeinterface.mazedialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.util.Timer;

public class ShadowOverlay extends JDialog {
    /**
     * Chế độ sáng dần
     */
    public static final int MIST_RISE = 0;
    /**
     * Chế độ tối dần
     */
    public static final int MIST_FALL = 1;

    private float deltaAlpha; // Độ thay đổi alpha mỗi 10ms
    private Boolean exitOnClose = true; // Biến để xác định có đóng JFrame cha hay không
    /**
     * Tạo một lớp phủ mờ dần cho JFrame <p>
     * Nếu chế độ là MIST_RISE, lớp phủ sẽ sáng dần từ trong suốt đến không trong suốt. <p>
     * Nếu chế độ là MIST_FALL, lớp phủ sẽ tối dần từ không trong suốt đến trong suốt. 
     * @param parent JFrame cha
     * @param fadingTime Thời gian mờ dần (ms)
     * @param delay Thời gian trễ trước khi bắt đầu mờ dần (ms)
     * @param mode Chế độ mờ dần (MIST_RISE hoặc MIST_FALL)
     */
    public ShadowOverlay(JFrame parent, float fadingTime, int delay, int mode) {
        super(parent, "Shadow Overlay",false);

        // Thiết lập các thuộc tính cho lớp phủ
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Đóng lớp phủ khi nhấn nút đóng
        setAlwaysOnTop(true); // Đặt lớp phủ luôn ở trên cùng
        setUndecorated(true); // Bỏ bỏ thanh tiêu đề
        // Kết thúc chương trình khi lớp phủ bị đóng bởi atlt + F4
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (exitOnClose) {
                    System.exit(0); // Đóng ứng dụng nếu exitOnClose là true
                }
            }
        });

        // Tính toán các thông số 
        deltaAlpha = 255f / (fadingTime / 10f); // Độ thay đổi alpha mỗi 10ms
        if (mode == MIST_RISE) {
            setBackground(new Color(0, 0, 0, 254)); // Bắt đầu với độ mờ 100% (không trong suốt)
            deltaAlpha = -deltaAlpha; // Đảo ngược độ thay đổi alpha cho chế độ mờ dần
        } else if (mode == MIST_FALL) {
            setBackground(new Color(0, 0, 0, 0)); // Bắt đầu với độ mờ 0% (trong suốt)
        } 
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent);

        // Tạo timer để điều chỉnh độ mờ
        new Timer().schedule(new java.util.TimerTask() {
            private float alpha = mode == MIST_RISE ? 254 : 0; // Giá trị alpha bắt đầu

            @Override
            public void run() {
                alpha += deltaAlpha;
                if (alpha > 0 && alpha < 254) {
                    setBackground(new Color(0, 0, 0, (int) alpha)); // Cập nhật màu nền với giá trị alpha mới
                } else {
                    cancel(); // Dừng timer khi đạt đến độ mờ mong muốn
                    dispose(); // Đóng lớp phủ
                    exitOnClose = false; // Không đóng JFrame cha khi lớp phủ bị đóng
                }
            }
        }, delay, 10); // Bắt đầu timer sau thời gian trễ và lặp lại mỗi 10ms
        setVisible(true); // Hiển thị lớp phủ
    }
    
}
