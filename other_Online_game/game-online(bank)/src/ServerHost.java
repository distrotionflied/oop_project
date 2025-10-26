// ServerHost.java
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerHost {
    private static final int PORT = 8888;
    private static LobbyState lobbyState = new LobbyState();
    private static List<ObjectOutputStream> clientStreams = new CopyOnWriteArrayList<>();
    private static LobbyWindow hostWindow; // UI ของ host
    // เพิ่มตัวแปร
    private static GameState gameState = new GameState();
    // เพิ่มตัวแปรที่ด้านบนของ class
private static Map<ObjectInputStream, String> clientNames = new HashMap<>();

    public static void main(String[] args) {
        // 1. เปิดหน้าต่าง host ทันที (ไม่ผ่าน network!)
        SwingUtilities.invokeLater(() -> {
            hostWindow = new LobbyWindow("Host", true, () -> {}, msg -> {
                                            String fullMsg = "Host: " + msg;
                                            lobbyState.addChatMessage(fullMsg);
                                            SwingUtilities.invokeLater(() -> hostWindow.updateState(lobbyState));
                                            broadcastState();
                                        });
        });

        // 2. เปิดเซิร์ฟเวอร์ (รันใน background)
        new Thread(ServerHost::startServer).start();
    }

    private static void startServer() {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                clientStreams.add(out);
                sendCurrentStateToClient(out); // ส่ง state ปัจจุบันให้ client ใหม่

                new Thread(() -> handleClient(in, clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // เรียกเมื่อ host กด "พร้อม" (แต่ host ไม่มีปุ่มพร้อม — ใช้สำหรับอนาคต)
    private static void onHostReady() {
        // Host ไม่ต้อง "พร้อม" — เป็นผู้ควบคุม
    }

    // เรียกเมื่อ host กด "เริ่มเกม"
    public static void onStartGame() {
        System.out.println("Host started the game!");
        JOptionPane.showMessageDialog(hostWindow, "Game started!", "notification", JOptionPane.INFORMATION_MESSAGE);
        // ส่งคำสั่ง START_GAME ไปยัง client ทุกคนได้ที่นี่

        gameState.setGameStarted(true);

        // ส่งคำสั่งเริ่มเกมให้ client
        for (ObjectOutputStream out : clientStreams) {
            try {
                out.writeObject("START_GAME");
                out.flush();
            } catch (IOException e) {}
        }

        SwingUtilities.invokeLater(() -> {
            // Host เริ่มเกม
            mainGame game = new mainGame(); // ← ใช้ constructor เดิม
            game.setupMultiplayer("Host", true); // ← ตั้งค่า multiplayer
            game.setVisible(true);
            hostWindow.dispose();
        });

    }

    // ใน handleClient
    private static void handleClient(ObjectInputStream in, Socket socket) {
        String currentPlayerName = "Unknown"; // ← ประกาศตัวแปรชื่ออื่นที่นี่
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof String) {
                    String msg = (String) obj;
                    // ใน handleClient() — ด้านบนสุด
                    if (msg.startsWith("JOIN:")) {
                        currentPlayerName = msg.substring(5);
                        lobbyState.setReady(currentPlayerName, false);
                        gameState.addPlayer(currentPlayerName); // ← เพิ่มนี้
                        SwingUtilities.invokeLater(() -> hostWindow.updateState(lobbyState));
                        broadcastState();
                    }else if (msg.startsWith("READY:")) {
                        currentPlayerName = msg.substring(6);
                        // อัปเดต state
                        lobbyState.setReady(currentPlayerName, true);
                        // อัปเดต UI ของ host
                        SwingUtilities.invokeLater(() -> hostWindow.updateState(lobbyState));
                        // ส่ง state ไปยัง client ทุกคน
                        broadcastState();
                    }

                    // ใน handleClient() — เพิ่มเงื่อนไขนี้ใน if (obj instanceof String)
                    if (msg.startsWith("CHAT:")) {
                        String chatMsg = msg.substring(5);
                        String fullMessage = currentPlayerName + ": " + chatMsg;
                        
                        lobbyState.addChatMessage(fullMessage);
                        
                        // อัปเดต host UI
                        SwingUtilities.invokeLater(() -> hostWindow.updateState(lobbyState));
                        
                        // ส่งไปทุก client
                        broadcastState();
                    }

                    // ใน handleClient() เพิ่มเงื่อนไขนี้
                    if (msg.startsWith("MOVE:")) {
                        String[] parts = msg.split(":");
                        String movingPlayerName = parts[1]; // ← ใช้ชื่ออื่น
                        int newPosition = Integer.parseInt(parts[2]);
                        System.out.println(movingPlayerName + " moved to " + newPosition);
                        
                        // ส่งต่อให้ client ทุกคน
                        for (ObjectOutputStream out : clientStreams) {
                            try {
                                out.writeObject("UPDATE:" + movingPlayerName + ":" + newPosition);
                                out.flush();
                            } catch (IOException e) {}
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Client " + currentPlayerName + " disconnected");
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void sendCurrentStateToClient(ObjectOutputStream out) {
        try {
            out.writeObject(lobbyState);
            out.flush();
        } catch (IOException e) {
            clientStreams.remove(out);
        }
    }

    private static void broadcastState() {
        for (ObjectOutputStream out : clientStreams) {
            try {
                out.reset();
                out.writeObject(lobbyState);
                out.flush();
            } catch (IOException e) {
                clientStreams.remove(out);
            }
        }
    }

    // เรียกจาก LobbyWindow เมื่อ host กดปุ่ม "เริ่มเกม"
    public static void notifyHostWantsToStart() {
        boolean allReady = lobbyState.areAllNonHostPlayersReady();
        if (allReady) {
            onStartGame();
        } else {
            JOptionPane.showMessageDialog(hostWindow, "Player aren't ready!", "notification", JOptionPane.WARNING_MESSAGE);
        }
    }
}