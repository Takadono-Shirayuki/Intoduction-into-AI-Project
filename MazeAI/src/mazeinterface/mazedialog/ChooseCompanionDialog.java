package mazeinterface.mazedialog;

import mazeinterface.mazecontrol.ColorShiftButton;
import javax.swing.*;

import japy.JaPySolveMaze;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;


public class ChooseCompanionDialog extends JDialog {
    private File[] scriptFiles;
    private int selectedIndex = -1;
    private JButton[] levelButtons;
    private boolean exitOnClose = true;

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
        File scriptDir = Paths.get(System.getProperty("user.dir"), "scripts").toFile();
        File[] subDirs = scriptDir.listFiles(File::isDirectory);
        if (subDirs == null || subDirs.length == 0) {
            System.err.println("Không tìm thấy thư mục script.");
            return;
        }

        List<File> validScripts = new ArrayList<>();
        List<String> levelNames = new ArrayList<>();

        for (File dir : subDirs) {
            File[] pyFiles = dir.listFiles((d, name) -> name.endsWith(".py"));
            if (pyFiles != null && pyFiles.length == 1) {
                validScripts.add(pyFiles[0]);
                levelNames.add(dir.getName());
            }
        }

        if (validScripts.isEmpty()) {
            System.err.println("Không tìm thấy tệp .py hợp lệ trong các thư mục script.");
            return;
        }

        scriptFiles = validScripts.toArray(new File[0]);
        levelButtons = new JButton[scriptFiles.length];

        Font btnFont = new Font("SansSerif", Font.BOLD, 20);
        Color btnStart = new Color(30, 30, 30);
        Color btnEnd = new Color(70, 70, 70);
        Dimension btnSize = new Dimension(300, 45);

        int total = scriptFiles.length;
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
            ColorShiftButton levelBtn = new ColorShiftButton(
                levelNames.get(i),
                btnFont,
                Color.WHITE,
                btnStart,
                btnEnd,
                btnSize
            );
            int idx = i;
            levelBtn.addActionListener(e -> {
                selectedIndex = idx;
                updateButtonStyles();
            });
            levelBtn.setBounds((centerPanelWidth - btnSize.width) / 2, 60 + i * 55, btnSize.width, btnSize.height);
            levelButtons[i] = levelBtn;
            centerPanel.add(levelBtn);
        }

        ColorShiftButton runButton = new ColorShiftButton("▶ Bắt đầu hành trình",
                btnFont,
                Color.WHITE,
                new Color(138, 43, 226),
                new Color(180, 80, 250),
                new Dimension(250, 45));

        runButton.setBounds((centerPanelWidth - 250) / 2, 60 + total * 55 + 10, 250, 45);
        runButton.addActionListener(e -> {
            JaPySolveMaze.Initialize(scriptFiles[selectedIndex].getAbsolutePath());
            exitOnClose = false;
            dispose();
        });
        centerPanel.add(runButton);

        selectedIndex = 0;
        updateButtonStyles();

        contentPanel.add(centerPanel);
    }

    private void updateButtonStyles() {
        for (int i = 0; i < levelButtons.length; i++) {
            JButton btn = levelButtons[i];
            if (i == selectedIndex) {
                btn.setForeground(Color.YELLOW);
            } else {
                btn.setForeground(Color.WHITE);
            }
            btn.repaint();
        }
    }
}
