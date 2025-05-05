package mazeinterface;

import javax.swing.*;
import java.awt.*;

public class InstructionWindow extends JDialog {

    // Hàm khởi tạo của InstructionWindow, nhận đối tượng JFrame làm tham số
    public InstructionWindow(JFrame parent) {
        super(parent, "Hướng dẫn chơi game", true); // Cửa sổ hướng dẫn là modal (chỉ có thể đóng khi người dùng hoàn thành)
        initializeUI(); // Gọi phương thức khởi tạo giao diện
    }

    private void initializeUI() {
        // Panel chính với BorderLayout, khoảng cách giữa các thành phần là 10px ngang và 10px dọc
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.BLACK); // Nền của panel chính là màu đen
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 3)); // Viền màu hồng neon

        // Tiêu đề cửa sổ với font "Segoe UI", màu sắc hồng neon
        JLabel titleLabel = new JLabel("HƯỚNG DẪN CHƠI", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Cỡ chữ lớn, đậm
        titleLabel.setForeground(new Color(255, 105, 180)); // Màu hồng neon
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Thêm khoảng cách cho tiêu đề

        // Panel nội dung với GridLayout, các phần tử cách nhau 10px ngang và 15px dọc
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 15));
        contentPanel.setBackground(Color.BLACK); // Nền của panel là màu đen

        // Thêm các phần với các nội dung mô tả
        addDarkSection(contentPanel, "CÁCH CHƠI", new String[]{
            "Sử dụng phím mũi tên ↑ ↓ ← → để di chuyển", // Cách di chuyển trong game
            "Tìm đường đến đích trong thời gian ngắn nhất", // Mục tiêu của trò chơi
            "Thu thập Buff để có lợi thế" // Lợi ích của việc thu thập Buff
        });

        addDarkSection(contentPanel, "ĐIỀU KHIỂN", new String[]{
            "Người chơi: Chế độ thủ công", // Điều khiển thủ công
            "Máy chơi: AI tự động giải mê cung", // AI sẽ tự động giải mê cung
            "Kích thước: Tuỳ chỉnh độ lớn mê cung" // Cho phép người chơi thay đổi kích thước mê cung
        });

        addDarkSection(contentPanel, "BUFF", new String[]{
            "Con đường vận mệnh: Mở lối đi tắt", // Buff giúp mở lối đi tắt
            "Thiên lý nhãn: Nhìn xa 2 ô", // Buff giúp tăng tầm nhìn
            "Slime thông thái: Gợi ý đường đi" // Buff giúp gợi ý đường đi
        });

        addDarkSection(contentPanel, "DEBUFF", new String[]{
            "Về đầu: Reset vị trí ban đầu", // Debuff khiến người chơi quay lại vị trí ban đầu
            "Dịch chuyển: Teleport ngẫu nhiên", // Debuff gây dịch chuyển ngẫu nhiên
            "Mù tạm thời: Giảm tầm nhìn" // Debuff giảm tầm nhìn
        });

        // Thêm vào panel chính
        mainPanel.add(titleLabel, BorderLayout.NORTH); // Thêm tiêu đề vào phần trên của cửa sổ
        mainPanel.add(new JScrollPane(contentPanel) {{
            setBorder(BorderFactory.createEmptyBorder()); // Không viền cho JScrollPane
            getViewport().setBackground(Color.BLACK); // Nền của viewport là màu đen
        }}, BorderLayout.CENTER); // Thêm nội dung vào giữa cửa sổ

        // Nút đóng cửa sổ, thay đổi màu nền khi di chuột vào
        JButton closeButton = new JButton("ĐÃ HIỂU");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font chữ lớn, đậm
        closeButton.setForeground(Color.WHITE); // Màu chữ trắng
        closeButton.setBackground(new Color(50, 50, 50)); // Màu nền nút xám đen
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2), // Viền hồng neon
            BorderFactory.createEmptyBorder(8, 30, 8, 30) // Khoảng cách trong nút
        ));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(255, 105, 180)); // Màu nền hồng neon khi di chuột vào
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(50, 50, 50)); // Quay lại màu xám khi di chuột ra
            }
        });
        closeButton.addActionListener(e -> dispose()); // Đóng cửa sổ khi nhấn nút

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK); // Nền của panel chứa nút là màu đen
        buttonPanel.add(closeButton); // Thêm nút đóng vào panel

        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Thêm panel chứa nút vào phần dưới cửa sổ

        // Cài đặt cho cửa sổ dialog
        setContentPane(mainPanel); // Đặt panel chính làm nội dung của cửa sổ
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Đóng cửa sổ khi nhấn đóng
        setSize(550, 650); // Kích thước cửa sổ
        setLocationRelativeTo(getParent()); // Đặt cửa sổ ở giữa cửa sổ cha
        setResizable(false); // Cấm thay đổi kích thước cửa sổ
    }

    // Phương thức thêm một phần với tiêu đề và danh sách mô tả vào panel
    private void addDarkSection(JPanel parent, String title, String[] items) {
        JPanel sectionPanel = new JPanel(new BorderLayout()); // Tạo panel cho mỗi phần
        sectionPanel.setBackground(Color.BLACK); // Nền của phần này là màu đen
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Thêm khoảng cách lề cho phần này

        // Tiêu đề phần
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Cỡ chữ lớn, đậm
        sectionTitle.setForeground(new Color(255, 105, 180)); // Màu chữ hồng neon
        sectionPanel.add(sectionTitle, BorderLayout.NORTH); // Đặt tiêu đề vào phần trên của panel

        // Panel chứa danh sách các mục mô tả
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 8, 8)); // Sử dụng GridLayout để căn chỉnh các mục theo hàng
        itemsPanel.setBackground(Color.BLACK); // Nền của panel là màu đen

        // Thêm từng mục mô tả vào phần này
        for (String item : items) {
            JLabel itemLabel = new JLabel("◆ " + item); // Dùng ký tự diamond làm bullet
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Cỡ chữ vừa phải
            itemLabel.setForeground(Color.WHITE); // Màu chữ trắng
            itemsPanel.add(itemLabel); // Thêm từng mục vào panel
        }

        sectionPanel.add(itemsPanel, BorderLayout.CENTER); // Đặt các mục mô tả vào giữa phần
        parent.add(sectionPanel); // Thêm phần này vào panel cha
    }
}
