package mazeinterface;

import java.awt.*;
import java.util.Timer;
import javax.swing.*;

import game.Main;
import mazeinterface.mazecontrol.InfoPanel;
import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazedialog.MessageDialog;
import mazeinterface.mazedialog.SelectDialog;
import mazeinterface.mazedialog.ShadowOverlay;
import mazeinterface.mazedialog.SkillDialog;
import mazenv.*;
import mazenv.MazeEnv.Buff;

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
        Main.setGameForm(this);
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

        // Khởi tạo MazePanel và InfoPanel, sau đó đưa vào JPanel chung
        MazeEnv mazeEnv = new MazeEnv(mazeSize, 15, 60, Buff.SLIME_STEP, Buff.TOU_NO_HIKARI_OBS);
        mazePanel = new MazePanel(mazeSize, mazeEnv, this);
        
        InfoPanel infoPanel = new InfoPanel();
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.setOpaque(false);
        combinedPanel.add(infoPanel, BorderLayout.WEST);
        combinedPanel.add(mazePanel, BorderLayout.CENTER);
        add(combinedPanel, BorderLayout.CENTER);

        // Tạo một JPanel cho các nút điều khiển
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);

        // Tạo nút cài đặt
        JButton settingBtn = new JButton("⚙");

        settingBtn.addActionListener(e -> {
            String options[] = {"Tiếp tục", "Chơi lại", "Trang chủ", "Trợ giúp", "Thoát"};
            int selection = new SelectDialog(this, "Cài đặt", options, new Dimension(200, 50)).returnValue;
            switch (selection) {
                case 0: // Tiếp tục
                    break;
                case 1: // Chơi lại
                    new ShadowOverlay(this, 500, 0, ShadowOverlay.MIST_FALL);
                    new Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            mazePanel.resetGame();
                            new ShadowOverlay(GameForm.this, 500, 500, ShadowOverlay.MIST_RISE);
                            GameForm.this.mazePanel.useSkill(GameForm.this.openSkillDialog());
                            cancel();
                        }
                    }, 300);
                    break;
                case 2: // Trang chủ
                    new ShadowOverlay(this, 300, 0, ShadowOverlay.MIST_FALL);
                    new ShadowOverlay(new MainForm(), 500, 1000, ShadowOverlay.MIST_RISE);
                    new Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            dispose();
                            cancel();
                        }
                    }, 1500);
                    new Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setVisible(false);
                            cancel();
                        }
                    }, 1000);
                    break;
                case 3: // Trợ giúp
                    break;
                case 4: // Thoát
                    System.exit(0);
                    break;
            }
            mazePanel.requestFocusInWindow();
        });
        topRightPanel.add(settingBtn);

        add(topRightPanel, BorderLayout.NORTH);

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
        int buffList[] = {Buff.SENRIGAN, Buff.SLIME_SAN_ONEGAI, Buff.TOU_NO_HIKARI, Buff.UNMEI_NO_MICHI};
        int skillBuff[] = new int[buffList.length - 1];
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
