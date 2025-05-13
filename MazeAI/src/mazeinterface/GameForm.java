package mazeinterface;

import javax.swing.*;

import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazedialog.MessageDialog;
import mazeinterface.mazedialog.SkillDialog;

import java.awt.*;
import mazenv.*;
import mazenv.MazeEnv.Buff;
import java.util.Timer;

public class GameForm extends JFrame {
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/GameBackground.jpg";  // Đường dẫn đến ảnh nền
    private MazePanel mazePanel;
    private SkillDialog skillDialog;
    /**
     * Khởi tạo giao diện chính của trò chơi mê cung
     * @param mazeSize Kích thước mê cung
     */
    public GameForm(int mazeSize) {
        // Khởi tạo JFrame
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true); // Bỏ bỏ thanh tiêu đề
        setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
        
        // Tạo một JPanel với hình nền
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
            Image bgImage = bgIcon.getImage();
            setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            });
            getContentPane().setLayout(new BorderLayout());
        } catch (Exception e) {
            System.err.println("Không thể tải background");
        }

        // Khởi tạo MazePanel và thêm vào JFrame
        MazeEnv mazeEnv = new MazeEnv(mazeSize, 15, 60, Buff.SLIME_STEP, Buff.TOU_NO_HIKARI_OBS);
        mazePanel = new MazePanel(mazeSize, mazeEnv, this);

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(mazePanel);
        container.setPreferredSize(new Dimension(mazePanel.getWidth(), mazePanel.getHeight()));
        add(container, BorderLayout.CENTER);
        
        // Tạo một JPanel cho các nút điều khiển
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);

        // Tạo nút cài đặt
        JButton settingBtn = new JButton("⚙");
        settingBtn.setPreferredSize(new Dimension(50, 40));
        JPopupMenu settingMenu = new JPopupMenu();
        settingMenu.add(new JMenuItem("Retry"));
        settingMenu.add(new JMenuItem("Load"));
        settingMenu.add(new JMenuItem("Save"));
        settingMenu.add(new JMenuItem("Home"));

        settingBtn.addActionListener(e -> settingMenu.show(settingBtn, 0, settingBtn.getHeight()));
        topLeftPanel.add(settingBtn);

        add(topLeftPanel, BorderLayout.NORTH);

        // Thay đổi kích thước của MazePanel để vừa với kích thước của JFrame
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                mazePanel.adjustScaleToFit();
            }
        });
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        // Mở hộp thoại kỹ năng khi bắt đầu trò chơi
        new Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                mazePanel.useSkill(openSkillDialog());
                cancel(); // Hủy tác vụ sau khi mở hộp thoại kỹ năng
            }
        }, 0); // Thời gian trễ 1 giây trước khi mở hộp thoại kỹ năng
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }

    public int openSkillDialog()
    {
        if (skillDialog == null)
            skillDialog = new SkillDialog(this);
        // Tạo danh sách các kỹ năng có thể chọn
        // Loại bỏ ngẫu nhiên một kỹ năng khỏi danh sách
        int randint = (int) (Math.random() * 4); // Tạo số ngẫu nhiên từ 0 đến 3
        int buffList[] = {Buff.SENRIGAN, Buff.SLIME_SAN_ONEGAI, Buff.TOU_NO_HIKARI, Buff.UNMEI_NO_MICHI};
        int skillBuff[] = new int[buffList.length - 1];
        int skillCount = 0;
        for (int i = 0; i < buffList.length; i++) {
            if (i != randint) {
                skillBuff[skillCount++] = buffList[i]; // Thêm các kỹ năng khác vào danh sách
            }
        }
        skillDialog.ShowDialog(skillBuff);
        int selectedSkill = skillDialog.selectedSkill;
        skillDialog.selectedSkill = Buff.NONE; // Đặt lại kỹ năng đã chọn về NONE
        skillDialog.Click_SkillCard(Buff.NONE); // Đặt lại kỹ năng tạm thời về NONE
        return selectedSkill; // Trả về kỹ năng đã chọn
    }

    public void showMessage(String message, Dimension size) {
        new MessageDialog(this, message, size);
        mazePanel.requestFocusInWindow(); // Đặt lại focus cho mazePanel sau khi hiển thị thông báo
    }
}
