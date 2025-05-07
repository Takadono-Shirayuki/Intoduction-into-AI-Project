package mazeinterface.mazecontrol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import mazenv.Pair;
import mazenv.MazeEnv.Buff;

public class SkillCard extends JPanel {
    private static final String SENRIGAN_ICON = "Icon/Senrigan.jpg";
    private static final String SLIME_SAN_ONEGAI_ICON = "Icon/SlimeSanOnegai.jpg";
    private static final String TOU_NO_HIKARI_ICON = "Icon/TouNoHikari.jpg";
    private static final String UNMEI_NO_MICHI_ICON = "Icon/UnmeiNoMichi.jpg";

    private int skillBuff; // Biến để lưu kỹ năng đã chọn
    private SkillDialog parent;
    /**
     * Khởi tạo SkillCard với SkillDialog cha và kỹ năng đã chọn.
     * @param parent SkillDialog cha để hiển thị SkillCard
     * @param skillBuff Mã kỹ năng
     */
    public SkillCard(SkillDialog parent, int skillBuff) {
        super(new BorderLayout());
        backgroundImage = new ImageIcon(getClass().getResource("/mazeai/Icon/SkillCard.jpg")).getImage(); // Tải ảnh nền
        if (backgroundImage == null) {
            System.err.println("Không thể tải ảnh nền cho SkillCard");
        }
        this.parent = parent; // Lưu tham chiếu đến SkillDialog
        this.skillBuff = skillBuff; // Lưu kỹ năng đã chọn

        // Lấy đường dẫn đến icon và tài liệu mô tả kỹ năng dựa trên mã kỹ năng
        String iconPath = ""; // Đường dẫn đến icon kỹ năng
        switch (skillBuff) {
            case Buff.SENRIGAN:
                iconPath = SENRIGAN_ICON;
                break;
            case Buff.SLIME_SAN_ONEGAI:
                iconPath = SLIME_SAN_ONEGAI_ICON;
                break;
            case Buff.TOU_NO_HIKARI:
                iconPath = TOU_NO_HIKARI_ICON;
                break;
            case Buff.UNMEI_NO_MICHI:
                iconPath = UNMEI_NO_MICHI_ICON; 
                break;
            default:
                break;
        }
        Pair<String, String> doc = Buff.getBuffInfo(skillBuff); // Lấy thông tin kỹ năng từ mã kỹ năng

        // Tạo icon kỹ năng 
        ImageIcon icon = createRoundedIcon(iconPath, 100);
        JLabel iconLabel = new JLabel(icon);
        // Đặt sự kiện cho iconLabel 
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt khoảng cách viền
        iconLabel.setOpaque(true); // Để có thể thấy màu nền
        iconLabel.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền trong suốt
        iconLabel.setPreferredSize(new java.awt.Dimension(200, 120)); // Đặt kích thước cho iconLabel
        iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Click_SkillCard();
            }
        });
        iconLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay

        // Tạo label tên kỹ năng
        JLabel skillName = new JLabel(doc.getItem1(), SwingConstants.CENTER);
        skillName.setForeground(Color.WHITE); // Đặt màu chữ (tuỳ chọn)
        skillName.setFont(new Font("Arial", Font.BOLD, 15)); // Tuỳ chỉnh kiểu chữ
        // Đặt sự kiện cho skillName
        skillName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt khoảng cách viền
        skillName.setOpaque(true); // Để có thể thấy màu nền
        skillName.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền trong suốt
        skillName.setPreferredSize(new java.awt.Dimension(200, 80)); // Đặt kích thước cho nameLabel
        skillName.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Click_SkillCard();
            }
        });
        skillName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay

        // Tạo TextArea mô tả kỹ năng
        JTextArea description = new JTextArea(doc.getItem2());
        description.setEditable(false); // Không cho phép chỉnh sửa
        description.setLineWrap(true); // Tự động xuống dòng
        description.setWrapStyleWord(true); // Chỉ xuống dòng tại các từ
        description.setForeground(Color.WHITE); // Đặt màu chữ (tuỳ chọn)
        description.setFont(new Font("Arial", Font.BOLD, 15)); // Tuỳ chỉnh kiểu chữ
        // Đặt sự kiện cho description
        description.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Đặt khoảng cách viền    
        description.setOpaque(true); // Để có thể thấy màu nền
        description.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền trong suốt
        description.setPreferredSize(new java.awt.Dimension(200, 280)); // Đặt kích thước cho descriptionLabel
        description.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Click_SkillCard();
            }
        });
        description.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Đặt con trỏ chuột thành hình bàn tay
        
        // Thêm vào skillCard
        add(iconLabel, BorderLayout.NORTH); // Đặt nút lên trên
        add(skillName, BorderLayout.CENTER); // Đặt tên ở giữa
        add(description, BorderLayout.SOUTH); // Đặt mô tả bên dưới
    }

    private Image backgroundImage;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    /** 
     * Xử lý sự kiện khi nhấn vào skillCard.<p>
     * Khiến viền của skillCard sáng lên và chuyển thông tin cho parent.
     */
    private void Click_SkillCard() {
        // Xử lý sự kiện khi nhấn vào skillCard
        // Khiến viền của skillCard sáng lên
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Thay đổi màu viền

        // Chuyển thông tin cho parent
        parent.Click_SkillCard(skillBuff); // Gọi phương thức Click_SkillCard trong SkillDialog
    }

    /**
     * Đặt lại viền của skillCard về mặc định.
     */
    public void resetBorder() {
        // Đặt lại viền về mặc định
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Đặt viền về mặc định
    }

    /**
     * Tạo một ImageIcon với hình tròn từ đường dẫn ảnh.
     * @param path Đường dẫn đến ảnh
     * @param size Kích thước của ảnh
     * @return ImageIcon với hình tròn
     */
    private ImageIcon createRoundedIcon(String path, int size) {
        try {
            BufferedImage original = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/mazeai/" + path));
            Image scaled = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = rounded.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();
            return new ImageIcon(rounded);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh: " + path);
            return null;
        }
    }

    /**
     * Lấy mã kỹ năng đã chọn.
     * @return Mã kỹ năng
     */
    public int getSkillBuff() {
        return skillBuff;
    }
}
