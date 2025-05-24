package mazeinterface;

import javax.swing.*;
import game.GameVariable;
import mazeinterface.mazecontrol.ColorShiftButton;
import mazeinterface.mazedialog.ShadowOverlay;
import mazenv.MazeEnv;

import java.awt.*;

public class MainForm extends JFrame {
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/MainBackground.jpg";

    public MainForm() {
        // Thiết lập JFrame
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        // Phát nhạc nền menu
        AudioPlayer.playSingleSound(AudioPlayer.BACKGROUND_MUSIC_PATH_MAINFROM);

        // Background panel
        JPanel backgroundPanel = new JPanel();
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
            Image bgImage = bgIcon.getImage();
            backgroundPanel = new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            };
            setContentPane(backgroundPanel);
            getContentPane().setLayout(null);
        } catch(Exception e){
            System.err.println("Không thể tải background");
        }

        // Font và màu gradient cho nút
        Font btnFont = new Font("SansSerif", Font.BOLD, 24);
        Color btnStart = new Color(255, 105, 180);
        Color btnEnd   = new Color(128, 0, 128);

        // Tọa độ và kích thước nút
        int x = 620, y0 = 300, spacing = 70;
        Dimension sz = new Dimension(300, 50);

        // Tạo và đặt vị trí các nút
        ColorShiftButton play    = new ColorShiftButton("Chơi",      btnFont, Color.WHITE, btnStart, btnEnd, sz);
        ColorShiftButton cont    = new ColorShiftButton("Chơi tiếp", btnFont, Color.WHITE, btnStart, btnEnd, sz);
        ColorShiftButton hell    = new ColorShiftButton("Chế độ địa ngục", btnFont, Color.WHITE, btnStart, btnEnd, sz);
        ColorShiftButton help    = new ColorShiftButton("Hướng dẫn", btnFont, Color.WHITE, btnStart, btnEnd, sz);
        ColorShiftButton exit    = new ColorShiftButton("Thoát",      btnFont, Color.WHITE, btnStart, btnEnd, sz);

        play   .setLocation(x, y0 + spacing*0);
        cont   .setLocation(x, y0 + spacing*1);
        hell   .setLocation(x, y0 + spacing*2);
        help   .setLocation(x, y0 + spacing*3);
        exit   .setLocation(x, y0 + spacing*4);

        // Sự kiện cho các nút
        play.addActionListener(e -> startGame(false));
        cont.addActionListener(e -> startGame(true));
        hell.addActionListener(e -> startHellMode());
        help.addActionListener(e -> new InstructionWindow(this).setVisible(true));
        exit.addActionListener(e -> System.exit(0));

        // Thêm vào panel
        backgroundPanel.add(play);
        backgroundPanel.add(cont);
        backgroundPanel.add(hell);
        backgroundPanel.add(help);
        backgroundPanel.add(exit);

        setVisible(true);
    }

private void startGame(boolean continueGame) {
    new ShadowOverlay(this, 500, 0, ShadowOverlay.MIST_FALL);
    int mazeSize = 30;

    if (continueGame) {
        try {
            MazeEnv env = MazeEnv.loadEnv(GameVariable.SAVED_GAME_PATH);
            // Tạo GameForm với skipInitialDialog = true
            new ShadowOverlay(new GameForm(env), 500, 1000, ShadowOverlay.MIST_RISE);
        } catch (Exception ex) {
            System.err.println("Không thể tải savegame, khởi mới");
            new ShadowOverlay(new GameForm(mazeSize), 500, 1000, ShadowOverlay.MIST_RISE);
        }
    } else {
        // Chơi mới
        new ShadowOverlay(new GameForm(mazeSize), 500, 1000, ShadowOverlay.MIST_RISE);
    }

    // Đóng menu sau hiệu ứng
    new java.util.Timer().schedule(new java.util.TimerTask(){
        @Override public void run(){ dispose(); cancel(); }
    }, 1500);
    setVisible(false);
}


    private void startHellMode() {
        // TODO: implement hell mode
    }
}
