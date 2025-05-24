package mazeinterface.mazedialog;

import mazeinterface.mazecontrol.CharacterImage;
import mazeinterface.mazecontrol.ColorShiftButton;
import mazeinterface.mazecontrol.TypingTextArea;
import mazeobject.Command;

import javax.swing.*;

import japy.JaPySolveMaze;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Timer;
import java.util.ArrayList;

public class ChooseCompanionDialog extends JDialog {
    private class Dialogue extends Command.Dialogue {
        public Dialogue(String character, String displayName, String content) {
            super(character, displayName, content);
        }

        public void run() {
            typingTextArea.load(this, 20);
            characterImage.load(this.character, this.avatar);
            characterImage.highlight();
        }
    }

    private int selectedIndex = -1;
    private JButton[] conpanionButtons;
    private boolean exitOnClose = true;
    private List<Dialogue> dialogues = new ArrayList<>();
    private CharacterImage characterImage = new CharacterImage(); // Hiển thị ảnh nhân vật
    
    private TypingTextArea typingTextArea; // Khu vực hiển thị lời thoại
    
    public ChooseCompanionDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (exitOnClose) System.exit(0);
            }
        });

        JPanel contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        setContentPane(contentPanel);

        // Load level folders
        File[] scriptDirectory = Paths.get(System.getProperty("user.dir"), "scripts").toFile().listFiles(File::isDirectory);
        if (scriptDirectory == null || scriptDirectory.length == 0) {
            System.err.println("Không tìm thấy thư mục script.");
            return;
        }

        List<String> companionName = new ArrayList<>();

        for (File dir : scriptDirectory) {
            File[] pyFiles = dir.listFiles((d, name) -> name.endsWith(".py"));
            if (pyFiles != null && pyFiles.length == 1) {
                companionName.add(dir.getName());
            }
        }
        conpanionButtons = new JButton[companionName.size()];

        Font btnFont = new Font("SansSerif", Font.BOLD, 20);
        Color btnStart = new Color(30, 30, 30);
        Color btnEnd = new Color(70, 70, 70);
        Dimension btnSize = new Dimension(300, 45);

        int total = companionName.size();
        int centerPanelWidth = 800;
        int centerPanelHeight = total * 60 + 130;
        JPanel centerPanel = new JPanel(null);
        centerPanel.setOpaque(false);
        centerPanel.setBounds(
            (getWidth() - centerPanelWidth) / 2,
            (getHeight() - centerPanelHeight) / 2,
            centerPanelWidth,
            centerPanelHeight
        );

        JLabel titleLabel = new JLabel("Ai là người giúp bạn thoát khỏi tuyệt vọng?");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds((centerPanelWidth - 800) / 2, 0, 800, 50);
        centerPanel.add(titleLabel);

        for (int i = 0; i < total; i++) {
            ColorShiftButton companionButton = new ColorShiftButton(
                companionName.get(i),
                btnFont,
                Color.WHITE,
                btnStart,
                btnEnd,
                btnSize
            );
            int scriptIndex = i;
            companionButton.addActionListener(e -> {
                dialogues.clear();
                try (BufferedReader reader = new BufferedReader(new FileReader(scriptDirectory[scriptIndex].getAbsolutePath() + "/info.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 3) {
                            String character = parts[0];
                            String displayName = parts[1];
                            String content = parts[2];
                            dialogues.add(new Dialogue(character, displayName, content));
                        }
                    }   
                } catch (Exception ex) {
                    System.err.println("Không thể đọc tài liệu");
                }

                new Timer().schedule(
                    new java.util.TimerTask() {
                        int commandIndex = 0;
                        @Override
                        public void run() {
                            if (!typingTextArea.isTyping())
                            {
                                if (commandIndex == dialogues.size()) {
                                    characterImage.dimmed();
                                    cancel();
                                } else {
                                    Dialogue dialogue = dialogues.get(commandIndex);
                                    commandIndex++;
                                    typingTextArea.load(dialogue, 50);
                                    characterImage.load(dialogue.character, dialogue.avatar);
                                    characterImage.highlight();
                                }
                            }
                        }
                    }, 0, 50
                );
                selectedIndex = scriptIndex;
                updateButtonStyles();
            });
            companionButton.setBounds((centerPanelWidth - btnSize.width) / 2, 60 + i * 60, btnSize.width, btnSize.height);
            conpanionButtons[i] = companionButton;
            centerPanel.add(companionButton);
        }

        ColorShiftButton runButton = new ColorShiftButton("▶ Bắt đầu hành trình",
                btnFont,
                Color.WHITE,
                new Color(138, 43, 226),
                new Color(180, 80, 250),
                new Dimension(250, 45));

        runButton.setBounds((centerPanelWidth - 250) / 2, 70 + total * 60, 250, 45);
        runButton.addActionListener(e -> {
            JaPySolveMaze.Initialize(scriptDirectory[selectedIndex].getAbsolutePath() + "/run.py");
            exitOnClose = false;
            dispose();
        });
        centerPanel.add(runButton);

        selectedIndex = 0;
        updateButtonStyles();

        // Tạo panel chứa lời thoại
        typingTextArea = new TypingTextArea();
        typingTextArea.setBounds(50, getHeight() - 170, getWidth() - 100, 150); // Set kích thước
        getContentPane().add(typingTextArea);

        // Gán ảnh nhân vật bên trái
        characterImage = new CharacterImage();
        characterImage.setBounds(50, 100, 400, 600);
        getContentPane().add(characterImage);

        contentPanel.add(centerPanel);
        setVisible(true);
    }

    private void updateButtonStyles() {
        for (int i = 0; i < conpanionButtons.length; i++) {
            JButton btn = conpanionButtons[i];
            if (i == selectedIndex) {
                btn.setForeground(Color.YELLOW);
            } else {
                btn.setForeground(Color.WHITE);
            }
            btn.repaint();
        }
    }
}
