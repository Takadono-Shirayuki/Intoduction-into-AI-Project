package mazeinterface; // Khai báo package chứa lớp MazeIntro

import javax.imageio.ImageIO; // Dùng để đọc ảnh
import javax.swing.*; // Thư viện Swing cho giao diện người dùng
import java.awt.*; // Thư viện đồ họa
import java.awt.event.*; // Xử lý sự kiện
import java.awt.image.BufferedImage; // Ảnh có thể thao tác pixel
import java.awt.image.ConvolveOp; // Dùng để làm mờ ảnh
import java.awt.image.Kernel; // Kernel cho filter
import java.net.URL; // Dùng để lấy tài nguyên từ file
import javax.sound.sampled.*; // Thư viện âm thanh
import java.io.*; // Đọc/ghi file

import java.util.ArrayList;
import java.util.List;

// Lớp hiển thị đoạn giới thiệu đầu game dưới dạng hội thoại
public class MazeIntro extends JDialog {
    // Đường dẫn tài nguyên
    private static final String BACKGROUND_IMAGE_PATH = "/mazeai/MazeImage/MazeIntro.jpg";
    private static final String NPC_IMAGE_PATH = "/mazeai/MazeImage/NPCImage.png";
    private static final String PLAYER_IMAGE_PATH = "/mazeai/MazeImage/PlayerImage.png";
    private static final String DIALOGUE_FILE_PATH = "/mazeai/MazeDialogues/intro_dialog.txt";

    private JLabel npcImageLabel; // Hiển thị ảnh NPC
    private JLabel playerImageLabel; // Hiển thị ảnh người chơi
    private JLabel speakerLabel; // Hiển thị tên người nói
    private JTextArea dialogArea; // Khu vực hiển thị lời thoại
    private Timer typingTimer; // Timer để hiện từng ký tự

    private List<String[]> dialogues = new ArrayList<>(); // Danh sách các câu thoại
    private int dialogueIndex = 0; // Câu thoại hiện tại
    private String currentLine = ""; // Dòng hiện tại đang hiển thị
    private int charIndex = 0; // Vị trí ký tự đang hiển thị

    private ImageIcon npcBrightIcon, npcDimIcon, playerBrightIcon, playerDimIcon; // Các ảnh sáng/tối
    private Clip introClip; // Âm thanh đoạn intro
    private boolean isMuted = false; // Trạng thái tắt tiếng

    // Constructor khởi tạo cửa sổ intro
    public MazeIntro(Frame parent) {
        super(parent, "Mở đầu trò chơi", true); // Tạo dialog modal
        setUndecorated(true); // Không viền
        setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Full màn hình
        setLayout(null); // Tự set layout thủ công

        loadDialogues(); // Tải lời thoại từ file

        try {
            // Làm mờ ảnh nền
            BufferedImage original = ImageIO.read(getClass().getResource(BACKGROUND_IMAGE_PATH));
            float[] blurKernel = {
                1f / 16f, 2f / 16f, 1f / 16f,
                2f / 16f, 4f / 16f, 2f / 16f,
                1f / 16f, 2f / 16f, 1f / 16f
            };
            ConvolveOp op = new ConvolveOp(new Kernel(3, 3, blurKernel));
            BufferedImage blurred = op.filter(original, null);
            Image scaledBlur = blurred.getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH);
            JLabel backgroundLabel = new JLabel(new ImageIcon(scaledBlur));
            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
            setContentPane(backgroundLabel); // Đặt ảnh làm nền
            getContentPane().setLayout(null); // Layout thủ công
        } catch (Exception e) {
            getContentPane().setBackground(Color.BLACK); // Nếu lỗi thì dùng nền đen
            e.printStackTrace();
        }

        // Tải ảnh sáng/tối
        npcBrightIcon = loadImage(NPC_IMAGE_PATH);
        npcDimIcon = makeDimmedIcon(npcBrightIcon);
        playerBrightIcon = loadImage(PLAYER_IMAGE_PATH);
        playerDimIcon = makeDimmedIcon(playerBrightIcon);

        // Gán ảnh NPC
        npcImageLabel = new JLabel();
        npcImageLabel.setBounds(50, 100, 500, 500);
        getContentPane().add(npcImageLabel);

        // Gán ảnh người chơi
        playerImageLabel = new JLabel();
        getContentPane().add(playerImageLabel);

        // Nhãn hiển thị tên người nói
        speakerLabel = new JLabel(" ");
        speakerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        speakerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        getContentPane().add(speakerLabel);

        // Khu vực hiển thị lời thoại
        dialogArea = new JTextArea();
        dialogArea.setLineWrap(true);
        dialogArea.setWrapStyleWord(true);
        dialogArea.setFont(new Font("Serif", Font.PLAIN, 26));
        dialogArea.setEditable(false);
        dialogArea.setBackground(Color.DARK_GRAY);

