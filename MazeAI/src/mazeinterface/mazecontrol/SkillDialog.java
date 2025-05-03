package mazeinterface.mazecontrol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mazenv.MazeEnv.Buff;

public class SkillDialog extends JDialog {
    public int selectedSkill = Buff.NONE; // Biến để lưu kỹ năng đã chọn
    public int tempSkill = Buff.NONE; // Biến tạm để lưu kỹ năng tạm thời
    private SkillCard skillCard[];

    /**
     * Khởi tạo SkillDialog với JFrame cha. 
     * @param frame JFrame cha để hiển thị SkillDialog
     */
    public SkillDialog(JFrame frame) {
        super(frame, "Chọn Kỹ năng", true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false); // Đảm bảo nền trong suốt

        // Tạo một khoảng trống phía trên để đẩy các nút xuống dưới
        JPanel emptySpace = new JPanel();
        emptySpace.setOpaque(false);
        emptySpace.setPreferredSize(new Dimension(1, 150)); 
        contentPanel.add(emptySpace, BorderLayout.NORTH);

        // Tạo JPanel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false); // Đảm bảo nền trong suốt
        
        // Tạo JPanel chứa các nút và căn giữa
        JPanel skillCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        skillCardPanel.setOpaque(false);
        skillCard = new SkillCard[4];
        skillCard[0] = new SkillCard(this, Buff.SENRIGAN);
        skillCard[1] = new SkillCard(this, Buff.SLIME_SAN_ONEGAI);
        skillCard[2] = new SkillCard(this, Buff.TOU_NO_HIKARI);
        skillCard[3] = new SkillCard(this, Buff.UNMEI_NO_MICHI);
        
        skillCardPanel.add(skillCard[0]);
        skillCardPanel.add(skillCard[1]);
        skillCardPanel.add(skillCard[2]);
        skillCardPanel.add(skillCard[3]);

        mainPanel.add(skillCardPanel, BorderLayout.NORTH);
        
        // Tạo JPanel chứa các nút chọn và bỏ qua
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        selectionPanel.setOpaque(false); // Đảm bảo nền trong suốt
        JButton selectButton = new ImageButton("/mazeai/Icon/SpookyButton.jpg", "Chọn", new Font("Arial", Font.BOLD, 20), Color.WHITE, new Dimension(150, 60));
        JButton skipButton = new ImageButton("/mazeai/Icon/SpookyButton.jpg", "Bỏ qua", new Font("Arial", Font.BOLD, 20), Color.WHITE, new Dimension(150, 60));
        selectionPanel.add(selectButton);
        selectButton.addActionListener(e -> {
            selectedSkill = tempSkill; // Lưu kỹ năng đã chọn
            setVisible(false); // Đóng dialog
        });
        selectionPanel.add(skipButton);
        skipButton.addActionListener(e -> {
            setVisible(false); // Đóng dialog
        });
        mainPanel.add(selectionPanel, BorderLayout.CENTER);

        contentPanel.add(mainPanel, BorderLayout.CENTER);

        add(contentPanel);
        setSize(frame.getWidth(), frame.getHeight());
        setLocationRelativeTo(frame);
    }

    /**
     * Phương thức này được gọi khi người dùng nhấp vào một kỹ năng trong SkillCard.
     * @param skillBuff Kỹ năng đã chọn
     */
    public void Click_SkillCard(int skillBuff) {
        tempSkill = skillBuff; // Lưu kỹ năng tạm thời
        for (int i = 0; i < skillCard.length; i++) {
            if (skillCard[i].getSkillBuff() != skillBuff) {
                skillCard[i].resetBorder();
            }
        }
    }
}
