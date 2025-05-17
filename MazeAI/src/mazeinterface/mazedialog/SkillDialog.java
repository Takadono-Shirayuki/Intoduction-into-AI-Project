package mazeinterface.mazedialog;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import mazeinterface.mazecontrol.ImageButton;
import mazeinterface.mazecontrol.SkillCard;
import mazenv.MazeEnv.Buff;

public class SkillDialog extends JDialog {
    public int selectedSkill = Buff.NONE;
    public int tempSkill = Buff.NONE;
    private SkillCard[] skillCard;

    public SkillDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 180));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // GridBagLayout để căn giữa toàn bộ
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Panel chứa các skill card
        JPanel skillCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        skillCardPanel.setOpaque(false);

        skillCard = new SkillCard[4];
        skillCard[0] = new SkillCard(this, Buff.SENRIGAN);
        skillCard[1] = new SkillCard(this, Buff.SLIME_SAN_ONEGAI);
        skillCard[2] = new SkillCard(this, Buff.TOU_NO_HIKARI);
        skillCard[3] = new SkillCard(this, Buff.UNMEI_NO_MICHI);

        for (SkillCard card : skillCard) {
            skillCardPanel.add(card);
        }

        gbc.gridy = 0;
        mainPanel.add(skillCardPanel, gbc);

        // Panel chứa nút
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        selectionPanel.setOpaque(false);

        JButton selectButton = new ImageButton("/mazeai/Icon/SpookyButton.jpg", "Chọn",
                new Font("Arial", Font.BOLD, 20), Color.WHITE, new Dimension(150, 60));
        JButton skipButton = new ImageButton("/mazeai/Icon/SpookyButton.jpg", "Bỏ qua",
                new Font("Arial", Font.BOLD, 20), Color.WHITE, new Dimension(150, 60));

        selectButton.addActionListener(e -> {
            selectedSkill = tempSkill;
            setVisible(false);
        });
        skipButton.addActionListener(e -> setVisible(false));

        selectionPanel.add(selectButton);
        selectionPanel.add(skipButton);

        gbc.gridy = 1;
        mainPanel.add(selectionPanel, gbc);

        contentPanel.add(mainPanel, BorderLayout.CENTER);
        add(contentPanel);

        pack();
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent);
    }

    public void Click_SkillCard(int skillBuff) {
        tempSkill = skillBuff;
        for (SkillCard card : skillCard) {
            if (card.getSkillBuff() != skillBuff) {
                card.resetBorder();
            }
        }
    }

    public void ShowDialog() {
        pack();
        setVisible(true);
    }

    public void ShowDialog(int[] skillBuff) {
        for (SkillCard card : skillCard) {
            boolean match = false;
            for (int buff : skillBuff) {
                if (card.getSkillBuff() == buff) {
                    match = true;
                    break;
                }
            }
            card.setVisible(match);
        }
        pack();
        setVisible(true);
    }
}
