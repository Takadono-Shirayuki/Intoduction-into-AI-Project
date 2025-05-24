package mazeinterface;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import javax.swing.*;

import game.Main;
import game.GameVariable;
import mazeinterface.mazecontrol.InfoPanel;
import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazedialog.MessageDialog;
import mazeinterface.mazedialog.SelectDialog;
import mazeinterface.mazedialog.ShadowOverlay;
import mazeinterface.mazedialog.SkillDialog;
import mazenv.MazeEnv;
import mazenv.MazeEnv.Buff;

public class GameForm extends JFrame {
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/GameBackground.jpg";

    private final boolean skipInitialDialog;
    private MazeEnv mazeEnv;
    private MazePanel mazePanel;
    private SkillDialog skillDialog;

    // constructor chung, gán env và flag skip, rồi build UI
    private GameForm(int mazeSize, MazeEnv env, boolean skipInitialDialog) {
        super();
        this.skipInitialDialog = skipInitialDialog;
        this.mazeEnv = env;
        initUI(mazeSize);
        SwingUtilities.invokeLater(() -> mazePanel.requestFocusInWindow());
    }

    // Game mới: skipInitialDialog = false
    public GameForm(int mazeSize) {
        this(mazeSize,
             new MazeEnv(mazeSize, 15, 60, Buff.SLIME_STEP, Buff.TOU_NO_HIKARI_OBS),
             false);
    }

    // Chơi tiếp: skipInitialDialog = true
    public GameForm(MazeEnv env) {
        this(env.getMazeSize(), env, true);
    }

    private void initUI(int mazeSize) {
        Main.setGameForm(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        setResizable(false);

        AudioPlayer.playSingleSound(AudioPlayer.BACKGROUND_MUSIC_PATH_GAMEFROM);

        try {
            ImageIcon ico = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
            Image img = ico.getImage();
            setContentPane(new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            });
            getContentPane().setLayout(new BorderLayout());
        } catch(Exception e) {
            System.err.println("Không thể tải background");
        }

        mazePanel = new MazePanel(mazeSize, mazeEnv, this);
        InfoPanel info = new InfoPanel();
        JPanel combined = new JPanel(new BorderLayout());
        combined.setOpaque(false);
        combined.add(info, BorderLayout.WEST);
        combined.add(mazePanel, BorderLayout.CENTER);
        add(combined, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRight.setOpaque(false);
        JButton settings = new JButton("⚙");
        settings.addActionListener(e -> openSettings());
        topRight.add(settings);
        add(topRight, BorderLayout.NORTH);

        addComponentListener(new java.awt.event.ComponentAdapter(){
            public void componentResized(java.awt.event.ComponentEvent e) {
                mazePanel.adjustScaleToFit();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        // popup SkillDialog đầu nếu không skip
        if (!skipInitialDialog) {
            new Timer().schedule(new java.util.TimerTask(){
                @Override public void run(){
                    skillDialog = new SkillDialog(GameForm.this);
                    mazePanel.useSkill(openSkillDialog());
                    cancel();
                }
            }, 0);
        }
    }

    private void openSettings() {
        String[] opts = {"Tiếp tục","Chơi lại","Trang chủ","Trợ giúp","Thoát"};
        int sel = new SelectDialog(this,"Cài đặt",opts,new Dimension(200,50)).returnValue;

        switch(sel){
            case 0: break; // tiếp tục
            case 1: // chơi lại
                new ShadowOverlay(this,500,0,ShadowOverlay.MIST_FALL);
                new Timer().schedule(new java.util.TimerTask(){
                    @Override public void run(){
                        mazePanel.resetGame();
                        new ShadowOverlay(GameForm.this,500,500,ShadowOverlay.MIST_RISE);
                        mazePanel.useSkill(openSkillDialog());
                        cancel();
                    }
                },300);
                break;
            case 2: // trang chủ
                try { mazeEnv.saveEnv(GameVariable.SAVED_GAME_PATH); }
                catch(IOException ex){ ex.printStackTrace(); }
                new ShadowOverlay(this,300,0,ShadowOverlay.MIST_FALL);
                new ShadowOverlay(new MainForm(),500,1000,ShadowOverlay.MIST_RISE);
                new Timer().schedule(new java.util.TimerTask(){
                    @Override public void run(){ dispose(); cancel(); }
                },1500);
                break;
            case 3: // trợ giúp
                break;
            case 4: // thoát
                System.exit(0);
                break;
        }
        mazePanel.requestFocusInWindow();
    }

    public int openSkillDialog(){
        int r=(int)(Math.random()*4);
        int[] all={Buff.SENRIGAN,Buff.SLIME_SAN_ONEGAI,Buff.TOU_NO_HIKARI,Buff.UNMEI_NO_MICHI};
        int[] pick=new int[3]; int idx=0;
        for(int i=0;i<4;i++) if(i!=r) pick[idx++]=all[i];
        skillDialog.ShowDialog(pick);
        int sel=skillDialog.selectedSkill;
        skillDialog.selectedSkill=Buff.NONE;
        skillDialog.Click_SkillCard(Buff.NONE);
        return sel;
    }

    public void showMessage(String msg, Dimension size){
        new MessageDialog(this,msg,size);
        mazePanel.requestFocusInWindow();
    }

    public MazePanel getMazePanel(){
        return mazePanel;
    }
}