        // Cuộn nếu lời thoại dài
        JScrollPane scrollPane = new JScrollPane(dialogArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        getContentPane().add(scrollPane);

        // Nút tắt tiếng
        JButton muteButton = new JButton("🔊");
        muteButton.setBounds(getWidth() - 120, 50, 100, 40);
        muteButton.addActionListener(e -> toggleMute());
        getContentPane().add(muteButton);

        // Nút bỏ qua đoạn giới thiệu
        JButton skipButton = new JButton("Skip");
        skipButton.setBounds(getWidth() - 120, 100, 100, 40);
        skipButton.addActionListener(e -> skipIntro());
        getContentPane().add(skipButton);

        // Set vị trí các thành phần khi giao diện sẵn sàng
        SwingUtilities.invokeLater(() -> {
            int w = getWidth();
            int h = getHeight();
            playerImageLabel.setBounds(w - 450, 110, 400, 600);
            speakerLabel.setBounds(50, h - 200, w - 100, 40);
            scrollPane.setBounds(50, h - 150, w - 200, 100);
        });

        playSound("/mazeai/MazeSound/MazeSound.wav"); // Bật nhạc nền
        showNextDialogue(); // Hiển thị câu thoại đầu

        // Xử lý Alt+F4 (đóng hoàn toàn)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (introClip != null) {
                    introClip.stop();
                }
                System.exit(0);
            }
        });

        // Nhấn phím sẽ chuyển tiếp thoại
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                showNextDialogue();
            }
        });

        // Click chuột cũng chuyển thoại
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextDialogue();
            }
        });

        setFocusable(true); // Cho phép nhận phím
    }

    // Tải lời thoại từ file
    private void loadDialogues() {
        InputStream is = getClass().getResourceAsStream(DIALOGUE_FILE_PATH);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(":", 2); // Format: speaker: content
                    if (parts.length == 2) {
                        dialogues.add(new String[]{parts[0].trim(), parts[1].trim()});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hiển thị câu thoại tiếp theo, có hiệu ứng gõ chữ
    private void showNextDialogue() {
        if (dialogueIndex < dialogues.size()) {
            String speaker = dialogues.get(dialogueIndex)[0];
            currentLine = dialogues.get(dialogueIndex)[1];
            charIndex = 0;
            dialogArea.setText("");
            highlightSpeaker(speaker); // Tô sáng người nói

            if (typingTimer != null && typingTimer.isRunning()) {
                typingTimer.stop();
            }

            typingTimer = new Timer(35, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (charIndex < currentLine.length()) {
                        dialogArea.append(String.valueOf(currentLine.charAt(charIndex)));
                        charIndex++;
                    } else {
                        typingTimer.stop();
                        dialogueIndex++;
                    }
                }
            });
            typingTimer.start();
        } else {
            // Hết thoại thì thông báo bắt đầu game
            JOptionPane.showMessageDialog(this, "Bắt đầu vào Game!");
            System.exit(0);
        }
    }

    // Làm sáng người nói và làm mờ người còn lại
    private void highlightSpeaker(String speaker) {
        if (speaker.equals("npc")) {
            npcImageLabel.setIcon(npcBrightIcon);
            playerImageLabel.setIcon(playerDimIcon);
            speakerLabel.setText("NPC:");
            speakerLabel.setForeground(Color.PINK);
            dialogArea.setForeground(Color.PINK);
        } else {
            npcImageLabel.setIcon(npcDimIcon);
            playerImageLabel.setIcon(playerBrightIcon);
            speakerLabel.setText("Player:");
            speakerLabel.setForeground(Color.CYAN);
            dialogArea.setForeground(Color.CYAN);
        }
    }

    // Làm mờ ảnh
    private ImageIcon makeDimmedIcon(ImageIcon original) {
        Image img = original.getImage();
        Image dimmed = new ImageIcon(GrayFilter.createDisabledImage(img)).getImage()
                .getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        return new ImageIcon(dimmed);
    }

    // Tải ảnh từ đường dẫn
    private ImageIcon loadImage(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Tài nguyên không tìm thấy: " + path);
            return new ImageIcon();
        }
        return new ImageIcon(url);
    }

    // Bật nhạc nền
    private void playSound(String soundPath) {
        try {
            URL soundURL = getClass().getResource(soundPath);
            if (soundURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                introClip = AudioSystem.getClip();
                introClip.open(audioIn);
                introClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bật/tắt tiếng
    private void toggleMute() {
        if (isMuted) {
            if (introClip != null) {
                introClip.start();
            }
            isMuted = false;
        } else {
            if (introClip != null) {
                introClip.stop();
            }
            isMuted = true;
        }
    }

    // Bỏ qua phần intro, chuyển luôn đến hết thoại
    private void skipIntro() {
        dialogueIndex = dialogues.size();
        showNextDialogue();
    }
}
