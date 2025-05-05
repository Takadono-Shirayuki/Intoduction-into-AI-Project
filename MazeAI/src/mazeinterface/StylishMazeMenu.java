package mazeinterface;

import javax.swing.*;
import java.awt.*;

public class StylishMazeMenu extends JFrame {

    private Image backgroundImage;

    public StylishMazeMenu() {
        // Khởi tạo cửa sổ JFrame cho menu với tiêu đề và các cấu hình cơ bản
        setTitle("Maze Game Menu");  // Đặt tiêu đề cho cửa sổ game là "Maze Game Menu"
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Khi đóng cửa sổ, ứng dụng sẽ thoát
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Mở rộng cửa sổ ra toàn màn hình
        setResizable(false);  // Cấm thay đổi kích thước cửa sổ

        // Tải ảnh nền cho menu, nếu không có thì dùng màu nền mặc định
        try {
            backgroundImage = new ImageIcon("MazeImage/Maze2.jpg").getImage();  // Tải ảnh nền từ file
        } catch (Exception e) {
            backgroundImage = null;  // Nếu không tìm thấy ảnh, gán giá trị null cho backgroundImage
            System.out.println("Không tìm thấy ảnh nền, sử dụng màu nền thay thế");  // In thông báo khi không tìm thấy ảnh
        }

        // Tạo JPanel với hình nền và xử lý vẽ lại giao diện
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Gọi phương thức paintComponent của JPanel gốc
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);  // Vẽ ảnh nền
                } else {
                    g.setColor(new Color(50, 50, 100));  // Nếu không có ảnh nền, dùng màu nền xanh dương
                    g.fillRect(0, 0, getWidth(), getHeight());  // Vẽ màu nền lên toàn bộ JPanel
                }
            }
        };
        backgroundPanel.setLayout(null);  // Đặt layout của JPanel là null để tự do định vị các thành phần
        setContentPane(backgroundPanel);  // Đặt JPanel làm nội dung chính của cửa sổ JFrame

        // Cấu hình font và màu sắc cho các nút
        Font btnFont = new Font("SansSerif", Font.BOLD, 24);  // Tạo font cho các nút với kiểu chữ SansSerif, in đậm, cỡ 24
        Color btnStart = new Color(255, 105, 180);  // Màu bắt đầu của gradient (hồng sáng)
        Color btnEnd = new Color(128, 0, 128);  // Màu kết thúc của gradient (tím đậm)

        // Vị trí và kích thước của các nút
        int x = 150;  // Vị trí X của các nút
        int yStart = 200;  // Vị trí Y bắt đầu của nút đầu tiên
        int btnWidth = 300;  // Chiều rộng của các nút
        int btnHeight = 50;  // Chiều cao của các nút
        int spacing = 70;  // Khoảng cách giữa các nút

        // Thêm các nút vào backgroundPanel
        addButton(backgroundPanel, "Chơi", x, yStart + spacing * 0, btnWidth, btnHeight, btnFont, btnStart, btnEnd, e -> startGame(false));  // Nút "Chơi" sẽ gọi hàm startGame(false) khi nhấn
        addButton(backgroundPanel, "Chơi tiếp", x, yStart + spacing * 1, btnWidth, btnHeight, btnFont, btnStart, btnEnd, e -> startGame(true));  // Nút "Chơi tiếp" sẽ gọi hàm startGame(true) khi nhấn
        addButton(backgroundPanel, "Chế độ địa ngục", x, yStart + spacing * 2, btnWidth, btnHeight, btnFont, btnStart, btnEnd, e -> startHellMode());  // Nút "Chế độ địa ngục" gọi hàm startHellMode() khi nhấn
        
        // Nút Hướng dẫn với action listener rõ ràng
        addButton(backgroundPanel, "Hướng dẫn", x, yStart + spacing * 3, btnWidth, btnHeight, 
        btnFont, btnStart, btnEnd, e -> new InstructionWindow(this).setVisible(true));  // Mở cửa sổ hướng dẫn khi nhấn vào nút

        // Nút Thoát sẽ thoát ứng dụng khi nhấn
        addButton(backgroundPanel, "Thoát", x, yStart + spacing * 4, btnWidth, btnHeight, btnFont, btnStart, btnEnd, e -> System.exit(0));  // Thoát ứng dụng khi nhấn vào nút

        // Hiển thị giao diện menu
        setVisible(true);
    }

    // Phương thức addButton để tạo và thêm nút vào JPanel
    private void addButton(JPanel panel, String text, int x, int y, int w, int h, 
                         Font font, Color colorStart, Color colorEnd, 
                         java.awt.event.ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();  // Tạo đối tượng Graphics2D để vẽ
                GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);  // Tạo gradient màu cho nút
                g2.setPaint(gp);  // Đặt gradient màu cho nút
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);  // Vẽ nút với các góc tròn
                g2.setColor(Color.WHITE);  // Đặt màu chữ là trắng
                g2.setFont(getFont());  // Sử dụng font đã cấu hình cho nút
                FontMetrics fm = g2.getFontMetrics();  // Lấy thông tin về font để căn chỉnh văn bản
                int stringWidth = fm.stringWidth(getText());  // Lấy độ rộng của văn bản
                int stringHeight = fm.getAscent();  // Lấy chiều cao của văn bản
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 4);  // Vẽ văn bản căn giữa trên nút
                g2.dispose();  // Giải phóng tài nguyên của đối tượng Graphics2D
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ border cho nút
            }
        };
        
        button.setBounds(x, y, w, h);  // Đặt vị trí và kích thước cho nút
        button.setOpaque(false);  // Không sử dụng màu nền cho nút
        button.setContentAreaFilled(false);  // Không tô màu cho khu vực nội dung của nút
        button.setBorderPainted(false);  // Không vẽ viền cho nút
        button.setFocusPainted(false);  // Không vẽ hiệu ứng focus cho nút khi có sự kiện
        button.setFont(font);  // Đặt font cho nút

        if (action != null) {
            button.addActionListener(action);  // Thêm hành động cho nút nếu có
        }

        panel.add(button);  // Thêm nút vào JPanel
    }

    // Phương thức startGame để bắt đầu trò chơi mới hoặc tiếp tục trò chơi
    private void startGame(boolean continueGame) {
        int mazeSize = 30;  // Kích thước mê cung
        int lightSize = 5;  // Kích thước ánh sáng (số ô sáng trong mê cung)
        
        if (continueGame) {
            System.out.println("Tiếp tục game...");  // Tiếp tục trò chơi đã lưu
            // Có thể thêm logic để tiếp tục trò chơi từ trạng thái đã lưu
            new MazeInterface(mazeSize, lightSize);
        } else {
            System.out.println("Bắt đầu game mới...");  // Bắt đầu trò chơi mới
            new MazeInterface(mazeSize, lightSize);
        }
    }

    // Phương thức startHellMode để bắt đầu chế độ địa ngục với độ khó cao hơn
    private void startHellMode() {
        int mazeSize = 50;  // Kích thước mê cung lớn hơn trong chế độ địa ngục
        int lightSize = 3;  // Số ô ánh sáng ít hơn trong chế độ địa ngục
        
        System.out.println("Bắt đầu chế độ địa ngục...");  // In ra thông báo chế độ địa ngục
        new MazeInterface(mazeSize, lightSize);  // Khởi tạo trò chơi với độ khó cao hơn
    }

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StylishMazeMenu menu = new StylishMazeMenu();  // Tạo đối tượng menu
            menu.setVisible(true);  // Hiển thị menu
        });
    }
}
