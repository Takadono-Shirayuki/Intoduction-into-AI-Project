package mazeinterface;

import javax.swing.*;
import mazeinterface.mazecontrol.ColorShiftButton;
import mazeinterface.mazecontrol.ShadowOverlay;

import java.awt.*;

public class MainForm extends JFrame {
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/MainBackground.jpg";  // Đường dẫn đến ảnh nền

    public MainForm() {
        // Khởi tạo cửa sổ JFrame cho menu với tiêu đề và các cấu hình cơ bản
        setUndecorated(true);  // Ẩn thanh tiêu đề của cửa sổ
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Khi đóng cửa sổ, ứng dụng sẽ thoát
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Mở rộng cửa sổ ra toàn màn hình
        setResizable(false);  // Cấm thay đổi kích thước cửa sổ

        JPanel backgroundPanel = new JPanel();  // Tạo một JPanel để chứa các thành phần giao diện
        // Tạo một JPanel với hình nền
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
            Image bgImage = bgIcon.getImage();
            backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);  // Gọi phương thức paintComponent của JPanel gốc
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);  // Vẽ hình nền lên JPanel
                }
            };
            setContentPane(backgroundPanel);  // Đặt JPanel làm nội dung chính của cửa sổ JFrame
            getContentPane().setLayout(null);  // Đặt layout của JFrame là null để tự do định vị các thành phần
        } catch (Exception e) {
            System.err.println("Không thể tải background");
        }        

        // Cấu hình font và màu sắc cho các nút
        Font btnFont = new Font("SansSerif", Font.BOLD, 24);  // Tạo font cho các nút với kiểu chữ SansSerif, in đậm, cỡ 24
        Color btnStart = new Color(255, 105, 180);  // Màu bắt đầu của gradient (hồng sáng)
        Color btnEnd = new Color(128, 0, 128);  // Màu kết thúc của gradient (tím đậm)

        // Vị trí và kích thước của các nút
        int x = 620;  // Vị trí X của các nút
        int yStart = 300;  // Vị trí Y bắt đầu của nút đầu tiên
        Dimension btnSize = new Dimension(300, 50);  // Kích thước của các nút
        int spacing = 70;  // Khoảng cách giữa các nút

        // Thêm các nút vào backgroundPanel
        ColorShiftButton startButton = new ColorShiftButton("Chơi", btnFont, Color.WHITE, btnStart, btnEnd, btnSize);  // Tạo nút "Chơi" với màu sắc và kích thước đã định nghĩa
        ColorShiftButton continueButton = new ColorShiftButton("Chơi tiếp", btnFont, Color.WHITE, btnStart, btnEnd, btnSize);  // Tạo nút "Chơi tiếp" với màu sắc và kích thước đã định nghĩa
        ColorShiftButton hellModeButton = new ColorShiftButton("Chế độ địa ngục", btnFont, Color.WHITE, btnStart, btnEnd, btnSize);  // Tạo nút "Chế độ địa ngục" với màu sắc và kích thước đã định nghĩa
        ColorShiftButton instructionButton = new ColorShiftButton("Hướng dẫn", btnFont, Color.WHITE, btnStart, btnEnd, btnSize);  // Tạo nút "Hướng dẫn" với màu sắc và kích thước đã định nghĩa
        ColorShiftButton exitButton = new ColorShiftButton("Thoát", btnFont, Color.WHITE, btnStart, btnEnd, btnSize);  // Tạo nút "Thoát" với màu sắc và kích thước đã định nghĩa

        // Đặt vị trí cho các nút
        startButton.setLocation(new Point(x, yStart + spacing * 0));  // Đặt vị trí cho nút "Chơi"
        continueButton.setLocation(new Point(x, yStart + spacing * 1));  // Đặt vị trí cho nút "Chơi tiếp"
        hellModeButton.setLocation(new Point(x, yStart + spacing * 2));  // Đặt vị trí cho nút "Chế độ địa ngục"
        instructionButton.setLocation(new Point(x, yStart + spacing * 3));  // Đặt vị trí cho nút "Hướng dẫn"
        exitButton.setLocation(new Point(x, yStart + spacing * 4));  // Đặt vị trí cho nút "Thoát"

        // Đặt sự kiện cho các nút
        startButton.addActionListener(e -> startGame(false));  // Khi nhấn nút "Chơi", gọi hàm startGame với tham số false
        continueButton.addActionListener(e -> startGame(true));  // Khi nhấn nút "Chơi tiếp", gọi hàm startGame với tham số true
        hellModeButton.addActionListener(e -> startHellMode());  // Khi nhấn nút "Chế độ địa ngục", gọi hàm startHellMode
        instructionButton.addActionListener(e -> new InstructionWindow(this).setVisible(true));  // Khi nhấn nút "Hướng dẫn", mở cửa sổ hướng dẫn
        exitButton.addActionListener(e -> System.exit(0));  // Khi nhấn nút "Thoát", thoát ứng dụng
        
        // Thêm các nút vào backgroundPanel
        backgroundPanel.add(startButton);  // Thêm nút "Chơi" vào JPanel
        backgroundPanel.add(continueButton);  // Thêm nút "Chơi tiếp" vào JPanel
        backgroundPanel.add(hellModeButton);  // Thêm nút "Chế độ địa ngục" vào JPanel
        backgroundPanel.add(instructionButton);  // Thêm nút "Hướng dẫn" vào JPanel
        backgroundPanel.add(exitButton);  // Thêm nút "Thoát" vào JPanel
        
        // Hiển thị giao diện menu
        setVisible(true);
    }

    // Phương thức startGame để bắt đầu trò chơi mới hoặc tiếp tục trò chơi
    private void startGame(boolean continueGame) {
        // Tạo lớp phủ mờ dần khi bắt đầu trò chơi
        ShadowOverlay shadowOverlay = new ShadowOverlay(this, 500, 0, ShadowOverlay.MIST_FALL);
        shadowOverlay.setVisible(true);  // Hiển thị lớp phủ mờ dần

        int mazeSize = 30;  // Kích thước mê cung
    
        // Nếu tiếp tục trò chơi, lấy trạng thái đã lưu
        if (continueGame) {
            // Có thể thêm logic để tiếp tục trò chơi từ trạng thái đã lưu
            new ShadowOverlay(new GameForm(mazeSize), 500, 1000, ShadowOverlay.MIST_RISE).setVisible(true);  // Tạo lớp phủ sáng dần cho trò chơi tiếp tục
        } else {
            // Tạo trò chơi mới
            new ShadowOverlay(new GameForm(mazeSize), 500, 1000, ShadowOverlay.MIST_RISE).setVisible(true);  // Tạo lớp phủ sáng dần cho trò chơi mới
        }
        setVisible(false);  // Ẩn cửa sổ menu
    }

    // Phương thức startHellMode để bắt đầu chế độ địa ngục với độ khó cao hơn
    private void startHellMode() {
        // Có thể thêm logic để bắt đầu chế độ địa ngục
    }
}
