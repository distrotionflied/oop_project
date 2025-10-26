// GameClient.java
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static LobbyWindow lobbyWindow;
    // เพิ่มตัวแปร
    private static String playerName;


    public static void main(String[] args) {
        final String inputName = JOptionPane.showInputDialog("Enter your name:");
        playerName = (inputName == null || inputName.trim().isEmpty()) ?
                "Player" + (int)(Math.random() * 1000) : inputName.trim();

        try {
            Socket socket = new Socket("localhost", 8888);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("JOIN:" + playerName);
            out.flush();

            SwingUtilities.invokeLater(() -> {
                // client ทั่วไป → isHost = false
                lobbyWindow = new LobbyWindow(playerName, false, () -> sendReady(playerName), GameClient::sendChatMessage);
            });

            new Thread(() -> {
                try {
                    while (true) {
                        Object obj = in.readObject();
                        if (obj instanceof LobbyState) {
                            LobbyState state = (LobbyState) obj;
                            SwingUtilities.invokeLater(() -> lobbyWindow.updateState(state));

                        } 
                        else if (obj instanceof String) {
                                // ✅ รับ String messages (START_GAME, UPDATE, etc.)
                                String msg = (String) obj;
                            if (msg.equals("START_GAME")) {
                                // เริ่มเกมเมื่อได้รับสัญญาณ
                                SwingUtilities.invokeLater(() -> {
                                    mainGame game = new mainGame(); // ใช้ constructor เดิม
                                    game.setupMultiplayer(playerName, false); // ตั้งค่า multiplayer
                                    game.setVisible(true);
                                    lobbyWindow.dispose(); // ปิด lobby
                                });
                            }
                            else if (msg.startsWith("UPDATE:")) {
                                // อัปเดตตำแหน่งผู้เล่นอื่น (ไว้สำหรับอนาคต)
                                String[] parts = msg.split(":");
                                System.out.println(parts[1] + " moved to " + parts[2]);
                            }
                            // ✅ เพิ่มรับ GAME_STATE ถ้ามี
                            else if (msg.startsWith("GAME_STATE:")) {
                                // รับข้อมูลเกม (ถ้าคุณใช้ระบบนี้)
                                String gameData = msg.substring(11);
                                System.out.println("Game state: " + gameData);
                            }

                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Disconnected\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void sendReady(String playerName) {
        try {
            out.writeObject("READY:" + playerName);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // เพิ่มเมธอดนี้ใน GameClient
    public static void sendChatMessage(String message) {
        try {
            out.writeObject("CHAT:" + message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}