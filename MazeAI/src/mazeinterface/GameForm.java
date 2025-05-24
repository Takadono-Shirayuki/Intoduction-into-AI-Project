package mazeinterface;

import java.awt.*;
import java.util.Timer;
import javax.swing.*;
import mazeinterface.mazecontrol.AIPanel;
import mazeinterface.mazecontrol.InfoPanel;
import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazedialog.MessageDialog;
import mazeinterface.mazedialog.SelectDialog;
import mazeinterface.mazedialog.ShadowOverlay;
import mazeinterface.mazedialog.SkillDialog;
import mazenv.*;
import mazenv.MazeEnv.Buff;

public class GameForm extends JFrame {
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/GameBackground.jpg";
    private MazePanel mazePanel;
    private SkillDialog skillDialog;
    private MazeEnv mazeEnv;

    public GameForm(int mazeSize) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        setResizable(false);

        // Vẽ hình nền
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
            Image bgImage = bgIcon.getImage();
            setContentPane(new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            });
            getContentPane().setLayout(new BorderLayout());
        } catch (Exception e) {
            System.err.println("Không thể tải background");
        }

        // Khởi tạo logic
        this.mazeEnv = new MazeEnv(mazeSize, 15, 60, Buff.SLIME_STEP, Buff.TOU_NO_HIKARI_OBS);
        mazePanel = new MazePanel(mazeSize, mazeEnv, this);

        InfoPanel infoPanel = new InfoPanel();
        AIPanel aiPanel = new AIPanel(500); // ping mặc định

        // Tạo khoảng đệm giữa InfoPanel và AIPanel
JPanel spacer = new JPanel();
spacer.setPreferredSize(new Dimension(0, 20)); // cao 20px để đẩy AIPanel lên cao hơn
spacer.setOpaque(false);

// Gói InfoPanel + spacer + AIPanel vào 1 panel nhỏ ở trên
JPanel aiContainer = new JPanel();
aiContainer.setLayout(new BoxLayout(aiContainer, BoxLayout.Y_AXIS));
aiContainer.setOpaque(false);
aiContainer.add(infoPanel);
aiContainer.add(spacer);
aiContainer.add(aiPanel);

JPanel leftColumn = new JPanel(new BorderLayout());
leftColumn.setOpaque(false);
leftColumn.add(aiContainer, BorderLayout.NORTH); // Đẩy toàn bộ cụm này lên đầu


// Gộp lại với MazePanel
JPanel combinedPanel = new JPanel(new BorderLayout());
combinedPanel.setOpaque(false);
combinedPanel.add(leftColumn, BorderLayout.WEST);
combinedPanel.add(mazePanel, BorderLayout.CENTER);

// Thêm vào frame
add(combinedPanel, BorderLayout.CENTER);


        // Nút cài đặt góc phải trên
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        JButton settingBtn = new JButton("⚙");
        settingBtn.addActionListener(e -> {
            String[] options = { "Tiếp tục", "Chơi lại", "Trang chủ", "Trợ giúp", "Thoát" };
            int selection = new SelectDialog(this, "Cài đặt", options, new Dimension(200, 50)).returnValue;
            switch (selection) {
                case 0 -> {} // tiếp tục
                case 1 -> {
                    new ShadowOverlay(this, 500, 0, ShadowOverlay.MIST_FALL);
                    new Timer().schedule(new java.util.TimerTask() {
                        public void run() {
                            mazePanel.resetGame();
                            new ShadowOverlay(GameForm.this, 500, 500, ShadowOverlay.MIST_RISE);
                            GameForm.this.mazePanel.useSkill(GameForm.this.openSkillDialog());
                            cancel();
                        }
                    }, 300);
                }
                case 2 -> {
                    new ShadowOverlay(this, 300, 0, ShadowOverlay.MIST_FALL);
                    new ShadowOverlay(new MainForm(), 500, 1000, ShadowOverlay.MIST_RISE);
                    new Timer().schedule(new java.util.TimerTask() {
                        public void run() {
                            dispose();
                            cancel();
                        }
                    }, 1500);
                    new Timer().schedule(new java.util.TimerTask() {
                        public void run() {
                            setVisible(false);
                            cancel();
                        }
                    }, 1000);
                }
                case 3 -> {} // trợ giúp
                case 4 -> System.exit(0);
            }
            mazePanel.requestFocusInWindow();
        });
        topRightPanel.add(settingBtn);
        add(topRightPanel, BorderLayout.NORTH);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                mazePanel.adjustScaleToFit();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        // Mở hộp thoại kỹ năng khi bắt đầu
        new Timer().schedule(new java.util.TimerTask() {
            public void run() {
                skillDialog = new SkillDialog(GameForm.this);
                mazePanel.useSkill(openSkillDialog());
                cancel();
            }
        }, 0);
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }

    public int openSkillDialog() {
        int randint = (int) (Math.random() * 4);
        int[] buffList = { Buff.SENRIGAN, Buff.SLIME_SAN_ONEGAI, Buff.TOU_NO_HIKARI, Buff.UNMEI_NO_MICHI };
        int[] skillBuff = new int[buffList.length - 1];
        int skillCount = 0;
        for (int i = 0; i < buffList.length; i++) {
            if (i != randint) {
                skillBuff[skillCount++] = buffList[i];
            }
        }
        skillDialog.ShowDialog(skillBuff);
        int selectedSkill = skillDialog.selectedSkill;
        skillDialog.selectedSkill = Buff.NONE;
        skillDialog.Click_SkillCard(Buff.NONE);
        return selectedSkill;
    }

    public void showMessage(String message, Dimension size) {
        new MessageDialog(this, message, size);
        mazePanel.requestFocusInWindow();
    }
}
