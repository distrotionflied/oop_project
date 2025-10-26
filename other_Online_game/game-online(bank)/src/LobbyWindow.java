// LobbyWindow.java
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.BorderLayout;
import java.util.function.Consumer;

public class LobbyWindow extends JFrame {
    private final Map<String, PlayerPanel> playerPanels = new HashMap<>();
    private final String localPlayerName;
    private final boolean isHost;
    private final Runnable onReadyCallback;
    private final JPanel playersPanel = new JPanel();
    private JButton startGameButton;

    private Consumer<String> onChatCallback; // ← ต้อง import java.util.function.Consumer;

    private ChatWindow chatWindow;

    public LobbyWindow(String playerName, boolean isHost, Runnable onReady, Consumer<String> onChat){
        this.localPlayerName = playerName;
        this.isHost = isHost;
        this.onReadyCallback = onReady;
        this.onChatCallback = onChat; // ← เพิ่มบรรทัดนี้

        setTitle(isHost ? "Waiting Room (Host)" : "Waiting Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ส่วนหัว
        JLabel title = new JLabel("Waiting Room", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // ส่วนผู้เล่น
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        JScrollPane playersScroll = new JScrollPane(playersPanel);
        
        // รวมทุกอย่าง
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(playersScroll, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ปุ่มเริ่มเกม (ถ้าเป็น host)
        startGameButton = new JButton("start");
        startGameButton.setVisible(isHost);
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(e -> {
            if (isHost) {
                ServerHost.notifyHostWantsToStart();
            }
        });
        add(startGameButton, BorderLayout.SOUTH);

        pack();
        setSize(350, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        // วางท้าย constructor (หลัง setVisible(true) ของล็อบบี้)
        SwingUtilities.invokeLater(() -> {
            chatWindow = new ChatWindow(
                (isHost ? "Host" : playerName) + " - Chat",
                onChat // ← ต้องรับ onChat ใน constructor
            );
        });
    }

    public void updateState(LobbyState state) {
        for (String name : state.getReadyStatus().keySet()) {
            if (!playerPanels.containsKey(name)) {
                boolean isLocal = isHost && name.equals("Host") || (!isHost && name.equals(localPlayerName));
                PlayerPanel pp = new PlayerPanel(name, isLocal, isHost && name.equals("Host"), onReadyCallback);
                playerPanels.put(name, pp);
                playersPanel.add(pp);
                playersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            playerPanels.get(name).setReady(state.getReadyStatus().get(name));
        }

        if (isHost) {
            startGameButton.setEnabled(state.areAllNonHostPlayersReady());
        }

        playersPanel.revalidate();
        playersPanel.repaint();

        // เพิ่มท้ายเมธอด updateState():
        if (chatWindow != null) {
            chatWindow.clear(); // ✅ ใช้เมธอดแทน
            for (String msg : state.getChatMessages()) {
                chatWindow.appendMessage(msg);
            }
        }
    }

    private class PlayerPanel extends JPanel {
        private final JLabel nameLabel;
        private final JButton readyButton;
        private final boolean isLocal;
        private final boolean isHostPlayer;
        private boolean isReady = false;

        public PlayerPanel(String name, boolean isLocal, boolean isHostPlayer, Runnable onReady) {
            this.isLocal = isLocal;
            this.isHostPlayer = isHostPlayer;
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            nameLabel = new JLabel(name + (isHostPlayer ? " (Host)" : ""));
            nameLabel.setPreferredSize(new Dimension(180, 25));

            if (isHostPlayer) {
                readyButton = new JButton("Host");
                readyButton.setEnabled(false);
                readyButton.setBackground(new Color(220, 220, 255));
            } else if (isLocal) {
                readyButton = new JButton("Not ready");
                readyButton.addActionListener(e -> {
                    isReady = !isReady;
                    onReady.run();
                });
            } else {
                readyButton = new JButton("Not ready");
                readyButton.setEnabled(false);
            }
            readyButton.setPreferredSize(new Dimension(120, 25));
            add(nameLabel);
            add(readyButton);
        }

        public void setReady(boolean ready) {
            if (isHostPlayer) return;
            isReady = ready;
            if (ready) {
                readyButton.setText("ready!");
                readyButton.setBackground(Color.GREEN);
                readyButton.setForeground(Color.WHITE);
            } else {
                readyButton.setText("Not ready");
                readyButton.setBackground(null);
                readyButton.setForeground(null);
            }
        }
    }
}